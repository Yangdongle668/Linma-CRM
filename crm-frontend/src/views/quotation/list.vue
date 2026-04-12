<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>报价单列表</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建报价单</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="报价单号">
          <el-input v-model="queryParams.quotationNo" placeholder="请输入报价单号" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="待审批" value="pending_approval" />
            <el-option label="已批准" value="approved" />
            <el-option label="已发送" value="sent" />
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
        <el-table-column prop="quotationNo" label="报价单号" width="150" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="validUntil" label="有效期至" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" @click="handleSend(row)">发送</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { getQuotationList, deleteQuotation } from '@/api/quotation'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  quotationNo: '',
  customerName: '',
  status: '',
})

const tableData = ref([
  {
    id: 1,
    quotationNo: 'QUO20260411001',
    customerName: 'ABC Company',
    amount: 18000.00,
    status: 'approved',
    validUntil: '2026-05-11',
  },
])

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    draft: '草稿',
    pending_approval: '待审批',
    approved: '已批准',
    sent: '已发送',
  }
  return labels[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    draft: 'info',
    pending_approval: 'warning',
    approved: 'success',
    sent: 'primary',
  }
  return types[status] || ''
}

const handleAdd = () => {
  console.log('Add quotation')
}

const handleView = (row: any) => {
  console.log('View quotation:', row)
}

const handleEdit = (row: any) => {
  console.log('Edit quotation:', row)
}

const handleSend = (row: any) => {
  console.log('Send quotation:', row)
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该报价单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteQuotation([row.id])
    ElMessage.success('删除成功')
    // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete quotation:', error)
    }
  }
}

const handleSearch = () => {
  queryParams.value.pageNum = 1
  // 重新加载数据
}

const handleReset = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    quotationNo: '',
    customerName: '',
    status: '',
  }
  // 重新加载数据
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
