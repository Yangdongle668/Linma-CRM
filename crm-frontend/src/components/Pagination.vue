<template>
  <div v-if="total > 0" class="pagination-container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="pageSizes"
      :total="total"
      :layout="layout"
      :background="background"
      :small="small"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface PaginationProps {
  total: number
  currentPage?: number
  pageSize?: number
  pageSizes?: number[]
  layout?: string
  background?: boolean
  small?: boolean
}

interface PaginationEmits {
  (e: 'update:currentPage', value: number): void
  (e: 'update:pageSize', value: number): void
  (e: 'pagination', params: { pageNum: number; pageSize: number }): void
}

const props = withDefaults(defineProps<PaginationProps>(), {
  currentPage: 1,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper',
  background: true,
  small: false,
})

const emit = defineEmits<PaginationEmits>()

const currentPage = computed({
  get: () => props.currentPage,
  set: (value) => emit('update:currentPage', value),
})

const pageSize = computed({
  get: () => props.pageSize,
  set: (value) => emit('update:pageSize', value),
})

const handleSizeChange = (size: number) => {
  emit('update:pageSize', size)
  emit('pagination', { pageNum: 1, pageSize: size })
}

const handleCurrentChange = (page: number) => {
  emit('update:currentPage', page)
  emit('pagination', { pageNum: page, pageSize: props.pageSize })
}
</script>

<style scoped lang="scss">
.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding: 10px 0;
}
</style>
