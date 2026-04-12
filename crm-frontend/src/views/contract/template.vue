<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>合同模板</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建模板</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="模板名称">
          <el-input placeholder="请输入模板名称" clearable />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select placeholder="请选择类型" clearable>
            <el-option label="销售合同" value="sales" />
            <el-option label="采购合同" value="purchase" />
            <el-option label="代理合同" value="agency" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search">搜索</el-button>
          <el-button icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="templateList" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="templateName" label="模板名称" />
        <el-table-column prop="templateCode" label="模板编码" width="150" />
        <el-table-column prop="templateType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ getTemplateTypeLabel(row.templateType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handlePreview(row)">预览</el-button>
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

    <!-- 编辑模板对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="templateForm" label-width="100px">
        <el-form-item label="模板名称" required>
          <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板编码">
          <el-input v-model="templateForm.templateCode" placeholder="请输入模板编码" />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="templateForm.templateType" placeholder="请选择类型">
            <el-option label="销售合同" value="sales" />
            <el-option label="采购合同" value="purchase" />
            <el-option label="代理合同" value="agency" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板内容" required>
          <el-input
            v-model="templateForm.content"
            type="textarea"
            :rows="15"
            placeholder="请输入模板内容，支持变量：{{customerName}}, {{amount}}, {{date}}等"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="templateForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog v-model="previewVisible" title="模板预览" width="800px">
      <div class="preview-content">
        <pre>{{ previewContent }}</pre>
      </div>
      <template #footer>
        <el-button @click="previewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getTemplateList, 
  createTemplate, 
  updateTemplate, 
  deleteTemplate,
  type ContractTemplate 
} from '@/api/contract'

const loading = ref(false)
const dialogVisible = ref(false)
const previewVisible = ref(false)
const dialogTitle = ref('新建模板')
const total = ref(0)
const previewContent = ref('')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
})

const templateList = ref<ContractTemplate[]>([])

const templateForm = reactive<ContractTemplate>({
  id: undefined,
  templateName: '',
  templateCode: '',
  templateType: '',
  content: '',
  status: 'active',
  remark: '',
})

const getTemplateTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    sales: '销售合同',
    purchase: '采购合同',
    agency: '代理合同',
  }
  return labels[type] || type
}

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await getTemplateList(queryParams)
    templateList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新建
function handleAdd() {
  dialogTitle.value = '新建模板'
  Object.assign(templateForm, {
    id: undefined,
    templateName: '',
    templateCode: '',
    templateType: '',
    content: '',
    status: 'active',
    remark: '',
  })
  dialogVisible.value = true
}

// 编辑
function handleEdit(row: ContractTemplate) {
  dialogTitle.value = '编辑模板'
  Object.assign(templateForm, row)
  dialogVisible.value = true
}

// 预览
function handlePreview(row: ContractTemplate) {
  previewContent.value = row.content
  previewVisible.value = true
}

// 删除
async function handleDelete(row: ContractTemplate) {
  try {
    await ElMessageBox.confirm('确定要删除该模板吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    if (row.id) {
      await deleteTemplate([row.id])
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

// 提交
async function handleSubmit() {
  try {
    if (templateForm.id) {
      await updateTemplate(templateForm)
      ElMessage.success('更新成功')
    } else {
      await createTemplate(templateForm)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
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

.preview-content {
  max-height: 500px;
  overflow-y: auto;
  pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    font-family: inherit;
    line-height: 1.6;
  }
}
</style>
