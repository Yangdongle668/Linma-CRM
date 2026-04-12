<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>跟进记录</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建跟进</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="客户名称">
          <el-input v-model="queryParams.keyword" placeholder="请输入客户名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="跟进方式">
          <el-select v-model="queryParams.followType" placeholder="请选择" clearable style="width: 120px">
            <el-option label="邮件" value="email" />
            <el-option label="电话" value="call" />
            <el-option label="会议" value="meeting" />
            <el-option label="微信" value="wechat" />
            <el-option label="拜访" value="visit" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="followList" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="customerName" label="客户名称" min-width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="goToCustomer(row.customerId)">
              {{ row.customerName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="跟进方式" width="100">
          <template #default="{ row }">
            <el-tag :type="getFollowUpTypeTag(row.followType)" size="small">
              {{ getFollowUpTypeLabel(row.followType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subject" label="主题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="content" label="跟进内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="nextFollowTime" label="下次跟进" width="120" />
        <el-table-column prop="creatorName" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" @close="resetForm">
      <el-form ref="formRef" :model="followForm" :rules="rules" label-width="100px">
        <el-form-item label="关联客户" prop="customerId">
          <el-select
            v-model="followForm.customerId"
            placeholder="请选择客户或输入关键词搜索"
            filterable
            remote
            reserve-keyword
            :remote-method="searchCustomers"
            :loading="customerSearchLoading"
            style="width: 100%"
            @change="handleCustomerChange"
          >
            <el-option
              v-for="customer in customerOptions"
              :key="customer.id"
              :label="customer.companyName"
              :value="customer.id"
            >
              <div class="customer-option">
                <span>{{ customer.companyName }}</span>
                <span class="customer-country">{{ customer.country }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="跟进方式" prop="followType">
          <el-radio-group v-model="followForm.followType">
            <el-radio label="email">
              <el-icon><Message /></el-icon>
              邮件
            </el-radio>
            <el-radio label="call">
              <el-icon><Phone /></el-icon>
              电话
            </el-radio>
            <el-radio label="meeting">会议</el-radio>
            <el-radio label="wechat">微信</el-radio>
            <el-radio label="visit">拜访</el-radio>
            <el-radio label="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="主题" prop="subject">
          <el-input v-model="followForm.subject" placeholder="请输入跟进主题" />
        </el-form-item>

        <el-form-item label="跟进内容" prop="content">
          <el-input
            v-model="followForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入跟进内容"
          />
        </el-form-item>

        <el-form-item label="下次跟进">
          <el-date-picker
            v-model="followForm.nextFollowTime"
            type="datetime"
            placeholder="选择下次跟进时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <!-- 邮件关联提示（当跟进方式为邮件时显示） -->
        <el-alert
          v-if="followForm.followType === 'email'"
          title="邮件跟进将自动关联到该客户，系统会根据邮箱域名自动匹配客户"
          type="info"
          :closable="false"
          show-icon
        />
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Message, Phone } from '@element-plus/icons-vue'
import {
  getFollowUpList,
  createFollowUp,
  updateFollowUp,
  deleteFollowUp,
  type FollowUp,
  type FollowUpQuery,
} from '@/api/followUp'
import { getCustomerList, type Customer } from '@/api/customer'

const router = useRouter()
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建跟进')
const formRef = ref<FormInstance>()
const total = ref(0)
const customerSearchLoading = ref(false)

const queryParams = reactive<FollowUpQuery>({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  followType: '',
})

const followList = ref<FollowUp[]>([])

const followForm = reactive<Partial<FollowUp>>({
  id: undefined,
  customerId: undefined,
  customerName: '',
  followType: 'email',
  subject: '',
  content: '',
  nextFollowTime: '',
})

const rules: FormRules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  content: [{ required: true, message: '请输入跟进内容', trigger: 'blur' }],
}

const customerOptions = ref<Customer[]>([])

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await getFollowUpList(queryParams)
    followList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索客户
async function searchCustomers(query: string) {
  if (!query) {
    customerOptions.value = []
    return
  }
  customerSearchLoading.value = true
  try {
    const res = await getCustomerList({ keyword: query, pageSize: 10 })
    customerOptions.value = res.records || []
  } catch (error) {
    console.error('搜索客户失败:', error)
  } finally {
    customerSearchLoading.value = false
  }
}

// 客户选择变化
function handleCustomerChange(customerId: number) {
  const customer = customerOptions.value.find(c => c.id === customerId)
  if (customer) {
    followForm.customerName = customer.companyName
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
  queryParams.followType = ''
  queryParams.pageNum = 1
  loadData()
}

// 新建
function handleAdd() {
  dialogTitle.value = '新建跟进'
  resetForm()
  // 默认设置为邮件跟进
  followForm.followType = 'email'
  dialogVisible.value = true
}

// 编辑
function handleEdit(row: FollowUp) {
  dialogTitle.value = '编辑跟进'
  Object.assign(followForm, row)
  dialogVisible.value = true
}

// 删除
async function handleDelete(row: FollowUp) {
  try {
    await ElMessageBox.confirm('确定要删除该跟进记录吗？', '提示', {
      type: 'warning',
    })
    if (row.id) {
      await deleteFollowUp([row.id])
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

    if (followForm.id) {
      await updateFollowUp(followForm as FollowUp)
    } else {
      await createFollowUp(followForm as FollowUp)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
function resetForm() {
  Object.assign(followForm, {
    id: undefined,
    customerId: undefined,
    customerName: '',
    followType: 'email',
    subject: '',
    content: '',
    nextFollowTime: '',
  })
  formRef.value?.clearValidate()
}

// 跳转到客户详情
function goToCustomer(customerId: number | undefined) {
  if (customerId) {
    router.push(`/customer/detail/${customerId}`)
  }
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

// 获取跟进方式标签
function getFollowUpTypeLabel(type: string) {
  const labels: Record<string, string> = {
    email: '邮件',
    call: '电话',
    meeting: '会议',
    wechat: '微信',
    visit: '拜访',
    other: '其他',
  }
  return labels[type] || type
}

// 获取跟进方式标签类型
function getFollowUpTypeTag(type: string) {
  const types: Record<string, any> = {
    email: 'primary',
    call: 'success',
    meeting: 'warning',
    wechat: 'info',
    visit: '',
    other: '',
  }
  return types[type] || ''
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

.customer-option {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .customer-country {
    font-size: 12px;
    color: #909399;
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
