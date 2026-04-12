import request from '@/utils/request'

export interface Inquiry {
  id?: number
  inquiryNo?: string
  customerId?: number
  customerName?: string
  subject: string
  content?: string
  status?: string
  priority?: string
  source?: string
  ownerId?: number
  ownerName?: string
  createdTime?: string
  updatedTime?: string
}

export interface InquiryQuery {
  pageNum?: number
  pageSize?: number
  inquiryNo?: string
  customerName?: string
  status?: string
  ownerId?: number
  startDate?: string
  endDate?: string
}

export interface InquiryCreateDTO {
  customerId: number
  subject: string
  content: string
  priority?: string
  source?: string
  attachmentUrls?: string[]
}

export interface InquiryUpdateDTO {
  id: number
  subject?: string
  content?: string
  priority?: string
  status?: string
  ownerId?: number
}

export interface InquiryReplyDTO {
  inquiryId: number
  content: string
  isPublic?: boolean
}

/**
 * 分页查询询盘列表
 */
export function getInquiryList(params: InquiryQuery) {
  return request({
    url: '/inquiry/page',
    method: 'get',
    params,
  })
}

/**
 * 获取询盘详情
 */
export function getInquiryById(id: number) {
  return request({
    url: `/inquiry/${id}`,
    method: 'get',
  })
}

/**
 * 创建询盘
 */
export function createInquiry(data: InquiryCreateDTO) {
  return request({
    url: '/inquiry',
    method: 'post',
    data,
  })
}

/**
 * 更新询盘
 */
export function updateInquiry(data: InquiryUpdateDTO) {
  return request({
    url: '/inquiry',
    method: 'put',
    data,
  })
}

/**
 * 删除询盘
 */
export function deleteInquiry(ids: number[]) {
  return request({
    url: '/inquiry',
    method: 'delete',
    data: ids,
  })
}

/**
 * 回复询盘
 */
export function replyInquiry(data: InquiryReplyDTO) {
  return request({
    url: '/inquiry/reply',
    method: 'post',
    data,
  })
}

/**
 * 分配询盘
 */
export function assignInquiry(inquiryId: number, ownerId: number) {
  return request({
    url: '/inquiry/assign',
    method: 'put',
    data: { inquiryId, ownerId },
  })
}

/**
 * 关闭询盘
 */
export function closeInquiry(id: number, reason?: string) {
  return request({
    url: `/inquiry/${id}/close`,
    method: 'put',
    data: { reason },
  })
}

/**
 * 重新打开询盘
 */
export function reopenInquiry(id: number) {
  return request({
    url: `/inquiry/${id}/reopen`,
    method: 'put',
  })
}
