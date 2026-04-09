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
              <div class="stat-value">1,234</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>较上月 +12%</span>
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
              <div class="stat-value">56</div>
              <div class="stat-label">待处理询盘</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>今日新增 8 条</span>
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
              <div class="stat-value">89</div>
              <div class="stat-label">进行中合同</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>本月签约 15 单</span>
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
              <div class="stat-value">¥2.5M</div>
              <div class="stat-label">本月销售额</div>
            </div>
          </div>
          <div class="stat-footer">
            <span>较上月 +23%</span>
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
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
              <el-radio-group v-model="trendPeriod" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-placeholder">
            <el-empty description="图表数据加载中..." />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card shadow="hover">
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'

const router = useRouter()

// 当前日期
const currentDate = computed(() => {
  return dayjs().format('YYYY年MM月DD日 dddd')
})

// 销售趋势周期
const trendPeriod = ref('month')

// 快捷入口列表
const quickAccessList = [
  { name: '新建客户', icon: 'UserFilled', path: '/customer/add', color: '#667eea' },
  { name: '新建询盘', icon: 'Message', path: '/inquiry/add', color: '#10B981' },
  { name: '新建合同', icon: 'Document', path: '/contract/add', color: '#F59E0B' },
  { name: '产品管理', icon: 'Goods', path: '/product/list', color: '#EF4444' },
  { name: '物流追踪', icon: 'Van', path: '/shipping/tracking', color: '#8B5CF6' },
  { name: '财务报表', icon: 'TrendCharts', path: '/finance/profit', color: '#EC4899' },
]

// 最新动态
const activities = [
  { content: '新客户 "ABC Trading" 已创建', time: '10分钟前' },
  { content: '询盘 #2024001 已回复', time: '30分钟前' },
  { content: '合同 CT-2024-001 已签署', time: '1小时前' },
  { content: '发货单 SH-2024-015 已发出', time: '2小时前' },
  { content: '收到付款 ¥50,000', time: '3小时前' },
]

/**
 * 处理快捷入口点击
 */
function handleQuickAccess(path: string) {
  router.push(path)
}
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
