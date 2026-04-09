import request from '@/utils/request'

// 登录请求参数接口
export interface LoginParams {
  username: string
  password: string
}

// 登录响应数据接口
export interface LoginResult {
  token: string
  userInfo: UserInfo
}

// 用户信息接口
export interface UserInfo {
  id: number
  username: string
  realName: string
  email: string
  phone: string
  avatar?: string
  roles: string[]
  permissions: string[]
}

/**
 * 用户登录
 * @param params 登录参数
 * @returns 登录结果(包含token和用户信息)
 */
export function login(params: LoginParams): Promise<LoginResult> {
  return request({
    url: '/auth/login',
    method: 'post',
    data: params,
  })
}

/**
 * 获取当前用户信息
 * @returns 用户信息
 */
export function getUserInfo(): Promise<UserInfo> {
  return request({
    url: '/auth/userinfo',
    method: 'get',
  })
}

/**
 * 用户登出
 */
export function logout(): Promise<void> {
  return request({
    url: '/auth/logout',
    method: 'post',
  })
}

/**
 * 刷新Token
 * @returns 新的token
 */
export function refreshToken(): Promise<{ token: string }> {
  return request({
    url: '/auth/refresh',
    method: 'post',
  })
}
