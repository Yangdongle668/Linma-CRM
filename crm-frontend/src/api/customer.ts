import request from '@/utils/request'

// 客户接口定义
export interface Customer {
  id?: number
  // 基本信息
  companyName?: string
  companyNameCn?: string
  website?: string
  emailDomain?: string
  customerAvatar?: string
  companyLogo?: string
  
  // 社交媒体
  socialLinkedin?: string
  socialFacebook?: string
  socialTwitter?: string
  
  // 公司信息
  industry?: string
  companySize?: string
  businessType?: string
  mainProducts?: string
  mainMarkets?: string
  establishedYear?: number
  annualRevenue?: number
  
  // 认证信息
  taxId?: string
  registrationNumber?: string
  importLicense?: string
  certificationIso?: number
  certificationCe?: number
  certificationFda?: number
  
  // 地址信息
  country?: string
  province?: string
  city?: string
  address?: string
  timezone?: string
  languagePreference?: string
  
  // 客户分级
  level?: string
  priority?: string
  creditRating?: string
  paymentTerms?: string
  
  // 来源信息
  source?: string
  acquisitionChannel?: string
  firstContactDate?: string
  customerLifecycle?: string
  
  // 风险与机会
  riskLevel?: string
  riskFactors?: string
  opportunityValue?: number
  conversionProbability?: number
  expectedCloseDate?: string
  
  // 满意度
  satisfactionScore?: number
  npsScore?: number
  
  // 统计信息
  totalOrder?: number
  
  // 负责人和公海
  ownerId?: number
  ownerName?: string
  isHighSea?: number
  lastFollowTime?: string
  
  // 其他
  remark?: string
  status?: string
  createdTime?: string
}

export interface CustomerQuery {
  pageNum?: number
  pageSize?: number
  companyName?: string
  country?: string
  level?: string
  status?: string
  industry?: string
  customerLifecycle?: string
  keyword?: string
  currentUserId?: number
  currentUserRole?: string
  onlyMyCustomers?: boolean
  isHighSea?: number
}

/**
 * 分页查询客户列表
 */
export function getCustomerList(params: CustomerQuery) {
  return request({
    url: '/customer/page',
    method: 'get',
    params,
  })
}

/**
 * 获取客户详情
 */
export function getCustomerById(id: number) {
  return request({
    url: `/customer/${id}`,
    method: 'get',
  })
}

/**
 * 创建客户
 */
export function createCustomer(data: Customer) {
  return request({
    url: '/customer',
    method: 'post',
    data,
  })
}

/**
 * 更新客户
 */
export function updateCustomer(data: Customer) {
  return request({
    url: '/customer',
    method: 'put',
    data,
  })
}

/**
 * 删除客户
 */
export function deleteCustomer(ids: number[]) {
  return request({
    url: `/customer/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 导出客户数据
 */
export function exportCustomers(params: CustomerQuery) {
  return request({
    url: '/customer/export',
    method: 'get',
    params,
    responseType: 'blob',
  })
}

/**
 * 获取公海客户列表
 */
export function getHighSeaCustomers(pageNum: number = 1, pageSize: number = 10) {
  return request({
    url: '/customer/highsea/page',
    method: 'get',
    params: { pageNum, pageSize },
  })
}

/**
 * 从公海领取客户
 */
export function claimFromHighSea(customerIds: number[], remark?: string) {
  return request({
    url: '/customer/highsea/claim',
    method: 'post',
    data: { customerIds, remark },
  })
}

/**
 * 释放客户到公海
 */
export function releaseToHighSea(customerIds: number[]) {
  return request({
    url: '/customer/highsea/release',
    method: 'post',
    data: customerIds,
  })
}

/**
 * 分配客户给指定负责人
 */
export function assignCustomers(customerIds: number[], ownerId: number, remark?: string) {
  return request({
    url: '/customer/assign',
    method: 'post',
    data: { customerIds, ownerId, remark },
  })
}
