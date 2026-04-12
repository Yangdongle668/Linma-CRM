<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header><span>物流追踪</span></template>
      <el-form :inline="true" class="search-form">
        <el-form-item label="追踪号">
          <el-input v-model="trackingNo" placeholder="请输入追踪号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search">查询</el-button>
        </el-form-item>
      </el-form>
      <el-timeline v-if="trackingRecords.length > 0">
        <el-timeline-item v-for="(record, index) in trackingRecords" :key="index" :timestamp="record.time" placement="top">
          <el-card>
            <p>{{ record.location }}</p>
            <p>{{ record.description }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="请输入追踪号查询物流信息" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const trackingNo = ref('')
const trackingRecords = ref([])
</script>

<style scoped lang="scss">
.page-container { padding: 0; }
.search-form { margin-bottom: 20px; }
</style>
