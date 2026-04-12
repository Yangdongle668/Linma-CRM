<template>
  <div class="sidebar">
    <!-- Logo区域 -->
    <div class="logo-container">
      <el-icon :size="32" color="#667eea"><Connection /></el-icon>
      <transition name="fade">
        <span v-if="!sidebarCollapsed" class="logo-title">外贸CRM</span>
      </transition>
    </div>

    <!-- 菜单区域 -->
    <el-menu
      :default-active="activeMenu"
      :collapse="sidebarCollapsed"
      :unique-opened="true"
      router
      background-color="#1F2937"
      text-color="#D1D5DB"
      active-text-color="#FFFFFF"
    >
      <!-- 首页 -->
      <el-menu-item index="/dashboard">
        <el-icon><HomeFilled /></el-icon>
        <template #title>首页</template>
      </el-menu-item>

      <!-- 任务管理 -->
      <el-sub-menu index="/task">
        <template #title>
          <el-icon><List /></el-icon>
          <span>任务</span>
        </template>
        <el-menu-item index="/task/list">任务列表</el-menu-item>
      </el-sub-menu>

      <!-- 客户管理 -->
      <el-sub-menu index="/customer">
        <template #title>
          <el-icon><User /></el-icon>
          <span>客户管理</span>
        </template>
        <el-menu-item index="/customer/list">客户列表</el-menu-item>
        <el-menu-item index="/customer/follow">跟进记录</el-menu-item>
      </el-sub-menu>

      <!-- 产品管理 -->
      <el-sub-menu index="/product">
        <template #title>
          <el-icon><Goods /></el-icon>
          <span>产品管理</span>
        </template>
        <el-menu-item index="/product/list">产品列表</el-menu-item>
        <el-menu-item index="/product/category">产品分类</el-menu-item>
      </el-sub-menu>

      <!-- 询盘管理 -->
      <el-sub-menu index="/inquiry">
        <template #title>
          <el-icon><Message /></el-icon>
          <span>询盘管理</span>
        </template>
        <el-menu-item index="/inquiry/list">询盘列表</el-menu-item>
        <el-menu-item index="/inquiry/reply">询盘回复</el-menu-item>
      </el-sub-menu>

      <!-- 合同管理 -->
      <el-sub-menu index="/contract">
        <template #title>
          <el-icon><Document /></el-icon>
          <span>合同管理</span>
        </template>
        <el-menu-item index="/contract/list">合同列表</el-menu-item>
        <el-menu-item index="/contract/template">合同模板</el-menu-item>
      </el-sub-menu>

      <!-- 物流管理 -->
      <el-sub-menu index="/shipping">
        <template #title>
          <el-icon><Van /></el-icon>
          <span>物流管理</span>
        </template>
        <el-menu-item index="/shipping/list">发货单</el-menu-item>
        <el-menu-item index="/shipping/tracking">物流追踪</el-menu-item>
      </el-sub-menu>

      <!-- 财务管理 -->
      <el-sub-menu index="/finance">
        <template #title>
          <el-icon><Money /></el-icon>
          <span>财务管理</span>
        </template>
        <el-menu-item index="/finance/receivable">应收账款</el-menu-item>
        <el-menu-item index="/finance/payable">应付账款</el-menu-item>
        <el-menu-item index="/finance/profit">利润分析</el-menu-item>
      </el-sub-menu>

      <!-- 数据分析 -->
      <el-sub-menu index="/analytics">
        <template #title>
          <el-icon><TrendCharts /></el-icon>
          <span>数据分析</span>
        </template>
        <el-menu-item index="/analytics/dashboard">数据看板</el-menu-item>
        <el-menu-item index="/analytics/funnel">销售漏斗</el-menu-item>
        <el-menu-item index="/analytics/ranking">业绩排行</el-menu-item>
      </el-sub-menu>

      <!-- 消息中心 -->
      <el-sub-menu index="/message">
        <template #title>
          <el-icon><Bell /></el-icon>
          <span>消息中心</span>
        </template>
        <el-menu-item index="/message/email">邮箱客户端</el-menu-item>
        <el-menu-item index="/message/template">邮件模板</el-menu-item>
        <el-menu-item index="/message/notification">通知中心</el-menu-item>
      </el-sub-menu>

      <!-- 系统设置 -->
      <el-sub-menu index="/system">
        <template #title>
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </template>
        <el-menu-item index="/system/user">用户管理</el-menu-item>
        <el-menu-item index="/system/role">角色管理</el-menu-item>
        <el-menu-item index="/system/config">系统配置</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'

const route = useRoute()
const appStore = useAppStore()

// 侧边栏折叠状态
const sidebarCollapsed = computed(() => appStore.sidebarCollapsed)

// 当前激活的菜单
const activeMenu = computed(() => route.path)
</script>

<style scoped lang="scss">
.sidebar {
  width: 100%;
  height: 100%;
  background: #1F2937;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: rgba(0, 0, 0, 0.2);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  
  .logo-title {
    font-size: 18px;
    font-weight: bold;
    color: white;
    white-space: nowrap;
  }
}

.el-menu {
  flex: 1;
  overflow-y: auto;
  border-right: none;
  
  // 自定义滚动条
  &::-webkit-scrollbar {
    width: 4px;
  }
  
  &::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 2px;
  }
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  &:hover {
    background: rgba(255, 255, 255, 0.1) !important;
  }
}

:deep(.el-menu-item.is-active) {
  background: rgba(102, 126, 234, 0.3) !important;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 3px;
    background: #667eea;
  }
}
</style>
