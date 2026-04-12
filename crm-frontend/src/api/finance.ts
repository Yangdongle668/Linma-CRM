import request from '@/utils/request'

// ========== 应收账款相关 ==========

export interface Receivable {
  id?: number
  receivableNo?: string
  customerId?: number
  customerName?: string
  orderId?: number
  orderNo?: string
  amount?: number
  receivedAmount?: number
  balance?: number
  currency?: string
  dueDate?: string
  status?: string
  remark?: string
  createdTime?: string
}

export interface ReceivableQuery {
  pageNum?: number
  pageSize?: number
  receivableNo?: string
  customerId?: number
  status?: string
  startDate?: string
  endDate?: string
}

export interface PaymentRecordDTO {
  receivableId: number
  amount: number
  paymentDate: string
  paymentMethod?: string
  referenceNo?: string
  remark?: string
}

/**
 * 分页查询应收账款列表
 */
export function getReceivableList(params: ReceivableQuery) {
  return request({
    url: '/finance/receivable/page',
    method: 'get',
    params,
  })
}

/**
 * 获取应收账款详情
 */
export function getReceivableById(id: number) {
  return request({
    url: `/finance/receivable/${id}`,
    method: 'get',
  })
}

/**
 * 创建应收账款
 */
export function createReceivable(data: Receivable) {
  return request({
    url: '/finance/receivable',
    method: 'post',
    data,
  })
}

/**
 * 更新应收账款
 */
export function updateReceivable(data: Receivable) {
  return request({
    url: '/finance/receivable',
    method: 'put',
    data,
  })
}

/**
 * 删除应收账款
 */
export function deleteReceivable(ids: number[]) {
  return request({
    url: '/finance/receivable',
    method: 'delete',
    data: ids,
  })
}

/**
 * 记录收款
 */
export function recordPayment(data: PaymentRecordDTO) {
  return request({
    url: '/finance/receivable/payment',
    method: 'post',
    data,
  })
}

/**
 * 获取收款记录
 */
export function getPaymentRecords(receivableId: number) {
  return request({
    url: `/finance/receivable/${receivableId}/payments`,
    method: 'get',
  })
}

// ========== 应付账款相关 ==========

export interface Payable {
  id?: number
  payableNo?: string
  supplierName: string
  orderId?: number
  orderNo?: string
  amount?: number
  paidAmount?: number
  balance?: number
  currency?: string
  dueDate?: string
  status?: string
  remark?: string
  createdTime?: string
}

export interface PayableQuery {
  pageNum?: number
  pageSize?: number
  payableNo?: string
  supplierName?: string
  status?: string
  startDate?: string
  endDate?: string
}

export interface PaymentApprovalDTO {
  payableId: number
  approved: boolean
  remark?: string
}

/**
 * 分页查询应付账款列表
 */
export function getPayableList(params: PayableQuery) {
  return request({
    url: '/finance/payable/page',
    method: 'get',
    params,
  })
}

/**
 * 获取应付账款详情
 */
export function getPayableById(id: number) {
  return request({
    url: `/finance/payable/${id}`,
    method: 'get',
  })
}

/**
 * 创建应付账款
 */
export function createPayable(data: Payable) {
  return request({
    url: '/finance/payable',
    method: 'post',
    data,
  })
}

/**
 * 更新应付账款
 */
export function updatePayable(data: Payable) {
  return request({
    url: '/finance/payable',
    method: 'put',
    data,
  })
}

/**
 * 删除应付账款
 */
export function deletePayable(ids: number[]) {
  return request({
    url: '/finance/payable',
    method: 'delete',
    data: ids,
  })
}

/**
 * 提交付款审批
 */
export function submitPaymentApproval(payableId: number, remark?: string) {
  return request({
    url: '/finance/payable/submit-approval',
    method: 'post',
    data: { payableId, remark },
  })
}

/**
 * 审批付款
 */
export function approvePayment(data: PaymentApprovalDTO) {
  return request({
    url: '/finance/payable/approve',
    method: 'post',
    data,
  })
}

/**
 * 记录付款
 */
export function recordPayablePayment(payableId: number, amount: number, paymentDate: string) {
  return request({
    url: '/finance/payable/payment',
    method: 'post',
    data: { payableId, amount, paymentDate },
  })
}

// ========== 利润分析相关 ==========

export interface ProfitAnalysis {
  period?: string
  revenue?: number
  cost?: number
  profit?: number
  profitMargin?: number
  orderCount?: number
}

export interface ProfitQuery {
  startDate?: string
  endDate?: string
  customerId?: number
  productId?: number
}

/**
 * 获取利润分析
 */
export function getProfitAnalysis(params: ProfitQuery) {
  return request({
    url: '/finance/profit/analysis',
    method: 'get',
    params,
  })
}

/**
 * 获取利润趋势
 */
export function getProfitTrend(params: ProfitQuery) {
  return request({
    url: '/finance/profit/trend',
    method: 'get',
    params,
  })
}

/**
 * 按客户统计利润
 */
export function getProfitByCustomer(params: ProfitQuery) {
  return request({
    url: '/finance/profit/by-customer',
    method: 'get',
    params,
  })
}

/**
 * 按产品统计利润
 */
export function getProfitByProduct(params: ProfitQuery) {
  return request({
    url: '/finance/profit/by-product',
    method: 'get',
    params,
  })
}
