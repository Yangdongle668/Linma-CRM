<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>通知中心</span>
          <el-button type="primary" icon="Check" @click="handleMarkAllRead">全部标记为已读</el-button>
        </div>
      </template>

      <!-- 筛选栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="通知类型">
          <el-select placeholder="请选择类型" clearable>
            <el-option label="系统通知" value="system" />
            <el-option label="订单通知" value="order" />
            <el-option label="客户通知" value="customer" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select placeholder="请选择状态" clearable>
            <el-option label="未读" value="unread" />
            <el-option label="已读" value="read" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search">搜索</el-button>
          <el-button icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 通知列表 -->
      <el-table :data="notificationList" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'unread' ? 'danger' : 'info'">
              {{ row.status === 'unread' ? '未读' : '已读' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === 'unread'"
              link
              type="success"
              @click="handleMarkAsRead(row)"
            >
              标记已读
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看通知对话框 -->
    <el-dialog v-model="dialogVisible" title="通知详情" width="600px">
      <div v-if="currentNotification">
        <h3>{{ currentNotification.title }}</h3>
        <div class="notification-meta">
          <span>类型：{{ getTypeLabel(currentNotification.type) }}</span>
          <span>时间：{{ currentNotification.createdTime }}</span>
        </div>
        <div class="notification-content">
          {{ currentNotification.content }}
        </div>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  getNotificationList,
  markNotificationAsRead,
  batchMarkAsRead,
  deleteNotification,
} from '@/api/message'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const dialogVisible = ref(false)
const currentNotification = ref<any>(null)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
})

const notificationList = ref([
  {
    id: 1,
    title: '新订单通知',
    type: 'order',
    content: '您有一个新的订单需要处理',
    status: 'unread',
    createdTime: '2026-04-11 10:30:00',
  },
  {
    id: 2,
    title: '系统维护通知',
    type: 'system',
    content: '系统将于今晚进行维护',
    status: 'read',
    createdTime: '2026-04-10 15:00:00',
  },
])

const getTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    system: '系统',
    order: '订单',
    customer: '客户',
  }
  return labels[type] || type
}

const getTypeTagType = (type: string) => {
  const types: Record<string, any> = {
    system: 'info',
    order: 'success',
    customer: 'warning',
  }
  return types[type] || ''
}

const handleView = (row: any) => {
  currentNotification.value = row
  dialogVisible.value = true
  if (row.status === 'unread') {
    handleMarkAsRead(row)
  }
}

const handleMarkAsRead = async (row: any) => {
  try {
    await markNotificationAsRead(row.id)
    row.status = 'read'
    ElMessage.success('已标记为已读')
  } catch (error) {
    console.error('Failed to mark as read:', error)
  }
}

const handleMarkAllRead = async () => {
  try {
    const unreadIds = notificationList.value
      .filter((item) => item.status === 'unread')
      .map((item) => item.id)
    if (unreadIds.length > 0) {
      await batchMarkAsRead(unreadIds)
      notificationList.value.forEach((item) => {
        if (item.status === 'unread') {
          item.status = 'read'
        }
      })
      ElMessage.success('已全部标记为已读')
    }
  } catch (error) {
    console.error('Failed to mark all as read:', error)
  }
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该通知吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await deleteNotification([row.id])
    ElMessage.success('删除成功')
    // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete notification:', error)
    }
  }
}

const handleSizeChange = (val: number) => {
  queryParams.value.pageSize = val
  // 重新加载数据
}

const handleCurrentChange = (val: number) => {
  queryParams.value.pageNum = val
  // 重新加载数据
}
</script>

<style scoped lang="scss">
.page-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.notification-meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 14px;
  margin: 10px 0;
}

.notification-content {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
