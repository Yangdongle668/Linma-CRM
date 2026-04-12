<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>询盘列表</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建询盘</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="询盘编号">
          <el-input v-model="queryParams.inquiryNo" placeholder="请输入询盘编号" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="pending" />
            <el-option label="处理中" value="processing" />
            <el-option label="已回复" value="replied" />
            <el-option label="已关闭" value="closed" />
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
        <el-table-column prop="inquiryNo" label="询盘编号" width="150" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="subject" label="主题" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="负责人" width="100" />
        <el-table-column prop="createdTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleReply(row)">回复</el-button>
            <el-button link type="warning" @click="handleAssign(row)">分配</el-button>
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

    <!-- 新建询盘对话框 -->
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
        <el-form-item label="主题" prop="subject">
          <el-input v-model="formData.subject" placeholder="请输入询盘主题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="6" placeholder="请输入询盘内容" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="formData.priority" placeholder="请选择优先级">
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源">
          <el-select v-model="formData.source" placeholder="请选择来源">
            <el-option label="网站" value="website" />
            <el-option label="邮件" value="email" />
            <el-option label="电话" value="phone" />
            <el-option label="展会" value="exhibition" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog v-model="replyDialogVisible" title="回复询盘" width="700px">
      <el-form ref="replyFormRef" :model="replyFormData" label-width="100px">
        <el-form-item label="回复内容">
          <el-input 
            v-model="replyFormData.content" 
            type="textarea" 
            :rows="8" 
            placeholder="请输入回复内容" 
          />
        </el-form-item>
        <el-form-item label="是否公开">
          <el-switch v-model="replyFormData.isPublic" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReplySubmit" :loading="replyLoading">发送</el-button>
      </template>
    </el-dialog>

    <!-- 分配对话框 -->
    <el-dialog v-model="assignDialogVisible" title="分配询盘" width="400px">
      <el-form :model="assignFormData" label-width="80px">
        <el-form-item label="负责人">
          <el-select v-model="assignFormData.ownerId" placeholder="请选择负责人" style="width: 100%">
            <el-option label="张三" :value="1" />
            <el-option label="李四" :value="2" />
            <el-option label="王五" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignSubmit" :loading="assignLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { 
  getInquiryList, 
  createInquiry, 
  updateInquiry, 
  deleteInquiry,
  replyInquiry,
  assignInquiry,
  type Inquiry, 
  type InquiryQuery,
  type InquiryCreateDTO,
  type InquiryUpdateDTO,
  type InquiryReplyDTO
} from '@/api/inquiry'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建询盘')
const formRef = ref<FormInstance>()
const total = ref(0)

const queryParams = reactive<InquiryQuery>({
  pageNum: 1,
  pageSize: 10,
  inquiryNo: '',
  customerName: '',
  status: '',
})

const formData = reactive<InquiryCreateDTO & { id?: number }>({
  customerId: 0,
  subject: '',
  content: '',
  priority: 'medium',
  source: 'website',
})

const rules: FormRules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  subject: [{ required: true, message: '请输入主题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
}

const tableData = ref<Inquiry[]>([])

// 回复相关
const replyDialogVisible = ref(false)
const replyLoading = ref(false)
const replyFormRef = ref<FormInstance>()
const replyFormData = reactive<InquiryReplyDTO>({
  inquiryId: 0,
  content: '',
  isPublic: true,
})

// 分配相关
const assignDialogVisible = ref(false)
const assignLoading = ref(false)
const assignFormData = reactive({
  inquiryId: 0,
  ownerId: 0,
})

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    pending: '待处理',
    processing: '处理中',
    replied: '已回复',
    closed: '已关闭',
  }
  return labels[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    pending: 'warning',
    processing: 'primary',
    replied: 'success',
    closed: 'info',
  }
  return types[status] || ''
}

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await getInquiryList(queryParams)
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
  queryParams.inquiryNo = ''
  queryParams.customerName = ''
  queryParams.status = ''
  queryParams.pageNum = 1
  loadData()
}

// 新建
function handleAdd() {
  dialogTitle.value = '新建询盘'
  resetForm()
  dialogVisible.value = true
}

// 查看
function handleView(row: Inquiry) {
  ElMessage.info(`查看询盘: ${row.subject}`)
}

// 回复
function handleReply(row: Inquiry) {
  replyFormData.inquiryId = row.id || 0
  replyFormData.content = ''
  replyDialogVisible.value = true
}

// 提交回复
async function handleReplySubmit() {
  try {
    replyLoading.value = true
    await replyInquiry(replyFormData)
    ElMessage.success('回复成功')
    replyDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('回复失败:', error)
    ElMessage.error('回复失败')
  } finally {
    replyLoading.value = false
  }
}

// 分配
function handleAssign(row: Inquiry) {
  assignFormData.inquiryId = row.id || 0
  assignFormData.ownerId = 0
  assignDialogVisible.value = true
}

// 提交分配
async function handleAssignSubmit() {
  try {
    assignLoading.value = true
    await assignInquiry(assignFormData.inquiryId, assignFormData.ownerId)
    ElMessage.success('分配成功')
    assignDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('分配失败:', error)
    ElMessage.error('分配失败')
  } finally {
    assignLoading.value = false
  }
}

// 编辑
function handleEdit(row: Inquiry) {
  dialogTitle.value = '编辑询盘'
  Object.assign(formData, {
    id: row.id,
    customerId: row.customerId,
    subject: row.subject,
    content: row.content,
    priority: row.priority,
    source: row.source,
  })
  dialogVisible.value = true
}

// 删除
async function handleDelete(row: Inquiry) {
  try {
    await ElMessageBox.confirm(`确定要删除询盘 "${row.subject}" 吗?`, '提示', {
      type: 'warning',
    })
    if (row.id) {
      await deleteInquiry([row.id])
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
      const updateData: InquiryUpdateDTO = {
        id: formData.id,
        subject: formData.subject,
        content: formData.content,
        priority: formData.priority,
        source: formData.source,
      }
      await updateInquiry(updateData)
    } else {
      await createInquiry(formData)
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
    subject: '',
    content: '',
    priority: 'medium',
    source: 'website',
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
