<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header><span>应付账款</span></template>
      <el-form :inline="true" class="search-form">
        <el-form-item label="供应商">
          <el-input placeholder="请输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select placeholder="请选择状态" clearable>
            <el-option label="未付款" value="unpaid" />
            <el-option label="已付款" value="paid" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search">搜索</el-button>
          <el-button icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="payableNo" label="应付单号" width="150" />
        <el-table-column prop="supplierName" label="供应商" />
        <el-table-column prop="amount" label="应付金额" width="120">
          <template #default="{ row }">¥{{ row.amount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120">
          <template #default="{ row }">¥{{ row.paidAmount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'paid' ? 'success' : 'warning'">
              {{ row.status === 'paid' ? '已付款' : '未付款' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default>
            <el-button link type="primary">查看</el-button>
            <el-button link type="success">付款</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, sizes, prev, pager, next, jumper" :total="total" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const loading = ref(false)
const total = ref(0)
const tableData = ref([
  { id: 1, payableNo: 'PAY20260411001', supplierName: 'Supplier A', amount: 5000, paidAmount: 0, dueDate: '2026-05-01', status: 'unpaid' },
])
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
.search-form { margin-bottom: 20px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
