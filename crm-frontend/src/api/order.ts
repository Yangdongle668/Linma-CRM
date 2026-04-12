import request from '@/utils/request'

// 订单接口定义
export interface Order {
  id?: number
  orderNo: string
  customerId?: number
  customerName?: string
  amount?: number
  status?: string
  orderDate?: string
  deliveryDate?: string
  remark?: string
  createdTime?: string
}

export interface OrderQuery {
  pageNum?: number
  pageSize?: number
  orderNo?: string
  customerId?: number
  status?: string
  startDate?: string
  endDate?: string
}

export interface OrderCreateDTO {
  customerId: number
  items: OrderItemDTO[]
  amount: number
  currency?: string
  deliveryDate?: string
  paymentTerms?: string
  remark?: string
}

export interface OrderItemDTO {
  productId: number
  productName: string
  quantity: number
  unitPrice: number
  remark?: string
}

export interface OrderUpdateDTO {
  id: number
  amount?: number
  deliveryDate?: string
  paymentTerms?: string
  remark?: string
}

export interface OrderStatusChangeDTO {
  orderId: number
  newStatus: string
  remark?: string
}

export interface ProductionProgressDTO {
  orderId: number
  progress: number
  description?: string
}

/**
 * 分页查询订单列表
 */
export function getOrderList(params: OrderQuery) {
  return request({
    url: '/order/page',
    method: 'get',
    params,
  })
}

/**
 * 获取订单详情
 */
export function getOrderById(id: number) {
  return request({
    url: `/order/${id}`,
    method: 'get',
  })
}

/**
 * 创建订单
 */
export function createOrder(data: OrderCreateDTO) {
  return request({
    url: '/order',
    method: 'post',
    data,
  })
}

/**
 * 更新订单
 */
export function updateOrder(data: OrderUpdateDTO) {
  return request({
    url: '/order',
    method: 'put',
    data,
  })
}

/**
 * 删除订单
 */
export function deleteOrder(ids: number[]) {
  return request({
    url: `/order/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 变更订单状态
 */
export function changeOrderStatus(data: OrderStatusChangeDTO) {
  return request({
    url: '/order/status',
    method: 'put',
    data,
  })
}

/**
 * 更新生产进度
 */
export function updateProductionProgress(data: ProductionProgressDTO) {
  return request({
    url: '/order/production-progress',
    method: 'put',
    data,
  })
}

/**
 * 生成发票
 */
export function generateInvoice(orderId: number) {
  return request({
    url: `/order/${orderId}/invoice`,
    method: 'post',
  })
}

/**
 * 导出发货清单
 */
export function exportPackingList(orderId: number) {
  return request({
    url: `/order/${orderId}/packing-list/export`,
    method: 'get',
    responseType: 'blob',
  })
}

/**
 * 导出订单Excel
 */
export function exportOrders(params: OrderQuery) {
  return request({
    url: '/order/export',
    method: 'get',
    params,
    responseType: 'blob',
  })
}
