<template>
  <div class="dashboard">
    <!-- 欢迎卡片 -->
    <el-card class="welcome-card" shadow="hover">
      <div class="welcome-content">
        <div class="welcome-text">
          <h2>欢迎回来,管理员! 👋</h2>
          <p>今天是 {{ currentDate }},祝您工作顺利!</p>
        </div>
        <el-icon :size="60" color="#667eea"><Sunny /></el-icon>
      </div>
    </el-card>

    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :lg="6">
        <el-card class="stat-card stat-card-primary" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon :size="32"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ loading ? '...' : dashboardData.totalCustomers.toLocaleString() }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>实时更新</span>
            <el-icon><ArrowRight /></el-icon>
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
              <div class="stat-value">{{ loading ? '...' : dashboardData.pendingInquiries }}</div>
              <div class="stat-label">待处理询盘</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>实时更新</span>
            <el-icon><ArrowRight /></el-icon>
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
              <div class="stat-value">{{ loading ? '...' : dashboardData.activeContracts }}</div>
              <div class="stat-label">进行中合同</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>实时更新</span>
            <el-icon><ArrowRight /></el-icon>
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
              <div class="stat-value">{{ loading ? '...' : formatMoney(dashboardData.monthlyRevenue) }}</div>
              <div class="stat-label">本月销售额</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>实时更新</span>
            <el-icon><ArrowRight /></el-icon>
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

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
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
              <span>最新动态</span>
              <el-link type="primary">查看更多</el-link>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.time"
              placement="top"
            >
              <p>{{ activity.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <CustomerFunnelChart v-if="funnelData.length > 0" :data="funnelData" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="8" animated />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <ProductRankingChart v-if="productRankingData.length > 0" :data="productRankingData" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="8" animated />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <RegionDistributionChart v-if="regionData.length > 0" :data="regionData" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="8" animated />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户增长趋势</span>
            </div>
          </template>
          <SalesTrendChart v-if="customerTrendData.length > 0" :data="customerTrendData" title="客户增长" />
          <div v-else class="chart-loading">
            <el-skeleton :rows="8" animated />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import {
  getDashboardData,
  getSalesTrend,
  getCustomerTrend,
  getSalesFunnel,
  getProductRanking,
  getRegionAnalytics,
} from '@/api/analytics'
import { ElMessage } from 'element-plus'
import {
  SalesTrendChart,
  CustomerFunnelChart,
  ProductRankingChart,
  RegionDistributionChart,
} from '@/components/charts'

const router = useRouter()

// 当前日期
const currentDate = computed(() => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

// 销售趋势周期
const trendPeriod = ref('month')

// Dashboard data
const dashboardData = ref({
  totalCustomers: 0,
  pendingInquiries: 0,
  activeContracts: 0,
  monthlyRevenue: 0,
})

const loading = ref(false)

// 图表数据
const salesTrendData = ref<Array<{ date: string; value: number }>>([])
const customerTrendData = ref<Array<{ date: string; value: number }>>([])
const funnelData = ref<Array<{ stage: string; count: number }>>([])
const productRankingData = ref<Array<{ name: string; value: number }>>([])
const regionData = ref<Array<{ name: string; value: number }>>([])

// 快捷入口列表
const quickAccessList = [
  { name: '新建客户', icon: 'UserFilled', path: '/customer/list', color: '#667eea' },
  { name: '新建询盘', icon: 'Message', path: '/inquiry/list', color: '#10B981' },
  { name: '邮件管理', icon: 'Message', path: '/message/email', color: '#3B82F6' },
  { name: '新建合同', icon: 'Document', path: '/contract/list', color: '#F59E0B' },
  { name: '产品管理', icon: 'Goods', path: '/product/list', color: '#EF4444' },
  { name: '物流追踪', icon: 'Van', path: '/shipping/list', color: '#8B5CF6' },
  { name: '财务报表', icon: 'TrendCharts', path: '/finance/profit', color: '#EC4899' },
]

// 最新动态
const activities = ref<any[]>([])

/**
 * 加载仪表盘数据
 */
async function loadDashboardData() {
  loading.value = true
  try {
    // Axios interceptor returns res.data directly
    const data = await getDashboardData()
    console.log('Dashboard data:', data)
    
    if (data) {
      // Backend returns nested structure: data.keyMetrics, data.monthlyPerformance, etc.
      const keyMetrics = data.keyMetrics || {}
      const monthlyPerf = data.monthlyPerformance || {}
      
      dashboardData.value = {
        totalCustomers: keyMetrics.totalCustomers || 0,
        pendingInquiries: keyMetrics.pendingInquiries || 0,
        activeContracts: keyMetrics.ordersThisMonth || monthlyPerf.orderCount || 0,
        monthlyRevenue: monthlyPerf.totalSales || keyMetrics.salesThisMonth || 0,
      }
      
      console.log('Dashboard data loaded:', dashboardData.value)
      
      // Load chart data
      salesTrendData.value = (data.salesTrend || []).map((item: any) => ({
        date: item.date,
        value: item.sales || 0,
      }))
      
      productRankingData.value = (data.topProducts || []).map((item: any) => ({
        name: item.productName,
        value: item.totalSales || 0,
      }))
      
      regionData.value = (data.customerDistribution || []).map((item: any) => ({
        name: item.country,
        value: item.count,
      }))
    }
  } catch (error) {
    console.error('Failed to load dashboard data:', error)
    ElMessage.warning('加载仪表盘数据失败，显示默认数据')
  } finally {
    loading.value = false
  }
}

/**
 * 加载销售趋势数据
 */
async function loadSalesTrendData() {
  try {
    const params = { period: trendPeriod.value }
    const res = await getSalesTrend(params)
    if (res.code === 200 && res.data) {
      salesTrendData.value = res.data.map((item: any) => ({
        date: item.date || item.name,
        value: item.value || item.amount || 0,
      }))
    }
  } catch (error) {
    console.error('Failed to load sales trend data:', error)
    salesTrendData.value = generateMockSalesTrend()
  }
}

/**
 * 加载客户增长数据
 */
async function loadCustomerTrendData() {
  try {
    const res = await getCustomerTrend({ period: 'month' })
    if (res.code === 200 && res.data) {
      customerTrendData.value = res.data.map((item: any) => ({
        date: item.date || item.name,
        value: item.value || item.count || 0,
      }))
    }
  } catch (error) {
    console.error('Failed to load customer trend data:', error)
    customerTrendData.value = generateMockCustomerTrend()
  }
}

/**
 * 加载销售漏斗数据
 */
async function loadFunnelData() {
  try {
    const res = await getSalesFunnel()
    if (res.code === 200 && res.data) {
      funnelData.value = res.data.map((item: any) => ({
        stage: item.stage || item.name,
        count: item.count || item.value || 0,
      }))
    }
  } catch (error) {
    console.error('Failed to load funnel data:', error)
    funnelData.value = generateMockFunnelData()
  }
}

/**
 * 加载产品排行数据
 */
async function loadProductRankingData() {
  try {
    const res = await getProductRanking({ limit: 10 })
    if (res.code === 200 && res.data) {
      productRankingData.value = res.data.map((item: any) => ({
        name: item.name || item.productName,
        value: item.value || item.sales || 0,
      }))
    }
  } catch (error) {
    console.error('Failed to load product ranking data:', error)
    productRankingData.value = generateMockProductRanking()
  }
}

/**
 * 加载区域分布数据
 */
async function loadRegionData() {
  try {
    const res = await getRegionAnalytics()
    if (res.code === 200 && res.data) {
      regionData.value = res.data.map((item: any) => ({
        name: item.name || item.region,
        value: item.value || item.amount || 0,
      }))
    }
  } catch (error) {
    console.error('Failed to load region data:', error)
    regionData.value = generateMockRegionData()
  }
}

/**
 * 处理趋势周期切换
 */
function handleTrendPeriodChange() {
  loadSalesTrendData()
}

/**
 * 处理快捷入口点击
 */
function handleQuickAccess(path: string) {
  router.push(path)
}

/**
 * 格式化金额
 */
function formatMoney(value: number) {
  if (value >= 1000000) {
    return `¥${(value / 1000000).toFixed(1)}M`
  } else if (value >= 10000) {
    return `¥${(value / 10000).toFixed(1)}万`
  }
  return `¥${value.toLocaleString()}`
}

/**
 * 生成模拟销售趋势数据
 */
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

/**
 * 生成模拟客户增长数据
 */
function generateMockCustomerTrend() {
  const data = []
  for (let i = 0; i < 30; i++) {
    data.push({
      date: dayjs()
        .subtract(29 - i, 'day')
        .format('MM-DD'),
      value: Math.floor(Math.random() * 50) + 10,
    })
  }
  return data
}

/**
 * 生成模拟漏斗数据
 */
function generateMockFunnelData() {
  return [
    { stage: '潜在客户', count: 1200 },
    { stage: '初步接触', count: 800 },
    { stage: '需求确认', count: 500 },
    { stage: '方案报价', count: 350 },
    { stage: '谈判阶段', count: 200 },
    { stage: '成交客户', count: 120 },
  ]
}

/**
 * 生成模拟产品排行
 */
function generateMockProductRanking() {
  const products = ['产品A', '产品B', '产品C', '产品D', '产品E', '产品F', '产品G', '产品H']
  return products.map((name) => ({
    name,
    value: Math.floor(Math.random() * 1000) + 100,
  }))
}

/**
 * 生成模拟区域数据
 */
function generateMockRegionData() {
  return [
    { name: '华东地区', value: 850000 },
    { name: '华南地区', value: 720000 },
    { name: '华北地区', value: 650000 },
    { name: '西南地区', value: 480000 },
    { name: '华中地区', value: 420000 },
    { name: '东北地区', value: 350000 },
    { name: '西北地区', value: 280000 },
  ]
}

/**
 * 加载所有图表数据
 */
async function loadAllChartData() {
  await Promise.all([
    loadSalesTrendData(),
    loadCustomerTrendData(),
    loadFunnelData(),
    loadProductRankingData(),
    loadRegionData(),
  ])
}

onMounted(() => {
  loadDashboardData()
  loadAllChartData()
})
</script>

<style scoped lang="scss">
.dashboard {
  padding: 0;
}

// 欢迎卡片
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
        margin: 0;
      }
    }
  }
}

// 统计卡片行
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
    margin-bottom: 16px;
    
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
        margin-bottom: 8px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #6B7280;
      }
    }
  }
  
  .stat-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid #F3F4F6;
    font-size: 13px;
    color: #9CA3AF;
  }
  
  // 不同颜色的卡片
  &.stat-card-primary {
    .stat-icon {
      background: rgba(102, 126, 234, 0.1);
      color: #667eea;
    }
  }
  
  &.stat-card-success {
    .stat-icon {
      background: rgba(16, 185, 129, 0.1);
      color: #10B981;
    }
  }
  
  &.stat-card-warning {
    .stat-icon {
      background: rgba(245, 158, 11, 0.1);
      color: #F59E0B;
    }
  }
  
  &.stat-card-danger {
    .stat-icon {
      background: rgba(239, 68, 68, 0.1);
      color: #EF4444;
    }
  }
}

// 快捷入口
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

// 图表区域
.chart-row {
  margin-bottom: 20px;
  
  .chart-card {
    min-height: 400px;
    
    .chart-loading {
      padding: 20px;
    }
  }
  
  .chart-placeholder {
    min-height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

// 卡片头部
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

// 时间线
:deep(.el-timeline-item__content) {
  p {
    margin: 0;
    font-size: 14px;
    color: #4B5563;
  }
}
</style>
