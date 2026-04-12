<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>发货单</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新建发货</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form" :model="queryParams">
        <el-form-item label="发货单号">
          <el-input v-model="queryParams.shippingNo" placeholder="请输入发货单号" clearable />
        </el-form-item>
        <el-form-item label="追踪号">
          <el-input v-model="queryParams.trackingNo" placeholder="请输入追踪号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待发货" value="pending" />
            <el-option label="已发货" value="shipped" />
            <el-option label="运输中" value="in_transit" />
            <el-option label="已送达" value="delivered" />
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
        <el-table-column prop="shippingNo" label="发货单号" width="150" />
        <el-table-column prop="orderNo" label="订单号" width="150" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="trackingNo" label="追踪号" width="150" />
        <el-table-column prop="carrier" label="承运商" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="shipDate" label="发货日期" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getShippingList, type Shipping, type ShippingQuery } from '@/api/shipping'

const loading = ref(false)
const total = ref(0)

const queryParams = reactive<ShippingQuery>({
  pageNum: 1,
  pageSize: 10,
  shippingNo: '',
  trackingNo: '',
  status: '',
})

const tableData = ref<Shipping[]>([])

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    pending: '待发货',
    shipped: '已发货',
    in_transit: '运输中',
    delivered: '已送达',
  }
  return labels[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    pending: 'warning',
    shipped: 'primary',
    in_transit: 'success',
    delivered: 'info',
  }
  return types[status] || ''
}

async function loadData() {
  loading.value = true
  try {
    const res = await getShippingList(queryParams)
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
  queryParams.shippingNo = ''
  queryParams.trackingNo = ''
  queryParams.status = ''
  queryParams.pageNum = 1
  loadData()
}

function handleAdd() {
  ElMessage.info('新建发货功能')
}

function handleView(row: Shipping) {
  ElMessage.info(`查看发货: ${row.shippingNo}`)
}

function handleEdit(row: Shipping) {
  ElMessage.info(`编辑发货: ${row.shippingNo}`)
}

function handleDelete(row: Shipping) {
  ElMessage.info(`删除发货: ${row.shippingNo}`)
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
