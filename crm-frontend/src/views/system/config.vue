<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header><span>系统配置</span></template>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="configName" label="配置名称" />
        <el-table-column prop="configKey" label="配置键" />
        <el-table-column prop="configValue" label="配置值" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" title="编辑配置" width="500px">
      <el-form :model="configForm" label-width="80px">
        <el-form-item label="配置值" required>
          <el-input v-model="configForm.configValue" placeholder="请输入配置值" />
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
const tableData = ref([
  { id: 1, configName: '系统名称', configKey: 'sys.name', configValue: '外贸CRM系统', remark: '系统显示名称' },
  { id: 2, configName: '每页条数', configKey: 'sys.pageSize', configValue: '10', remark: '默认分页大小' },
])
const configForm = ref({ configValue: '' })
function handleEdit(row: any) { Object.assign(configForm.value, row); dialogVisible.value = true }
function handleSubmit() { ElMessage.success('保存成功'); dialogVisible.value = false }
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
</style>
