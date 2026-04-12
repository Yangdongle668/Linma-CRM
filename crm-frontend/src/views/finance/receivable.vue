<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>应收账款</span>
          <el-button type="primary" icon="Plus" @click="handleRecordPayment">记录收款</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="应收单号">
          <el-input v-model="queryParams.receivableNo" placeholder="请输入应收单号" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="未收款" value="unpaid" />
            <el-option label="部分收款" value="partial" />
            <el-option label="已收款" value="paid" />
            <el-option label="逾期" value="overdue" />
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
        <el-table-column prop="receivableNo" label="应收单号" width="150" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="orderNo" label="订单号" width="150" />
        <el-table-column prop="amount" label="应收金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedAmount" label="已收金额" width="120">
          <template #default="{ row }">
            ¥{{ row.receivedAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="120">
          <template #default="{ row }">
            ¥{{ row.balance?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="success" @click="handleRecordPaymentRow(row)">收款</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getReceivableList, type Receivable, type ReceivableQuery } from '@/api/finance'

const loading = ref(false)
const total = ref(0)

const queryParams = reactive<ReceivableQuery>({
  pageNum: 1,
  pageSize: 10,
  receivableNo: '',
  customerId: undefined,
  status: '',
})

const tableData = ref<Receivable[]>([])

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    unpaid: '未收款',
    partial: '部分收款',
    paid: '已收款',
    overdue: '逾期',
  }
  return labels[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    unpaid: 'danger',
    partial: 'warning',
    paid: 'success',
    overdue: 'danger',
  }
  return types[status] || ''
}

async function loadData() {
  loading.value = true
  try {
    const res = await getReceivableList(queryParams)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.pageNum = 1
  loadData()
}

function handleReset() {
  queryParams.receivableNo = ''
  queryParams.customerId = undefined
  queryParams.status = ''
  queryParams.pageNum = 1
  loadData()
}

function handleRecordPayment() {
  ElMessage.info('记录收款功能')
}

function handleRecordPaymentRow(row: Receivable) {
  ElMessage.info(`记录收款: ${row.receivableNo}`)
}

function handleView(row: Receivable) {
  ElMessage.info(`查看详情: ${row.receivableNo}`)
}

function handleSizeChange(size: number) {
  queryParams.pageSize = size
  loadData()
}

function handleCurrentChange(page: number) {
  queryParams.pageNum = page
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
