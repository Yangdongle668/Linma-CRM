import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, logout as logoutApi, getUserInfo, type LoginParams, type UserInfo } from '@/api/auth'
import router from '@/router'

// Token的localStorage键名
const TOKEN_KEY = 'crm_token'

/**
 * 用户状态管理
 */
export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) || '')
  const userInfo = ref<UserInfo | null>(null)

  // 计算属性: 是否已登录
  const isLoggedIn = computed(() => !!token.value)

  /**
   * 用户登录
   * @param params 登录参数(用户名和密码)
   */
  async function login(params: LoginParams) {
    try {
      // 调用登录API
      const result = await loginApi(params)
      
      // 保存token
      token.value = result.token
      localStorage.setItem(TOKEN_KEY, result.token)
      
      // 保存用户信息
      userInfo.value = result.userInfo
      
      return result
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }

  /**
   * 获取用户信息
   */
  async function fetchUserInfo() {
    try {
      const info = await getUserInfo()
      userInfo.value = info
      return info
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    }
  }

  /**
   * 用户登出
   */
  async function logout() {
    try {
      // 调用登出API
      await logoutApi()
    } catch (error) {
      console.error('登出API调用失败:', error)
    } finally {
      // 无论API调用成功与否,都清除本地状态
      token.value = ''
      userInfo.value = null
      localStorage.removeItem(TOKEN_KEY)
      
      // 跳转到登录页
      router.push('/login')
    }
  }

  /**
   * 重置状态(用于清理)
   */
  function resetState() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem(TOKEN_KEY)
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    fetchUserInfo,
    logout,
    resetState,
  }
})
