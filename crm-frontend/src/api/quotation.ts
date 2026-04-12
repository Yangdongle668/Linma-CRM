import request from '@/utils/request'

// 报价单接口定义
export interface Quotation {
  id?: number
  quotationNo: string
  customerId?: number
  customerName?: string
  amount?: number
  status?: string
  validUntil?: string
  createdTime?: string
}

export interface QuotationQuery {
  pageNum?: number
  pageSize?: number
  quotationNo?: string
  customerId?: number
  status?: string
  startDate?: string
  endDate?: string
}

export interface QuotationCreateDTO {
  customerId: number
  items: QuotationItemDTO[]
  currency?: string
  validDays?: number
  remark?: string
}

export interface QuotationItemDTO {
  productId: number
  productName: string
  quantity: number
  unitPrice: number
  discount?: number
  remark?: string
}

export interface QuotationUpdateDTO {
  id: number
  items?: QuotationItemDTO[]
  currency?: string
  validDays?: number
  remark?: string
}

export interface QuotationApprovalDTO {
  quotationId: number
  approved: boolean
  remark?: string
}

export interface QuotationEmailDTO {
  quotationId: number
  recipientEmail: string
  subject?: string
  message?: string
}

/**
 * 分页查询报价单列表
 */
export function getQuotationList(params: QuotationQuery) {
  return request({
    url: '/quotation/page',
    method: 'get',
    params,
  })
}

/**
 * 获取报价单详情
 */
export function getQuotationById(id: number) {
  return request({
    url: `/quotation/${id}`,
    method: 'get',
  })
}

/**
 * 创建报价单
 */
export function createQuotation(data: QuotationCreateDTO) {
  return request({
    url: '/quotation',
    method: 'post',
    data,
  })
}

/**
 * 更新报价单
 */
export function updateQuotation(data: QuotationUpdateDTO) {
  return request({
    url: '/quotation',
    method: 'put',
    data,
  })
}

/**
 * 删除报价单
 */
export function deleteQuotation(ids: number[]) {
  return request({
    url: `/quotation/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 提交审批
 */
export function submitForApproval(id: number) {
  return request({
    url: `/quotation/${id}/submit-approval`,
    method: 'post',
  })
}

/**
 * 审批报价单
 */
export function approveQuotation(data: QuotationApprovalDTO) {
  return request({
    url: '/quotation/approve',
    method: 'post',
    data,
  })
}

/**
 * 生成PDF
 */
export function generatePdf(id: number) {
  return request({
    url: `/quotation/${id}/pdf`,
    method: 'post',
    responseType: 'blob',
  })
}

/**
 * 发送邮件
 */
export function sendQuotationEmail(data: QuotationEmailDTO) {
  return request({
    url: '/quotation/send-email',
    method: 'post',
    data,
  })
}

/**
 * 导出报价单Excel
 */
export function exportQuotations(params: QuotationQuery) {
  return request({
    url: '/quotation/export',
    method: 'get',
    params,
    responseType: 'blob',
  })
}
