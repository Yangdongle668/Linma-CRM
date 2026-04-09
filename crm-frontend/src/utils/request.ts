import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应数据接口定义
export interface ApiResponse<T = any> {
  code: number
  data: T
  msg: string
}

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API || '/api',
  timeout: 30000, // 请求超时时间30秒
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      // 在请求头中添加Bearer Token
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data

    // 如果返回的状态码不是200,则认为是错误
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求失败')

      // 401: 未授权,需要重新登录
      if (res.code === 401) {
        ElMessageBox.confirm('登录状态已过期,请重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          // 清除token并跳转登录页
          localStorage.removeItem('token')
          window.location.href = '/login'
        })
      }

      return Promise.reject(new Error(res.msg || '请求失败'))
    }

    // 返回响应数据
    return res.data
  },
  (error) => {
    console.error('响应错误:', error)

    // 处理HTTP错误状态码
    let message = '网络错误'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权,请重新登录'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用'
          break
        case 504:
          message = '网关超时'
          break
        default:
          message = `连接错误${error.response.status}`
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时,请稍后重试'
    } else if (error.message.includes('Network Error')) {
      message = '网络连接异常,请检查网络'
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// 导出axios实例
export default service
