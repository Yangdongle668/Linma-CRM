<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header><span>业绩排行</span></template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="销售员排行" name="salesman">
          <el-table :data="salesmanRanking" border stripe>
            <el-table-column prop="rank" label="排名" width="80" />
            <el-table-column prop="name" label="销售员" />
            <el-table-column prop="customerCount" label="客户数" width="100" />
            <el-table-column prop="orderCount" label="订单数" width="100" />
            <el-table-column prop="amount" label="销售额" width="150">
              <template #default="{ row }">¥{{ row.amount?.toFixed(2) }}</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="产品排行" name="product">
          <el-table :data="productRanking" border stripe>
            <el-table-column prop="rank" label="排名" width="80" />
            <el-table-column prop="productName" label="产品名称" />
            <el-table-column prop="salesCount" label="销量" width="100" />
            <el-table-column prop="amount" label="销售额" width="150">
              <template #default="{ row }">¥{{ row.amount?.toFixed(2) }}</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const activeTab = ref('salesman')
const salesmanRanking = ref([
  { rank: 1, name: '张三', customerCount: 50, orderCount: 30, amount: 150000 },
  { rank: 2, name: '李四', customerCount: 40, orderCount: 25, amount: 120000 },
])
const productRanking = ref([
  { rank: 1, productName: '产品A', salesCount: 100, amount: 50000 },
  { rank: 2, productName: '产品B', salesCount: 80, amount: 40000 },
])
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
</style>
