<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>邮件模板</span>
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
            <el-option label="报价单" value="quotation" />
            <el-option label="订单确认" value="order" />
            <el-option label="跟进邮件" value="followup" />
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
        <el-table-column prop="templateCode" label="模板编码" />
        <el-table-column prop="templateType" label="类型" width="120" />
        <el-table-column prop="language" label="语言" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
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
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 编辑模板对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="templateForm" label-width="100px">
        <el-form-item label="模板名称" required>
          <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板编码" required>
          <el-input v-model="templateForm.templateCode" placeholder="请输入模板编码" />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="templateForm.templateType" placeholder="请选择类型">
            <el-option label="报价单" value="quotation" />
            <el-option label="订单确认" value="order" />
            <el-option label="跟进邮件" value="followup" />
          </el-select>
        </el-form-item>
        <el-form-item label="语言">
          <el-select v-model="templateForm.language" placeholder="请选择语言">
            <el-option label="中文" value="zh" />
            <el-option label="English" value="en" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮件主题" required>
          <el-input v-model="templateForm.subject" placeholder="请输入邮件主题" />
        </el-form-item>
        <el-form-item label="邮件内容" required>
          <el-input
            v-model="templateForm.content"
            type="textarea"
            :rows="10"
            placeholder="请输入邮件内容，支持变量：{{customerName}}, {{productName}}等"
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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { getTemplateList, createTemplate, updateTemplate, deleteTemplate } from '@/api/message'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新建模板')

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

const templateList = ref([
  {
    id: 1,
    templateName: '报价单模板',
    templateCode: 'QUOTATION_TEMPLATE',
    templateType: 'quotation',
    language: 'zh',
    status: 'active',
  },
])

const templateForm = ref({
  id: undefined as number | undefined,
  templateName: '',
  templateCode: '',
  templateType: '',
  language: 'zh',
  subject: '',
  content: '',
  remark: '',
})

const handleAdd = () => {
  dialogTitle.value = '新建模板'
  templateForm.value = {
    id: undefined,
    templateName: '',
    templateCode: '',
    templateType: '',
    language: 'zh',
    subject: '',
    content: '',
    remark: '',
  }
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑模板'
  templateForm.value = { ...row }
  dialogVisible.value = true
}

const handlePreview = (row: any) => {
  console.log('Preview template:', row)
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该模板吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteTemplate([row.id])
    ElMessage.success('删除成功')
    // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete template:', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    if (templateForm.value.id) {
      await updateTemplate(templateForm.value)
      ElMessage.success('更新成功')
    } else {
      await createTemplate(templateForm.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    // 刷新列表
  } catch (error) {
    console.error('Failed to save template:', error)
  }
}

const handleSizeChange = (val: number) => {
  queryParams.value.pageSize = val
  // 重新加载数据
}

const handleCurrentChange = (val: number) => {
  queryParams.value.pageNum = val
  // 重新加载数据
}
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
