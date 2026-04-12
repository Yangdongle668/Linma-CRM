import request from '@/utils/request'

// ========== 仪表盘数据 ==========

export interface DashboardData {
  totalCustomers?: number
  totalOrders?: number
  totalRevenue?: number
  totalInquiries?: number
  customerGrowth?: number
  orderGrowth?: number
  revenueGrowth?: number
  inquiryGrowth?: number
}

/**
 * 获取仪表盘数据
 */
export function getDashboardData(userId?: number) {
  return request({
    url: '/analytics/dashboard',
    method: 'get',
    params: userId ? { userId } : undefined,
  })
}

// ========== 销售漏斗 ==========

export interface FunnelData {
  stage?: string
  count?: number
  conversionRate?: number
}

/**
 * 获取销售漏斗数据
 */
export function getSalesFunnel(params?: any) {
  return request({
    url: '/analytics/funnel',
    method: 'get',
    params,
  })
}

// ========== 排行榜 ==========

export interface RankingData {
  name?: string
  value?: number
  rank?: number
}

/**
 * 获取销售排行
 */
export function getRanking(params?: any) {
  return request({
    url: '/analytics/ranking',
    method: 'get',
    params,
  })
}

// ========== 趋势分析 ==========

export interface TrendData {
  date?: string
  value?: number
}

/**
 * 获取销售趋势
 */
export function getSalesTrend(params?: any) {
  return request({
    url: '/analytics/trend/sales',
    method: 'get',
    params,
  })
}

/**
 * 获取客户增长趋势
 */
export function getCustomerTrend(params?: any) {
  return request({
    url: '/analytics/trend/customers',
    method: 'get',
    params,
  })
}

// ========== 客户分析 ==========

/**
 * 获取客户分析数据
 */
export function getCustomerAnalytics(params?: any) {
  return request({
    url: '/analytics/customer',
    method: 'get',
    params,
  })
}

/**
 * 获取客户地域分布
 */
export function getCustomerRegionDistribution(params?: any) {
  return request({
    url: '/analytics/customer/region',
    method: 'get',
    params,
  })
}

// ========== 产品分析 ==========

/**
 * 获取产品销售分析
 */
export function getProductAnalytics(params?: any) {
  return request({
    url: '/analytics/product',
    method: 'get',
    params,
  })
}

/**
 * 获取产品销量排行
 */
export function getProductRanking(params?: any) {
  return request({
    url: '/analytics/product/ranking',
    method: 'get',
    params,
  })
}

// ========== 区域分析 ==========

/**
 * 获取区域销售分析
 */
export function getRegionAnalytics(params?: any) {
  return request({
    url: '/analytics/region',
    method: 'get',
    params,
  })
}
