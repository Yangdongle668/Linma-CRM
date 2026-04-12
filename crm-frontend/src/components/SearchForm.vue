<template>
  <el-card shadow="never" class="search-form-card">
    <el-form
      :model="modelValue"
      :inline="true"
      class="search-form"
      @submit.prevent
    >
      <slot :form="modelValue" />

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleSearch">
          {{ searchLabel || '搜索' }}
        </el-button>
        <el-button icon="Refresh" @click="handleReset">
          {{ resetLabel || '重置' }}
        </el-button>
        <el-button
          v-if="showExport"
          icon="Download"
          @click="handleExport"
        >
          {{ exportLabel || '导出' }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
interface SearchFormProps {
  modelValue: Record<string, any>
  searchLabel?: string
  resetLabel?: string
  showExport?: boolean
  exportLabel?: string
}

interface SearchFormEmits {
  (e: 'update:modelValue', value: Record<string, any>): void
  (e: 'search'): void
  (e: 'reset'): void
  (e: 'export'): void
}

const props = withDefaults(defineProps<SearchFormProps>(), {
  searchLabel: '搜索',
  resetLabel: '重置',
  showExport: false,
  exportLabel: '导出',
})

const emit = defineEmits<SearchFormEmits>()

const handleSearch = () => {
  emit('search')
}

const handleReset = () => {
  emit('reset')
}

const handleExport = () => {
  emit('export')
}
</script>

<style scoped lang="scss">
.search-form-card {
  margin-bottom: 16px;

  :deep(.el-card__body) {
    padding: 16px 20px;
  }
}

.search-form {
  margin: 0;

  :deep(.el-form-item) {
    margin-bottom: 0;
  }
}
</style>
