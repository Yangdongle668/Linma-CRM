<template>
  <div class="tags-view">
    <el-scrollbar>
      <div class="tags-container">
        <router-link
          v-for="tag in visitedViews"
          :key="tag.path"
          :to="{ path: tag.path, query: tag.query }"
          class="tag-item"
          :class="{ active: isActive(tag) }"
          @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
          @contextmenu.prevent="openContextMenu($event, tag)"
        >
          {{ tag.title }}
          <el-icon 
            v-if="!isAffix(tag)" 
            class="close-icon"
            @click.prevent.stop="closeSelectedTag(tag)"
          >
            <Close />
          </el-icon>
        </router-link>
      </div>
    </el-scrollbar>

    <!-- 右键菜单 -->
    <ul
      v-show="contextMenuVisible"
      :style="{ left: contextMenuLeft + 'px', top: contextMenuTop + 'px' }"
      class="context-menu"
    >
      <li @click="refreshSelectedTag(selectedTag)">
        <el-icon><Refresh /></el-icon>
        刷新
      </li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
        <el-icon><Close /></el-icon>
        关闭
      </li>
      <li @click="closeOtherTags">
        <el-icon><Close /></el-icon>
        关闭其他
      </li>
      <li @click="closeAllTags">
        <el-icon><Close /></el-icon>
        关闭全部
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { RouteLocationNormalizedLoaded } from 'vue-router'

interface TagView extends Partial<RouteLocationNormalizedLoaded> {
  title?: string
  affix?: boolean
}

const route = useRoute()
const router = useRouter()

// 已访问的标签列表
const visitedViews = ref<TagView[]>([
  {
    path: '/dashboard',
    title: '首页',
    affix: true,
  },
])

// 当前选中的标签
const selectedTag = ref<TagView>({})

// 右键菜单状态
const contextMenuVisible = ref(false)
const contextMenuLeft = ref(0)
const contextMenuTop = ref(0)

/**
 * 判断标签是否激活
 */
function isActive(tag: TagView): boolean {
  return tag.path === route.path
}

/**
 * 判断是否是固定标签(不可关闭)
 */
function isAffix(tag: TagView): boolean {
  return tag.affix || false
}

/**
 * 添加标签
 */
function addTag() {
  const { path, name, meta, fullpath, query } = route
  
  // 如果路由没有标题,不添加标签
  if (!meta?.title) return
  
  // 检查标签是否已存在
  const exists = visitedViews.value.some(v => v.path === path)
  if (exists) return
  
  // 添加新标签
  visitedViews.value.push({
    path,
    name,
    meta: { ...meta },
    query,
    title: meta.title as string,
    affix: meta.affix as boolean,
  })
}

/**
 * 关闭选中的标签
 */
function closeSelectedTag(view: TagView) {
  const index = visitedViews.value.findIndex(v => v.path === view.path)
  if (index > -1) {
    visitedViews.value.splice(index, 1)
  }
  
  // 如果关闭的是当前激活的标签,跳转到最后一个标签
  if (isActive(view)) {
    toLastView(visitedViews.value, view)
  }
}

/**
 * 刷新选中的标签
 */
function refreshSelectedTag(view: TagView) {
  // 实际项目中可以重新加载组件
  console.log('刷新标签:', view.title)
}

/**
 * 关闭其他标签
 */
function closeOtherTags() {
  visitedViews.value = visitedViews.value.filter(tag => {
    return tag.affix || tag.path === selectedTag.value.path
  })
}

/**
 * 关闭所有标签(保留固定标签)
 */
function closeAllTags() {
  visitedViews.value = visitedViews.value.filter(tag => tag.affix)
  toLastView(visitedViews.value, selectedTag.value)
}

/**
 * 跳转到最后一个标签
 */
function toLastView(visitedViews: TagView[], view: TagView) {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView && latestView.path) {
    router.push(latestView.path)
  } else {
    router.push('/')
  }
}

/**
 * 打开右键菜单
 */
function openContextMenu(e: MouseEvent, tag: TagView) {
  selectedTag.value = tag
  contextMenuVisible.value = true
  contextMenuLeft.value = e.clientX
  contextMenuTop.value = e.clientY
  
  // 点击其他地方关闭菜单
  document.body.addEventListener('click', () => {
    contextMenuVisible.value = false
  })
}

// 监听路由变化,自动添加标签
watch(
  () => route.path,
  () => {
    addTag()
  },
  { immediate: true }
)
</script>

<style scoped lang="scss">
.tags-view {
  height: 40px;
  background: white;
  border-bottom: 1px solid #E5E7EB;
  position: relative;
  
  .tags-container {
    display: flex;
    align-items: center;
    padding: 0 10px;
    height: 100%;
    
    .tag-item {
      display: inline-flex;
      align-items: center;
      padding: 4px 12px;
      margin-right: 8px;
      font-size: 13px;
      color: #6B7280;
      background: #F3F4F6;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.3s;
      text-decoration: none;
      
      &:hover {
        color: #2563EB;
      }
      
      &.active {
        color: white;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        
        .close-icon {
          &:hover {
            background: rgba(255, 255, 255, 0.3);
          }
        }
      }
      
      .close-icon {
        margin-left: 6px;
        font-size: 12px;
        padding: 2px;
        border-radius: 50%;
        
        &:hover {
          background: rgba(0, 0, 0, 0.1);
        }
      }
    }
  }
}

.context-menu {
  position: fixed;
  min-width: 100px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  list-style: none;
  padding: 5px 0;
  z-index: 3000;
  
  li {
    padding: 8px 16px;
    cursor: pointer;
    font-size: 13px;
    color: #4B5563;
    display: flex;
    align-items: center;
    gap: 8px;
    
    &:hover {
      background: #F3F4F6;
      color: #2563EB;
    }
  }
}
</style>
