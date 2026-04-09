import { defineStore } from 'pinia'
import { ref } from 'vue'

/**
 * 应用全局状态管理
 */
export const useAppStore = defineStore('app', () => {
  // 侧边栏折叠状态
  const sidebarCollapsed = ref<boolean>(false)

  // 设备类型(移动端/桌面端)
  const device = ref<'mobile' | 'desktop'>('desktop')

  // 主题色
  const themeColor = ref<string>('#2563EB')

  /**
   * 切换侧边栏折叠状态
   */
  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  /**
   * 设置侧边栏折叠状态
   * @param collapsed 是否折叠
   */
  function setSidebarCollapsed(collapsed: boolean) {
    sidebarCollapsed.value = collapsed
  }

  /**
   * 设置设备类型
   * @param deviceType 设备类型
   */
  function setDevice(deviceType: 'mobile' | 'desktop') {
    device.value = deviceType
  }

  /**
   * 设置主题色
   * @param color 主题颜色
   */
  function setThemeColor(color: string) {
    themeColor.value = color
  }

  return {
    sidebarCollapsed,
    device,
    themeColor,
    toggleSidebar,
    setSidebarCollapsed,
    setDevice,
    setThemeColor,
  }
})
