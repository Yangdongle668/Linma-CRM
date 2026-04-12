import request from '@/utils/request'

// 跟进记录接口定义
export interface FollowUp {
  id?: number
  customerId: number
  customerName?: string
  
  // 跟进类型
  followType: 'email' | 'call' | 'meeting' | 'wechat' | 'visit' | 'other'
  
  // 跟进内容
  subject?: string
  content: string
  
  // 下次跟进时间
  nextFollowTime?: string
  
  // 创建信息
  creatorId?: number
  creatorName?: string
  createTime?: string
  updateTime?: string
  
  status?: string
}

export interface FollowUpQuery {
  pageNum?: number
  pageSize?: number
  customerId?: number
  followType?: string
  keyword?: string
}

/**
 * 分页查询跟进记录列表
 */
export function getFollowUpList(params: FollowUpQuery) {
  return request({
    url: '/followup/page',
    method: 'get',
    params,
  })
}

/**
 * 获取客户的所有跟进记录
 */
export function getFollowUpByCustomerId(customerId: number) {
  return request({
    url: `/followup/timeline/${customerId}`,
    method: 'get',
  })
}

/**
 * 获取跟进记录详情
 */
export function getFollowUpById(id: number) {
  return request({
    url: `/followup/${id}`,
    method: 'get',
  })
}

/**
 * 创建跟进记录
 */
export function createFollowUp(data: FollowUp) {
  return request({
    url: '/followup',
    method: 'post',
    data,
  })
}

/**
 * 更新跟进记录
 */
export function updateFollowUp(data: FollowUp) {
  return request({
    url: `/followup/${data.id}`,
    method: 'put',
    data: {
      followContent: data.content,
      nextPlan: data.subject || '',
      nextFollowTime: data.nextFollowTime,
    },
  })
}

/**
 * 删除跟进记录
 */
export function deleteFollowUp(ids: number[]) {
  return request({
    url: `/followup/${ids.join(',')}`,
    method: 'delete',
  })
}
