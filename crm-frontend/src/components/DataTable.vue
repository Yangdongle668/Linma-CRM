<template>
  <el-card shadow="never" class="data-table-card">
    <!-- 表格头部操作区 -->
    <div v-if="$slots.header || title" class="table-header">
      <div class="table-title">
        <slot name="title">
          <span class="title-text">{{ title }}</span>
        </slot>
      </div>
      <div class="table-actions">
        <slot name="actions" />
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="data"
      :border="border"
      :stripe="stripe"
      :height="height"
      :max-height="maxHeight"
      :highlight-current-row="highlightCurrentRow"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      @row-click="handleRowClick"
    >
      <!-- 选择列 -->
      <el-table-column
        v-if="showSelection"
        type="selection"
        width="55"
        fixed="left"
        align="center"
      />

      <!-- 序号列 -->
      <el-table-column
        v-if="showIndex"
        type="index"
        label="序号"
        width="60"
        fixed="left"
        align="center"
      />

      <!-- 动态列 -->
      <el-table-column
        v-for="col in columns"
        :key="col.prop"
        :prop="col.prop"
        :label="col.label"
        :width="col.width"
        :min-width="col.minWidth"
        :fixed="col.fixed"
        :align="col.align || 'left'"
        :sortable="col.sortable"
        :show-overflow-tooltip="col.showOverflowTooltip !== false"
      >
        <template #default="scope">
          <!-- 自定义渲染 -->
          <slot
            v-if="col.slot"
            :name="col.slot"
            :row="scope.row"
            :column="col"
            :$index="scope.$index"
          />
          <!-- 默认渲染 -->
          <template v-else>
            {{ col.formatter ? col.formatter(scope.row, col) : scope.row[col.prop] }}
          </template>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column
        v-if="$slots.operations || operations?.length"
        :label="operationsLabel"
        :width="operationsWidth"
        fixed="right"
        align="center"
      >
        <template #default="scope">
          <slot name="operations" :row="scope.row" :$index="scope.$index">
            <template v-for="op in operations" :key="op.key">
              <el-button
                v-if="!op.hidden || !op.hidden(scope.row)"
                link
                :type="op.type || 'primary'"
                :disabled="op.disabled || (op.disabledFn && op.disabledFn(scope.row))"
                @click.stop="op.click?.(scope.row)"
              >
                {{ op.label }}
              </el-button>
            </template>
          </slot>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty
      v-if="!loading && data.length === 0"
      :description="emptyText"
      :image-size="120"
    />

    <!-- 分页 -->
    <Pagination
      v-if="showPagination"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="pageSizes"
      @pagination="handlePagination"
    />
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Pagination from './Pagination.vue'
import type { ElTable } from 'element-plus'

// 列定义
export interface TableColumn {
  prop: string
  label: string
  width?: number | string
  minWidth?: number | string
  fixed?: boolean | 'left' | 'right'
  align?: 'left' | 'center' | 'right'
  sortable?: boolean | 'custom'
  formatter?: (row: any, col: TableColumn) => string
  slot?: string
  showOverflowTooltip?: boolean
}

// 操作按钮定义
export interface TableOperation {
  key: string
  label: string
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info'
  disabled?: boolean
  disabledFn?: (row: any) => boolean
  hidden?: (row: any) => boolean
  click?: (row: any) => void
}

interface DataTableProps {
  data: any[]
  columns: TableColumn[]
  operations?: TableOperation[]
  operationsLabel?: string
  operationsWidth?: number | string
  loading?: boolean
  border?: boolean
  stripe?: boolean
  height?: number | string
  maxHeight?: number | string
  highlightCurrentRow?: boolean
  showSelection?: boolean
  showIndex?: boolean
  emptyText?: string
  title?: string
  showPagination?: boolean
  currentPage?: number
  pageSize?: number
  total?: number
  pageSizes?: number[]
}

interface DataTableEmits {
  (e: 'selection-change', selection: any[]): void
  (e: 'sort-change', sortInfo: { prop: string; order: string }): void
  (e: 'row-click', row: any): void
  (e: 'pagination', params: { pageNum: number; pageSize: number }): void
  (e: 'update:currentPage', value: number): void
  (e: 'update:pageSize', value: number): void
}

const props = withDefaults(defineProps<DataTableProps>(), {
  operationsLabel: '操作',
  operationsWidth: 200,
  loading: false,
  border: true,
  stripe: true,
  highlightCurrentRow: false,
  showSelection: false,
  showIndex: false,
  emptyText: '暂无数据',
  showPagination: true,
  currentPage: 1,
  pageSize: 10,
  total: 0,
  pageSizes: () => [10, 20, 50, 100],
})

const emit = defineEmits<DataTableEmits>()

const tableRef = ref<InstanceType<typeof ElTable>>()

const currentPage = ref(props.currentPage)
const pageSize = ref(props.pageSize)

const handleSelectionChange = (selection: any[]) => {
  emit('selection-change', selection)
}

const handleSortChange = ({ prop, order }: { prop: string; order: string }) => {
  emit('sort-change', { prop, order })
}

const handleRowClick = (row: any) => {
  emit('row-click', row)
}

const handlePagination = (params: { pageNum: number; pageSize: number }) => {
  emit('pagination', params)
}

// 暴露方法供外部调用
const clearSelection = () => {
  tableRef.value?.clearSelection()
}

const toggleRowSelection = (row: any, selected?: boolean) => {
  tableRef.value?.toggleRowSelection(row, selected)
}

defineExpose({
  clearSelection,
  toggleRowSelection,
})
</script>

<style scoped lang="scss">
.data-table-card {
  :deep(.el-card__body) {
    padding: 0;
  }
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);

  .table-title {
    .title-text {
      font-size: 16px;
      font-weight: 600;
      color: var(--el-text-color-primary);
    }
  }

  .table-actions {
    display: flex;
    gap: 8px;
  }
}

:deep(.el-table) {
  .el-table__header-wrapper {
    th {
      background-color: var(--el-fill-color-light);
      color: var(--el-text-color-primary);
      font-weight: 600;
    }
  }

  .el-button + .el-button {
    margin-left: 8px;
  }
}
</style>
