<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>合同列表</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建合同</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="合同编号">
          <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="待审批" value="pending_approval" />
            <el-option label="已批准" value="approved" />
            <el-option label="已签署" value="signed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="contractNo" label="合同编号" width="150" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="currency" label="币种" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="signDate" label="签署日期" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" @click="handleSign(row)">签署</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="queryParams.pageSize"
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="formData.customerId" placeholder="请选择客户" style="width: 100%">
            <el-option label="ABC Trading Co." :value="1" />
            <el-option label="XYZ Import Ltd." :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="合同金额" prop="amount">
          <el-input-number v-model="formData.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="formData.currency" placeholder="请选择币种">
            <el-option label="人民币 (CNY)" value="CNY" />
            <el-option label="美元 (USD)" value="USD" />
            <el-option label="欧元 (EUR)" value="EUR" />
          </el-select>
        </el-form-item>
        <el-form-item label="签署日期">
          <el-date-picker
            v-model="formData.signDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="formData.startDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="formData.endDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="付款条款">
          <el-input v-model="formData.paymentTerms" type="textarea" :rows="2" placeholder="请输入付款条款" />
        </el-form-item>
        <el-form-item label="交货条款">
          <el-input v-model="formData.deliveryTerms" type="textarea" :rows="2" placeholder="请输入交货条款" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { 
  getContractList, 
  createContract, 
  updateContract, 
  deleteContract,
  signContract,
  type Contract, 
  type ContractQuery,
  type ContractCreateDTO,
  type ContractUpdateDTO
} from '@/api/contract'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建合同')
const formRef = ref<FormInstance>()
const total = ref(0)

const queryParams = reactive<ContractQuery>({
  pageNum: 1,
  pageSize: 10,
  contractNo: '',
  customerName: '',
  status: '',
})

const formData = reactive<ContractCreateDTO & { id?: number }>({
  customerId: 0,
  amount: 0,
  currency: 'CNY',
  signDate: '',
  startDate: '',
  endDate: '',
  paymentTerms: '',
  deliveryTerms: '',
  remark: '',
})

const rules: FormRules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  amount: [{ required: true, message: '请输入合同金额', trigger: 'blur' }],
}

const tableData = ref<Contract[]>([])

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    draft: '草稿',
    pending_approval: '待审批',
    approved: '已批准',
    signed: '已签署',
  }
  return labels[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    draft: 'info',
    pending_approval: 'warning',
    approved: 'success',
    signed: 'primary',
  }
  return types[status] || ''
}

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await getContractList(queryParams)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  queryParams.pageNum = 1
  loadData()
}

// 重置
function handleReset() {
  queryParams.contractNo = ''
  queryParams.customerName = ''
  queryParams.status = ''
  queryParams.pageNum = 1
  loadData()
}

// 新建
function handleAdd() {
  dialogTitle.value = '新建合同'
  resetForm()
  dialogVisible.value = true
}

// 查看
function handleView(row: Contract) {
  ElMessage.info(`查看合同: ${row.contractNo}`)
}

// 编辑
function handleEdit(row: Contract) {
  dialogTitle.value = '编辑合同'
  Object.assign(formData, {
    id: row.id,
    customerId: row.customerId,
    amount: row.amount,
    currency: row.currency,
    signDate: row.signDate,
    startDate: row.startDate,
    endDate: row.endDate,
    remark: row.remark,
  })
  dialogVisible.value = true
}

// 签署
async function handleSign(row: Contract) {
  try {
    await ElMessageBox.confirm(`确定要签署合同 "${row.contractNo}" 吗？`, '提示', {
      type: 'warning',
    })
    const today = new Date().toISOString().split('T')[0]
    await signContract(row.id || 0, today)
    ElMessage.success('签署成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('签署失败:', error)
      ElMessage.error('签署失败')
    }
  }
}

// 删除
async function handleDelete(row: Contract) {
  try {
    await ElMessageBox.confirm(`确定要删除合同 "${row.contractNo}" 吗?`, '提示', {
      type: 'warning',
    })
    if (row.id) {
      await deleteContract([row.id])
      ElMessage.success('删除成功')
      loadData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (formData.id) {
      const updateData: ContractUpdateDTO = {
        id: formData.id,
        amount: formData.amount,
        currency: formData.currency,
        signDate: formData.signDate,
        startDate: formData.startDate,
        endDate: formData.endDate,
        remark: formData.remark,
      }
      await updateContract(updateData)
    } else {
      await createContract(formData)
    }
    
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
function resetForm() {
  Object.assign(formData, {
    id: undefined,
    customerId: 0,
    amount: 0,
    currency: 'CNY',
    signDate: '',
    startDate: '',
    endDate: '',
    paymentTerms: '',
    deliveryTerms: '',
    remark: '',
  })
  formRef.value?.clearValidate()
}

// 分页大小变化
function handleSizeChange(size: number) {
  queryParams.pageSize = size
  loadData()
}

// 页码变化
function handleCurrentChange(page: number) {
  queryParams.pageNum = page
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.page-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
