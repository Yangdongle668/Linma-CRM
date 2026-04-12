<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>客户列表</span>
          <div class="header-actions">
            <el-button type="primary" icon="Plus" @click="handleAdd">新建客户</el-button>
            <el-button icon="Download" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="关键词">
          <el-input
            v-model="queryParams.keyword"
            placeholder="公司名称/联系人/邮箱"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="国家">
          <el-input v-model="queryParams.country" placeholder="请输入国家" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="客户等级">
          <el-select v-model="queryParams.level" placeholder="请选择" clearable style="width: 120px">
            <el-option label="A级" value="A" />
            <el-option label="B级" value="B" />
            <el-option label="C级" value="C" />
          </el-select>
        </el-form-item>
        <el-form-item label="生命周期">
          <el-select v-model="queryParams.customerLifecycle" placeholder="请选择" clearable style="width: 140px">
            <el-option label="潜在客户" value="prospect" />
            <el-option label="销售线索" value="lead" />
            <el-option label="商机" value="opportunity" />
            <el-option label="正式客户" value="customer" />
            <el-option label="流失客户" value="churned" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" border stripe v-loading="loading" @row-click="handleRowClick">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="公司信息" min-width="200">
          <template #default="{ row }">
            <div class="company-info">
              <el-avatar :size="40" :src="row.customerAvatar" class="company-avatar">
                {{ (row.companyName || '?').charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="company-details">
                <div class="company-name">{{ row.companyName }}</div>
                <div class="company-name-cn">{{ row.companyNameCn }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="country" label="国家" width="100" />
        <el-table-column prop="industry" label="行业" width="120" />
        <el-table-column label="等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)" size="small">
              {{ row.level || '未评级' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="生命周期" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.customerLifecycle" size="small">
              {{ getLifecycleLabel(row.customerLifecycle) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="website" label="网站" width="150" show-overflow-tooltip>
          <template #default="{ row }">
            <a v-if="row.website" :href="row.website" target="_blank" class="link">
              {{ row.website }}
            </a>
          </template>
        </el-table-column>
        <el-table-column label="订单金额" width="120">
          <template #default="{ row }">
            <span class="amount">{{ formatMoney(row.totalOrder) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click.stop="handleView(row)">查看</el-button>
            <el-button link type="primary" @click.stop="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click.stop="handleDelete(row)">删除</el-button>
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
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称(英文)" prop="companyName">
              <el-input v-model="formData.companyName" placeholder="请输入公司英文名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司名称(中文)">
              <el-input v-model="formData.companyNameCn" placeholder="请输入公司中文名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="国家" prop="country">
              <el-input v-model="formData.country" placeholder="请输入国家" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市">
              <el-input v-model="formData.city" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="网站">
              <el-input v-model="formData.website" placeholder="请输入公司网站" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业">
              <el-select v-model="formData.industry" placeholder="请选择行业" clearable style="width: 100%">
                <el-option label="制造业" value="manufacturing" />
                <el-option label="贸易" value="trading" />
                <el-option label="科技" value="technology" />
                <el-option label="服务" value="service" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">客户分级</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="客户等级">
              <el-select v-model="formData.level" placeholder="请选择" style="width: 100%">
                <el-option label="A级" value="A" />
                <el-option label="B级" value="B" />
                <el-option label="C级" value="C" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="优先级">
              <el-select v-model="formData.priority" placeholder="请选择" style="width: 100%">
                <el-option label="高" value="high" />
                <el-option label="中" value="medium" />
                <el-option label="低" value="low" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="生命周期">
              <el-select v-model="formData.customerLifecycle" placeholder="请选择" style="width: 100%">
                <el-option label="潜在客户" value="prospect" />
                <el-option label="销售线索" value="lead" />
                <el-option label="商机" value="opportunity" />
                <el-option label="正式客户" value="customer" />
                <el-option label="流失客户" value="churned" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">来源信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户来源">
              <el-select v-model="formData.source" placeholder="请选择" clearable style="width: 100%">
                <el-option label="展会" value="exhibition" />
                <el-option label="阿里巴巴" value="alibaba" />
                <el-option label="官网" value="website" />
                <el-option label="转介绍" value="referral" />
                <el-option label="Google搜索" value="google" />
                <el-option label="社交媒体" value="social" />
                <el-option label="冷开发" value="cold_call" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="首次接触日期">
              <el-date-picker
                v-model="formData.firstContactDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getCustomerList,
  getCustomerById,
  createCustomer,
  updateCustomer,
  deleteCustomer,
  exportCustomers,
  type Customer,
  type CustomerQuery,
} from '@/api/customer'

const router = useRouter()
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建客户')
const formRef = ref<FormInstance>()
const total = ref(0)

const queryParams = reactive<CustomerQuery>({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  country: '',
  level: '',
  customerLifecycle: '',
})

const formData = reactive<Customer>({
  id: undefined,
  companyName: '',
  companyNameCn: '',
  country: '',
  province: '',
  city: '',
  address: '',
  website: '',
  industry: '',
  companySize: '',
  businessType: '',
  mainProducts: '',
  level: '',
  priority: '',
  customerLifecycle: 'prospect',
  source: '',
  acquisitionChannel: '',
  firstContactDate: '',
  paymentTerms: '',
  creditRating: '',
  timezone: '',
  languagePreference: '',
  emailDomain: '',
  socialLinkedin: '',
  socialFacebook: '',
  socialTwitter: '',
  establishedYear: undefined,
  annualRevenue: undefined,
  taxId: '',
  registrationNumber: '',
  riskLevel: '',
  opportunityValue: undefined,
  satisfactionScore: undefined,
  npsScore: undefined,
  ownerId: undefined,
  isHighSea: 0,
  lastFollowTime: '',
  totalOrder: 0,
  remark: '',
  status: '1',
})

const rules: FormRules = {
  companyName: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
  country: [{ required: true, message: '请输入国家', trigger: 'blur' }],
}

const tableData = ref<Customer[]>([])

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await getCustomerList(queryParams)
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
  queryParams.keyword = ''
  queryParams.country = ''
  queryParams.level = ''
  queryParams.customerLifecycle = ''
  queryParams.pageNum = 1
  loadData()
}

// 新建
function handleAdd() {
  dialogTitle.value = '新建客户'
  dialogVisible.value = true
}

// 编辑
async function handleEdit(row: Customer) {
  dialogTitle.value = '编辑客户'
  try {
    if (row.id) {
      // Fetch full customer data from backend
      const res = await getCustomerById(row.id)
      if (res.code === 200 && res.data) {
        const data = res.data
        console.log('Loading customer data:', data)
        
        // Directly set each property to ensure reactivity
        formData.id = data.id
        formData.companyName = data.companyName || ''
        formData.companyNameCn = data.companyNameCn || ''
        formData.country = data.country || ''
        formData.province = data.province || ''
        formData.city = data.city || ''
        formData.address = data.address || ''
        formData.website = data.website || ''
        formData.industry = data.industry || ''
        formData.companySize = data.companySize || ''
        formData.businessType = data.businessType || ''
        formData.mainProducts = data.mainProducts || ''
        formData.level = data.level || ''
        formData.priority = data.priority || ''
        formData.customerLifecycle = data.customerLifecycle || 'prospect'
        formData.source = data.source || ''
        formData.acquisitionChannel = data.acquisitionChannel || ''
        formData.firstContactDate = data.firstContactDate || ''
        formData.paymentTerms = data.paymentTerms || ''
        formData.creditRating = data.creditRating || ''
        formData.timezone = data.timezone || ''
        formData.languagePreference = data.languagePreference || ''
        formData.emailDomain = data.emailDomain || ''
        formData.socialLinkedin = data.socialLinkedin || ''
        formData.socialFacebook = data.socialFacebook || ''
        formData.socialTwitter = data.socialTwitter || ''
        formData.establishedYear = data.establishedYear || undefined
        formData.annualRevenue = data.annualRevenue || undefined
        formData.taxId = data.taxId || ''
        formData.registrationNumber = data.registrationNumber || ''
        formData.riskLevel = data.riskLevel || ''
        formData.opportunityValue = data.opportunityValue || undefined
        formData.satisfactionScore = data.satisfactionScore || undefined
        formData.npsScore = data.npsScore || undefined
        formData.ownerId = data.ownerId || undefined
        formData.isHighSea = data.isHighSea || 0
        formData.lastFollowTime = data.lastFollowTime || ''
        formData.totalOrder = data.totalOrder || 0
        formData.remark = data.remark || ''
        formData.status = data.status !== undefined ? data.status : '1'
        
        console.log('Form data after assignment:', formData)
      } else {
        ElMessage.error('获取客户详情失败：数据为空')
        return
      }
    } else {
      resetForm()
    }
    dialogVisible.value = true
  } catch (error) {
    console.error('获取客户详情失败:', error)
    ElMessage.error('获取客户详情失败')
  }
}

// 查看
function handleView(row: Customer) {
  if (row.id) {
    router.push(`/customer/detail/${row.id}`)
  }
}

// 行点击
function handleRowClick(row: Customer) {
  handleView(row)
}

// 删除
async function handleDelete(row: Customer) {
  try {
    await ElMessageBox.confirm(`确定要删除客户 "${row.companyName}" 吗?`, '提示', {
      type: 'warning',
    })
    if (row.id) {
      await deleteCustomer([row.id])
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

// 导出
async function handleExport() {
  try {
    const res = await exportCustomers(queryParams)
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `客户列表_${new Date().getTime()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 提交表单
async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    if (formData.id) {
      await updateCustomer(formData)
    } else {
      await createCustomer(formData)
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
  formData.id = undefined
  formData.companyName = ''
  formData.companyNameCn = ''
  formData.country = ''
  formData.province = ''
  formData.city = ''
  formData.address = ''
  formData.website = ''
  formData.industry = ''
  formData.companySize = ''
  formData.businessType = ''
  formData.mainProducts = ''
  formData.level = ''
  formData.priority = ''
  formData.customerLifecycle = 'prospect'
  formData.source = ''
  formData.acquisitionChannel = ''
  formData.firstContactDate = ''
  formData.paymentTerms = ''
  formData.creditRating = ''
  formData.timezone = ''
  formData.languagePreference = ''
  formData.emailDomain = ''
  formData.socialLinkedin = ''
  formData.socialFacebook = ''
  formData.socialTwitter = ''
  formData.establishedYear = undefined
  formData.annualRevenue = undefined
  formData.taxId = ''
  formData.registrationNumber = ''
  formData.riskLevel = ''
  formData.opportunityValue = undefined
  formData.satisfactionScore = undefined
  formData.npsScore = undefined
  formData.ownerId = undefined
  formData.isHighSea = 0
  formData.lastFollowTime = ''
  formData.totalOrder = 0
  formData.remark = ''
  formData.status = '1'
  
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

// 格式化金额
function formatMoney(amount: number | undefined) {
  if (!amount) return '$0.00'
  return `$${amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

// 获取等级类型
function getLevelType(level: string | undefined) {
  const types: Record<string, any> = {
    A: 'success',
    B: 'warning',
    C: 'info',
  }
  return types[level || ''] || ''
}

// 获取生命周期标签
function getLifecycleLabel(lifecycle: string) {
  const labels: Record<string, string> = {
    prospect: '潜在客户',
    lead: '销售线索',
    opportunity: '商机',
    customer: '正式客户',
    churned: '流失客户',
  }
  return labels[lifecycle] || lifecycle
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

  .header-actions {
    display: flex;
    gap: 8px;
  }
}

.search-form {
  margin-bottom: 20px;
}

.company-info {
  display: flex;
  align-items: center;
  gap: 12px;

  .company-avatar {
    flex-shrink: 0;
  }

  .company-details {
    flex: 1;
    min-width: 0;

    .company-name {
      font-weight: 500;
      color: #303133;
      margin-bottom: 2px;
    }

    .company-name-cn {
      font-size: 12px;
      color: #909399;
    }
  }
}

.link {
  color: #409eff;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

.amount {
  color: #67c23a;
  font-weight: 500;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table__row) {
  cursor: pointer;

  &:hover {
    background-color: #f5f7fa;
  }
}
</style>
