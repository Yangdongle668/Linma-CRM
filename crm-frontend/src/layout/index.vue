<template>
  <div class="app-wrapper" :class="{ 'mobile': device === 'mobile' }">
    <!-- 侧边栏 -->
    <Sidebar class="sidebar-container" />
    
    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <Navbar />
      
      <!-- 标签栏 -->
      <TagsView />
      
      <!-- 应用主内容 -->
      <AppMain />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'
import Sidebar from './components/Sidebar/index.vue'
import Navbar from './components/Navbar/index.vue'
import TagsView from './components/TagsView/index.vue'
import AppMain from './components/AppMain.vue'

const appStore = useAppStore()

// 设备类型
const device = computed(() => appStore.device)
</script>

<style scoped lang="scss">
.app-wrapper {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  
  &.mobile {
    .sidebar-container {
      transform: translateX(-100%);
    }
  }
}

.sidebar-container {
  width: 210px;
  height: 100%;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 1001;
  transition: width 0.3s;
}

.main-container {
  flex: 1;
  margin-left: 210px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left 0.3s;
}

// 移动端适配
@media (max-width: 768px) {
  .sidebar-container {
    width: 210px;
  }
  
  .main-container {
    margin-left: 0;
  }
}
</style>
