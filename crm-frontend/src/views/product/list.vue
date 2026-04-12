<template>
  <div class="page-container">
    <!-- 搜索表单 -->
    <SearchForm
      v-model="queryParams"
      show-export
      @search="handleSearch"
      @reset="handleReset"
      @export="handleExport"
    >
      <template #default="{ form }">
        <el-form-item label="产品名称">
          <el-input v-model="form.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="form.productCode" placeholder="请输入产品编码" clearable />
        </el-form-item>
        <el-form-item label="产品分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable style="width: 150px">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.categoryName"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="上架" value="0" />
            <el-option label="下架" value="1" />
          </el-select>
        </el-form-item>
      </template>
    </SearchForm>

    <!-- 数据表格 -->
    <DataTable
      ref="dataTableRef"
      :data="tableData"
      :columns="columns"
      :operations="operations"
      :loading="loading"
      :total="total"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      show-selection
      @selection-change="handleSelectionChange"
      @pagination="handlePagination"
    >
      <template #title>
        <span>产品列表</span>
      </template>

      <template #actions>
        <el-button type="primary" icon="Plus" @click="handleAdd">新建产品</el-button>
        <el-button
          type="danger"
          icon="Delete"
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          批量删除 ({{ selectedRows.length }})
        </el-button>
      </template>

      <!-- 自定义列渲染 -->
      <template #price="{ row }">
        <span class="price-text">¥{{ row.price?.toFixed(2) }}</span>
      </template>

      <template #status="{ row }">
        <el-tag :type="row.status === '0' ? 'success' : 'info'">
          {{ row.status === '0' ? '上架' : '下架' }}
        </el-tag>
      </template>
    </DataTable>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="formData.productCode" placeholder="请输入产品编码">
            <template #append>
              <el-button @click="generateProductCode">生成</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="产品分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option label="电子产品" :value="1" />
            <el-option label="服装" :value="2" />
            <el-option label="食品" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input-number v-model="formData.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="成本价">
          <el-input-number v-model="formData.costPrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="formData.unit" placeholder="如：件、kg、m" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="formData.specification" type="textarea" :rows="2" placeholder="请输入规格描述" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  getProductList,
  createProduct,
  updateProduct,
  deleteProduct,
  generateProductNo,
  type Product,
  type ProductQuery,
  type ProductCreateDTO,
  type ProductUpdateDTO,
} from '@/api/product'
import { SearchForm, DataTable, type TableColumn, type TableOperation } from '@/components'
import { exportTableData } from '@/utils/export'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建产品')
const formRef = ref<FormInstance>()
const total = ref(0)
const selectedRows = ref<any[]>([])
const dataTableRef = ref()

const queryParams = reactive<ProductQuery>({
  pageNum: 1,
  pageSize: 10,
  productName: '',
  productCode: '',
  categoryId: undefined,
  status: undefined,
})

const formData = reactive<ProductCreateDTO & { id?: number }>({
  productName: '',
  productCode: '',
  categoryId: 0,
  price: 0,
  costPrice: 0,
  unit: '',
  specification: '',
  remark: '',
})

const rules: FormRules = {
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择产品分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入单价', trigger: 'blur' }],
}

const tableData = ref<Product[]>([])

// 模拟分类数据（实际应从API获取）
const categories = ref([
  { id: 1, categoryName: '电子产品' },
  { id: 2, categoryName: '服装' },
  { id: 3, categoryName: '食品' },
  { id: 4, categoryName: '家居用品' },
  { id: 5, categoryName: '办公用品' },
])

// 表格列配置
const columns: TableColumn[] = [
  { prop: 'id', label: 'ID', width: 80, align: 'center' },
  { prop: 'productName', label: '产品名称', minWidth: 150 },
  { prop: 'productCode', label: '产品编码', width: 120 },
  { prop: 'categoryName', label: '分类', width: 120 },
  { prop: 'price', label: '单价', width: 120, slot: 'price' },
  { prop: 'unit', label: '单位', width: 80 },
  { prop: 'status', label: '状态', width: 100, slot: 'status' },
]

// 操作按钮配置
const operations: TableOperation[] = [
  {
    key: 'view',
    label: '查看',
    type: 'primary',
    click: (row) => handleView(row),
  },
  {
    key: 'edit',
    label: '编辑',
    type: 'primary',
    click: (row) => handleEdit(row),
  },
  {
    key: 'delete',
    label: '删除',
    type: 'danger',
    click: (row) => handleDelete(row),
  },
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await getProductList(queryParams)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 生成产品编码
async function generateProductCode() {
  try {
    const code = await generateProductNo()
    formData.productCode = code
    ElMessage.success('产品编码已生成')
  } catch (error) {
    console.error('生成编码失败:', error)
    ElMessage.error('生成编码失败')
  }
}

// 搜索
function handleSearch() {
  queryParams.pageNum = 1
  loadData()
}

// 重置
function handleReset() {
  queryParams.productName = ''
  queryParams.productCode = ''
  queryParams.categoryId = undefined
  queryParams.status = undefined
  queryParams.pageNum = 1
  loadData()
}

// 选择变化
function handleSelectionChange(selection: any[]) {
  selectedRows.value = selection
}

// 分页
function handlePagination(params: { pageNum: number; pageSize: number }) {
  queryParams.pageNum = params.pageNum
  queryParams.pageSize = params.pageSize
  loadData()
}

// 导出
function handleExport() {
  if (tableData.value.length === 0) {
    ElMessage.warning('没有数据可导出')
    return
  }
  exportTableData(
    tableData.value,
    columns.filter((col) => !col.slot),
    `产品列表_${new Date().getTime()}.csv`
  )
  ElMessage.success('导出成功')
}

// 批量删除
async function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的产品')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个产品吗?`, '提示', {
      type: 'warning',
    })
    const ids = selectedRows.value.map((row) => row.id).filter(Boolean)
    if (ids.length > 0) {
      await deleteProduct(ids)
      ElMessage.success('批量删除成功')
      selectedRows.value = []
      dataTableRef.value?.clearSelection()
      loadData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 新建
function handleAdd() {
  dialogTitle.value = '新建产品'
  resetForm()
  dialogVisible.value = true
}

// 编辑
function handleEdit(row: Product) {
  dialogTitle.value = '编辑产品'
  Object.assign(formData, {
    id: row.id,
    productName: row.productName,
    productCode: row.productCode,
    categoryId: row.categoryId,
    price: row.price,
    costPrice: row.costPrice,
    unit: row.unit,
    specification: row.specification,
    remark: row.remark,
  })
  dialogVisible.value = true
}

// 查看
function handleView(row: Product) {
  ElMessage.info(`查看产品: ${row.productName}`)
}

// 删除
async function handleDelete(row: Product) {
  try {
    await ElMessageBox.confirm(`确定要删除产品 "${row.productName}" 吗?`, '提示', {
      type: 'warning',
    })
    if (row.id) {
      await deleteProduct([row.id])
      ElMessage.success('删除成功')
      loadData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (formData.id) {
      const updateData: ProductUpdateDTO = {
        id: formData.id,
        productName: formData.productName,
        price: formData.price,
        costPrice: formData.costPrice,
        unit: formData.unit,
        specification: formData.specification,
        remark: formData.remark,
      }
      await updateProduct(updateData)
    } else {
      await createProduct(formData)
    }
    
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
function resetForm() {
  Object.assign(formData, {
    id: undefined,
    productName: '',
    productCode: '',
    categoryId: 0,
    price: 0,
    costPrice: 0,
    unit: '',
    specification: '',
    remark: '',
  })
  formRef.value?.clearValidate()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.page-container {
  padding: 16px;
}

.price-text {
  color: #f56c6c;
  font-weight: 600;
}
</style>
