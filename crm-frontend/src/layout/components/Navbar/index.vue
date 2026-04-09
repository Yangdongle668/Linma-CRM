<template>
  <div class="navbar">
    <!-- 左侧: 折叠按钮 + 面包屑 -->
    <div class="navbar-left">
      <el-icon 
        class="hamburger" 
        :class="{ 'is-active': sidebarCollapsed }"
        @click="toggleSidebar"
      >
        <Fold v-if="!sidebarCollapsed" />
        <Expand v-else />
      </el-icon>
      
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>当前页面</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 右侧: 功能按钮 + 用户信息 -->
    <div class="navbar-right">
      <!-- 全屏按钮 -->
      <el-tooltip content="全屏" placement="bottom">
        <el-icon class="navbar-icon" @click="toggleFullscreen">
          <FullScreen />
        </el-icon>
      </el-tooltip>

      <!-- 消息通知 -->
      <el-badge :value="3" class="notification-badge">
        <el-tooltip content="消息通知" placement="bottom">
          <el-icon class="navbar-icon">
            <Bell />
          </el-icon>
        </el-tooltip>
      </el-badge>

      <!-- 用户下拉菜单 -->
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="36" icon="UserFilled" />
          <span class="username">管理员</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              系统设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

// 侧边栏折叠状态
const sidebarCollapsed = computed(() => appStore.sidebarCollapsed)

/**
 * 切换侧边栏折叠状态
 */
function toggleSidebar() {
  appStore.toggleSidebar()
}

/**
 * 切换全屏
 */
function toggleFullscreen() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

/**
 * 处理下拉菜单命令
 */
async function handleCommand(command: string) {
  switch (command) {
    case 'profile':
      // 跳转到个人中心
      console.log('跳转到个人中心')
      break
    case 'settings':
      // 跳转到系统设置
      console.log('跳转到系统设置')
      break
    case 'logout':
      // 退出登录确认
      try {
        await ElMessageBox.confirm('确定要退出登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
        await userStore.logout()
      } catch {
        // 用户取消
      }
      break
  }
}
</script>

<style scoped lang="scss">
.navbar {
  height: 60px;
  background: white;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 20px;
  
  .hamburger {
    font-size: 20px;
    cursor: pointer;
    transition: transform 0.3s;
    padding: 8px;
    border-radius: 4px;
    
    &:hover {
      background: #F3F4F6;
    }
    
    &.is-active {
      transform: rotate(180deg);
    }
  }
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 20px;
  
  .navbar-icon {
    font-size: 20px;
    cursor: pointer;
    padding: 8px;
    border-radius: 4px;
    transition: all 0.3s;
    
    &:hover {
      background: #F3F4F6;
      color: #2563EB;
    }
  }
  
  .notification-badge {
    cursor: pointer;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 6px 12px;
    border-radius: 20px;
    transition: background 0.3s;
    
    &:hover {
      background: #F3F4F6;
    }
    
    .username {
      font-size: 14px;
      color: #1F2937;
    }
  }
}
</style>
