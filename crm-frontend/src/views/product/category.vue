<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>产品分类</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建分类</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="categoryName" label="分类名称" />
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'info'">
              {{ row.status === '0' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="categoryForm" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="categoryForm.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sort" :min="0" />
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
import { ElMessage } from 'element-plus'
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建分类')
const tableData = ref([
  { id: 1, categoryName: '电子产品', sort: 1, status: '0' },
  { id: 2, categoryName: '服装', sort: 2, status: '0' },
])
const categoryForm = ref({ categoryName: '', sort: 0 })
function handleAdd() { dialogTitle.value = '新建分类'; dialogVisible.value = true }
function handleEdit(row: any) { dialogTitle.value = '编辑分类'; Object.assign(categoryForm.value, row); dialogVisible.value = true }
function handleDelete(row: any) { ElMessage.info(`删除分类: ${row.categoryName}`) }
function handleSubmit() { ElMessage.success('保存成功'); dialogVisible.value = false }
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
