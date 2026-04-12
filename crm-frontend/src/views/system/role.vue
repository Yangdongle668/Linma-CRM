<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建角色</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleKey" label="角色标识" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'info'">
              {{ row.status === '0' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handleAssignMenu(row)">分配菜单</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="角色名称" required>
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色标识" required>
          <el-input v-model="roleForm.roleKey" placeholder="请输入角色标识" />
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
const dialogTitle = ref('新建角色')
const tableData = ref([
  { id: 1, roleName: '超级管理员', roleKey: 'admin', status: '0' },
  { id: 2, roleName: '销售员', roleKey: 'sales', status: '0' },
])
const roleForm = ref({ roleName: '', roleKey: '' })
function handleAdd() { dialogTitle.value = '新建角色'; dialogVisible.value = true }
function handleEdit(row: any) { dialogTitle.value = '编辑角色'; Object.assign(roleForm.value, row); dialogVisible.value = true }
function handleAssignMenu(row: any) { ElMessage.info(`分配菜单: ${row.roleName}`) }
function handleDelete(row: any) { ElMessage.info(`删除角色: ${row.roleName}`) }
function handleSubmit() { ElMessage.success('保存成功'); dialogVisible.value = false }
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
