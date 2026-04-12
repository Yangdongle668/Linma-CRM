<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header><span>销售漏斗</span></template>
      <el-form :inline="true" class="search-form">
        <el-form-item label="时间范围">
          <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search">查询</el-button>
        </el-form-item>
      </el-form>
      <div style="height: 500px; display: flex; align-items: center; justify-content: center;">
        <el-empty description="销售漏斗图表（ECharts集成）" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const dateRange = ref([])
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
.search-form { margin-bottom: 20px; }
</style>
