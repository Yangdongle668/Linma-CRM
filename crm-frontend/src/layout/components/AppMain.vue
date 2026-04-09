<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="cachedViews">
          <component :is="Component" :key="route.path" />
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'

// 缓存的视图列表(实际项目中可以从store获取)
const cachedViews = computed(() => {
  // 这里可以配置需要缓存的页面名称
  return []
})
</script>

<style scoped lang="scss">
.app-main {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #F3F4F6;
}

// 页面过渡动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
