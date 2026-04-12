<template>
  <div class="dashboard-enhanced">
    <!-- 欢迎卡片 -->
    <el-card class="welcome-card" shadow="hover">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎回来, {{ userInfo.realName }}! 👋</h2>
          <p>今天是 {{ currentDate }},祝您工作顺利!</p>
          <div class="quick-stats">
            <el-tag size="small" type="success">今日待跟进: {{ todayFollowUps }}</el-tag>
            <el-tag size="small" type="warning">未读邮件: {{ unreadEmails }}</el-tag>
            <el-tag size="small" type="info">待处理询盘: {{ pendingInquiries }}</el-tag>
          </div>
        </div>
        <el-icon :size="60" color="#667eea"><Sunny /></el-icon>
      </div>
    </el-card>

    <!-- 核心数据统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card stat-card-primary" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="32"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ loading ? '...' : stats.totalCustomers.toLocaleString() }}</div>
              <div class="stat-label">客户总数</div>
              <div class="stat-trend">
                <el-icon color="#67c23a"><Top /></el-icon>
                <span>+{{ stats.newCustomersThisMonth }} 本月新增</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card stat-card-success" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="32"><Message /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ loading ? '...' : stats.emailCount }}</div>
              <div class="stat-label">邮件往来</div>
              <div class="stat-trend">
                <el-icon color="#409eff"><ChatDotRound /></el-icon>
                <span>{{ stats.emailAccounts }} 个账户</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card stat-card-warning" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="32"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ loading ? '...' : stats.activeOpportunities }}</div>
              <div class="stat-label">活跃商机</div>
              <div class="stat-trend">
                <span>价值: {{ formatMoney(stats.totalOpportunityValue) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card stat-card-danger" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="32"><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ loading ? '...' : formatMoney(stats.monthlyRevenue) }}</div>
              <div class="stat-label">本月销售额</div>
              <div class="stat-trend">
                <el-icon v-if="stats.revenueGrowth > 0" color="#67c23a"><Top /></el-icon>
                <el-icon v-else color="#f56c6c"><Bottom /></el-icon>
                <span>{{ stats.revenueGrowth }}% 环比</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <el-card class="quick-access-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>快捷入口</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="4" v-for="item in quickAccessList" :key="item.name">
          <div class="quick-item" @click="handleQuickAccess(item.path)">
            <el-icon :size="32" :color="item.color">
              <component :is="item.icon" />
            </el-icon>
            <span>{{ item.name }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 第一行：销售趋势 + 客户分布 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
              <el-radio-group v-model="trendPeriod" size="small" @change="loadSalesTrendData">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <SalesTrendChart v-if="salesTrendData.length > 0" :data="salesTrendData" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="8" animated />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户等级分布</span>
            </div>
          </template>
          <CustomerDistributionChart v-if="customerDistribution.length > 0" :data="customerDistribution" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="6" animated />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：邮件统计 + 跟进提醒 + 优先级客户 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 邮件活动统计 -->
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>邮件活动</span>
              <el-link type="primary" @click="goToEmailPage">查看更多</el-link>
            </div>
          </template>
          <div class="email-stats-widget">
            <div class="email-stat-item">
              <div class="email-stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
                <el-icon :size="24"><Message /></el-icon>
              </div>
              <div class="email-stat-info">
                <div class="value">{{ emailStats.sent }}</div>
                <div class="label">已发送</div>
              </div>
            </div>
            <div class="email-stat-item">
              <div class="email-stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
                <el-icon :size="24"><Received /></el-icon>
              </div>
              <div class="email-stat-info">
                <div class="value">{{ emailStats.received }}</div>
                <div class="label">已接收</div>
              </div>
            </div>
            <div class="email-stat-item">
              <div class="email-stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
                <el-icon :size="24"><Clock /></el-icon>
              </div>
              <div class="email-stat-info">
                <div class="value">{{ emailStats.pending }}</div>
                <div class="label">待回复</div>
              </div>
            </div>
            <div class="email-stat-item">
              <div class="email-stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)">
                <el-icon :size="24"><CircleCheck /></el-icon>
              </div>
              <div class="email-stat-info">
                <div class="value">{{ emailStats.responseRate }}%</div>
                <div class="label">回复率</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 跟进提醒 -->
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>跟进提醒</span>
              <el-badge :value="followUpReminders.length" type="danger">
                <el-icon><Bell /></el-icon>
              </el-badge>
            </div>
          </template>
          <el-timeline class="followup-timeline">
            <el-timeline-item
              v-for="reminder in followUpReminders.slice(0, 5)"
              :key="reminder.id"
              :timestamp="formatDate(reminder.nextFollowDate)"
              :type="getUrgencyType(reminder.urgency)"
            >
              <div class="reminder-item">
                <div class="reminder-header">
                  <strong>{{ reminder.customerName }}</strong>
                  <el-tag size="small" :type="getFollowUpTypeTag(reminder.followType)">
                    {{ getFollowUpTypeLabel(reminder.followType) }}
                  </el-tag>
                </div>
                <div class="reminder-subject">{{ reminder.subject }}</div>
                <div class="reminder-actions">
                  <el-button size="small" type="primary" link @click="completeFollowUp(reminder)">
                    完成
                  </el-button>
                  <el-button size="small" link @click="viewCustomer(reminder.customerId)">
                    查看客户
                  </el-button>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-if="followUpReminders.length === 0" description="暂无待跟进事项" :image-size="80" />
        </el-card>
      </el-col>

      <!-- 高优先级客户 -->
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>高优先级客户</span>
              <el-link type="primary" @click="goToCustomersPage">查看全部</el-link>
            </div>
          </template>
          <div class="priority-customers">
            <div
              v-for="customer in highPriorityCustomers"
              :key="customer.id"
              class="customer-item"
              @click="viewCustomer(customer.id)"
            >
              <el-avatar :size="40" class="customer-avatar">
                {{ customer.companyName?.charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="customer-info">
                <div class="customer-name">{{ customer.companyName }}</div>
                <div class="customer-meta">
                  <el-tag size="small" :type="getPriorityType(customer.priority)">
                    {{ getPriorityLabel(customer.priority) }}
                  </el-tag>
                  <span class="customer-value">{{ formatMoney(customer.opportunityValue) }}</span>
                </div>
              </div>
              <el-icon class="customer-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
          <el-empty v-if="highPriorityCustomers.length === 0" description="暂无高优先级客户" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：客户生命周期 + 行业分布 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户生命周期分布</span>
            </div>
          </template>
          <CustomerLifecycleChart v-if="lifecycleData.length > 0" :data="lifecycleData" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="6" animated />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>行业分布TOP10</span>
            </div>
          </template>
          <IndustryDistributionChart v-if="industryData.length > 0" :data="industryData" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="6" animated />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行：最新动态时间线 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>最新动态</span>
              <el-radio-group v-model="activityFilter" size="small">
                <el-radio-button label="all">全部</el-radio-button>
                <el-radio-button label="email">邮件</el-radio-button>
                <el-radio-button label="followup">跟进</el-radio-button>
                <el-radio-button label="order">订单</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <el-timeline class="activity-timeline">
            <el-timeline-item
              v-for="activity in recentActivities"
              :key="activity.id"
              :timestamp="formatDateTime(activity.createdTime)"
              placement="top"
              :type="getActivityType(activity.type)"
            >
              <el-card shadow="hover" class="activity-card">
                <div class="activity-header">
                  <el-icon :size="20" :color="getActivityColor(activity.type)">
                    <component :is="getActivityIcon(activity.type)" />
                  </el-icon>
                  <strong>{{ activity.title }}</strong>
                  <span class="activity-customer">{{ activity.customerName }}</span>
                </div>
                <div class="activity-content">{{ activity.content }}</div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-if="recentActivities.length === 0" description="暂无动态" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'
import {
  SalesTrendChart,
  CustomerDistributionChart,
  CustomerLifecycleChart,
  IndustryDistributionChart,
} from '@/components/charts'

const router = useRouter()

// User info (从store或localStorage获取)
const userInfo = ref({
  realName: '管理员',
})

// Current date
const currentDate = computed(() => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

// Loading state
const loading = ref(false)
const trendPeriod = ref('month')
const activityFilter = ref('all')

// Quick stats
const todayFollowUps = ref(5)
const unreadEmails = ref(12)
const pendingInquiries = ref(8)

// Dashboard stats
const stats = reactive({
  totalCustomers: 1247,
  newCustomersThisMonth: 45,
  emailCount: 3856,
  emailAccounts: 3,
  activeOpportunities: 62,
  totalOpportunityValue: 2850000,
  monthlyRevenue: 620000,
  revenueGrowth: 15.8,
})

// Email statistics
const emailStats = reactive({
  sent: 1256,
  received: 2600,
  pending: 23,
  responseRate: 87.5,
})

// Follow-up reminders
const followUpReminders = ref([
  {
    id: 1,
    customerId: 101,
    customerName: 'ABC Company Ltd.',
    subject: '产品询价跟进',
    followType: 'email',
    nextFollowDate: '2026-04-13 10:00:00',
    urgency: 'high',
  },
  {
    id: 2,
    customerId: 102,
    customerName: 'XYZ Trading GmbH',
    subject: '合同谈判',
    followType: 'call',
    nextFollowDate: '2026-04-13 14:00:00',
    urgency: 'medium',
  },
  {
    id: 3,
    customerId: 103,
    customerName: 'Global Import Inc.',
    subject: '样品确认',
    followType: 'meeting',
    nextFollowDate: '2026-04-14 09:00:00',
    urgency: 'low',
  },
])

// High priority customers
const highPriorityCustomers = ref([
  {
    id: 101,
    companyName: 'ABC Company Ltd.',
    priority: 'high',
    opportunityValue: 580000,
  },
  {
    id: 102,
    companyName: 'XYZ Trading GmbH',
    priority: 'high',
    opportunityValue: 420000,
  },
  {
    id: 103,
    companyName: 'Global Import Inc.',
    priority: 'medium',
    opportunityValue: 350000,
  },
  {
    id: 104,
    companyName: 'Tech Solutions LLC',
    priority: 'high',
    opportunityValue: 680000,
  },
])

// Chart data
const salesTrendData = ref<Array<{ date: string; value: number }>>([])
const customerDistribution = ref<Array<{ name: string; value: number }>>([])
const lifecycleData = ref<Array<{ name: string; value: number }>>([])
const industryData = ref<Array<{ name: string; value: number }>>([])
const recentActivities = ref<any[]>([])

// Quick access list
const quickAccessList = [
  { name: '新建客户', icon: 'UserFilled', path: '/customer/list?mode=new', color: '#667eea' },
  { name: '发送邮件', icon: 'Message', path: '/message/email-client', color: '#10B981' },
  { name: '新建询盘', icon: 'DocumentAdd', path: '/inquiry/list', color: '#F59E0B' },
  { name: '产品管理', icon: 'Goods', path: '/product/list', color: '#EF4444' },
  { name: '物流追踪', icon: 'Van', path: '/shipping/list', color: '#8B5CF6' },
  { name: '财务报表', icon: 'TrendCharts', path: '/finance/profit', color: '#EC4899' },
]

// Methods
function handleQuickAccess(path: string) {
  router.push(path)
}

function goToEmailPage() {
  router.push('/message/email-client')
}

function goToCustomersPage() {
  router.push('/customer/list?priority=high')
}

function viewCustomer(customerId: number) {
  router.push(`/customer/detail/${customerId}`)
}

function completeFollowUp(reminder: any) {
  ElMessage.success('跟进已完成')
  // TODO: Call API to mark as completed
  const index = followUpReminders.value.findIndex(r => r.id === reminder.id)
  if (index > -1) {
    followUpReminders.value.splice(index, 1)
  }
}

function loadSalesTrendData() {
  // TODO: Load from API
  salesTrendData.value = generateMockSalesTrend()
}

function loadCustomerDistribution() {
  // TODO: Load from API
  customerDistribution.value = [
    { name: 'A级客户', value: 125 },
    { name: 'B级客户', value: 342 },
    { name: 'C级客户', value: 568 },
    { name: 'D级客户', value: 212 },
  ]
}

function loadLifecycleData() {
  // TODO: Load from API
  lifecycleData.value = [
    { name: '潜在客户', value: 450 },
    { name: '销售线索', value: 320 },
    { name: '商机', value: 180 },
    { name: '正式客户', value: 250 },
    { name: '流失客户', value: 47 },
  ]
}

function loadIndustryData() {
  // TODO: Load from API
  industryData.value = [
    { name: '电子产品', value: 285 },
    { name: '机械制造', value: 198 },
    { name: '纺织服装', value: 156 },
    { name: '化工材料', value: 142 },
    { name: '食品饮料', value: 98 },
    { name: '医疗器械', value: 87 },
    { name: '汽车配件', value: 76 },
    { name: '建筑材料', value: 65 },
    { name: '家居用品', value: 54 },
    { name: '其他', value: 86 },
  ]
}

function loadRecentActivities() {
  // TODO: Load from API
  recentActivities.value = [
    {
      id: 1,
      type: 'email',
      title: '发送邮件',
      content: '发送产品报价单给 ABC Company',
      customerName: 'ABC Company Ltd.',
      createdTime: '2026-04-12 10:30:00',
    },
    {
      id: 2,
      type: 'followup',
      title: '电话跟进',
      content: '与客户确认订单细节',
      customerName: 'XYZ Trading GmbH',
      createdTime: '2026-04-12 09:15:00',
    },
    {
      id: 3,
      type: 'order',
      title: '新订单',
      content: '收到订单 #ORD20260412001，金额 $45,000',
      customerName: 'Global Import Inc.',
      createdTime: '2026-04-11 16:45:00',
    },
  ]
}

// Helper functions
function formatMoney(value: number) {
  if (value >= 1000000) {
    return `$${(value / 1000000).toFixed(1)}M`
  } else if (value >= 10000) {
    return `$${(value / 10000).toFixed(1)}万`
  }
  return `$${value.toLocaleString()}`
}

function formatDate(date: string) {
  return dayjs(date).format('MM-DD HH:mm')
}

function formatDateTime(date: string) {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

function getUrgencyType(urgency: string) {
  const types: Record<string, any> = { high: 'danger', medium: 'warning', low: 'info' }
  return types[urgency] || ''
}

function getFollowUpTypeLabel(type: string) {
  const labels: Record<string, string> = {
    email: '邮件',
    call: '电话',
    meeting: '会议',
    visit: '拜访',
  }
  return labels[type] || type
}

function getFollowUpTypeTag(type: string) {
  const tags: Record<string, any> = {
    email: 'primary',
    call: 'success',
    meeting: 'warning',
    visit: 'danger',
  }
  return tags[type] || ''
}

function getPriorityType(priority: string) {
  const types: Record<string, any> = { high: 'danger', medium: 'warning', low: 'info' }
  return types[priority] || ''
}

function getPriorityLabel(priority: string) {
  const labels: Record<string, string> = { high: '高', medium: '中', low: '低' }
  return labels[priority] || priority
}

function getActivityType(type: string) {
  const types: Record<string, any> = {
    email: 'primary',
    followup: 'success',
    order: 'warning',
    inquiry: 'info',
  }
  return types[type] || ''
}

function getActivityColor(type: string) {
  const colors: Record<string, string> = {
    email: '#409eff',
    followup: '#67c23a',
    order: '#e6a23c',
    inquiry: '#909399',
  }
  return colors[type] || '#909399'
}

function getActivityIcon(type: string) {
  const icons: Record<string, string> = {
    email: 'Message',
    followup: 'Phone',
    order: 'ShoppingCart',
    inquiry: 'Document',
  }
  return icons[type] || 'Document'
}

function generateMockSalesTrend() {
  const data = []
  const days = trendPeriod.value === 'week' ? 7 : trendPeriod.value === 'month' ? 30 : 12
  for (let i = 0; i < days; i++) {
    const date =
      trendPeriod.value === 'year'
        ? `${i + 1}月`
        : dayjs()
            .subtract(days - i - 1, 'day')
            .format('MM-DD')
    data.push({
      date,
      value: Math.floor(Math.random() * 500000) + 100000,
    })
  }
  return data
}

onMounted(() => {
  loading.value = true
  setTimeout(() => {
    loadSalesTrendData()
    loadCustomerDistribution()
    loadLifecycleData()
    loadIndustryData()
    loadRecentActivities()
    loading.value = false
  }, 500)
})
</script>

<style scoped lang="scss">
.dashboard-enhanced {
  padding: 0;
}

// Welcome card
.welcome-card {
  margin-bottom: 20px;

  .welcome-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .welcome-text {
      h2 {
        font-size: 24px;
        color: #1F2937;
        margin: 0 0 8px;
      }

      p {
        font-size: 14px;
        color: #6B7280;
        margin: 0 0 12px;
      }

      .quick-stats {
        display: flex;
        gap: 8px;
      }
    }
  }
}

// Stats row
.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-4px);
  }

  .stat-content {
    display: flex;
    align-items: center;
    gap: 16px;

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .stat-info {
      flex: 1;

      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #1F2937;
        line-height: 1;
        margin-bottom: 6px;
      }

      .stat-label {
        font-size: 14px;
        color: #6B7280;
        margin-bottom: 6px;
      }

      .stat-trend {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: #9CA3AF;
      }
    }
  }

  &.stat-card-primary .stat-icon {
    background: rgba(102, 126, 234, 0.1);
    color: #667eea;
  }

  &.stat-card-success .stat-icon {
    background: rgba(16, 185, 129, 0.1);
    color: #10B981;
  }

  &.stat-card-warning .stat-icon {
    background: rgba(245, 158, 11, 0.1);
    color: #F59E0B;
  }

  &.stat-card-danger .stat-icon {
    background: rgba(239, 68, 68, 0.1);
    color: #EF4444;
  }
}

// Quick access
.quick-access-card {
  margin-bottom: 20px;

  .quick-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    padding: 20px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      background: #F3F4F6;
      transform: translateY(-2px);
    }

    span {
      font-size: 13px;
      color: #4B5563;
    }
  }
}

// Chart rows
.chart-row {
  margin-bottom: 20px;

  .chart-card {
    min-height: 400px;

    .chart-loading {
      padding: 20px;
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    font-size: 16px;
    font-weight: 500;
    color: #1F2937;
  }
}

// Email stats widget
.email-stats-widget {
  padding: 16px 0;

  .email-stat-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    margin-bottom: 8px;
    border-radius: 8px;
    background: #F9FAFB;
    transition: all 0.3s;

    &:hover {
      background: #F3F4F6;
    }

    .email-stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
    }

    .email-stat-info {
      flex: 1;

      .value {
        font-size: 20px;
        font-weight: 600;
        color: #1F2937;
      }

      .label {
        font-size: 12px;
        color: #9CA3AF;
      }
    }
  }
}

// Follow-up timeline
.followup-timeline {
  padding: 16px 0;

  .reminder-item {
    .reminder-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 4px;

      strong {
        font-size: 14px;
        color: #1F2937;
      }
    }

    .reminder-subject {
      font-size: 13px;
      color: #6B7280;
      margin-bottom: 8px;
    }

    .reminder-actions {
      display: flex;
      gap: 8px;
    }
  }
}

// Priority customers
.priority-customers {
  padding: 8px 0;

  .customer-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      background: #F3F4F6;
    }

    .customer-avatar {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      font-size: 16px;
      font-weight: 600;
    }

    .customer-info {
      flex: 1;
      min-width: 0;

      .customer-name {
        font-size: 14px;
        font-weight: 500;
        color: #1F2937;
        margin-bottom: 4px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .customer-meta {
        display: flex;
        align-items: center;
        gap: 8px;

        .customer-value {
          font-size: 12px;
          color: #67c23a;
          font-weight: 600;
        }
      }
    }

    .customer-arrow {
      color: #9CA3AF;
    }
  }
}

// Activity timeline
.activity-timeline {
  padding: 16px 0;

  .activity-card {
    .activity-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;

      strong {
        font-size: 14px;
        color: #1F2937;
      }

      .activity-customer {
        margin-left: auto;
        font-size: 12px;
        color: #9CA3AF;
      }
    }

    .activity-content {
      font-size: 13px;
      color: #6B7280;
      line-height: 1.6;
    }
  }
}
</style>
