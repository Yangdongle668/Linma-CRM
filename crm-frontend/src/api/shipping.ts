import request from '@/utils/request'

export interface Shipping {
  id?: number
  shippingNo?: string
  orderId?: number
  orderNo?: string
  customerId?: number
  customerName?: string
  trackingNo?: string
  carrier?: string
  carrierWebsite?: string
  status?: string
  shipDate?: string
  estimatedArrival?: string
  actualArrival?: string
  origin?: string
  destination?: string
  weight?: number
  volume?: number
  freightCost?: number
  remark?: string
  createdTime?: string
}

export interface ShippingQuery {
  pageNum?: number
  pageSize?: number
  shippingNo?: string
  trackingNo?: string
  status?: string
  orderId?: number
  startDate?: string
  endDate?: string
}

export interface ShippingCreateDTO {
  orderId: number
  carrier: string
  trackingNo?: string
  shipDate?: string
  estimatedArrival?: string
  origin?: string
  destination?: string
  weight?: number
  volume?: number
  freightCost?: number
  remark?: string
}

export interface ShippingUpdateDTO {
  id: number
  trackingNo?: string
  status?: string
  shipDate?: string
  estimatedArrival?: string
  actualArrival?: string
  freightCost?: number
  remark?: string
}

export interface TrackingRecord {
  id?: number
  shippingId?: number
  trackingTime: string
  location?: string
  status: string
  description: string
}

/**
 * 分页查询发货列表
 */
export function getShippingList(params: ShippingQuery) {
  return request({
    url: '/shipping/page',
    method: 'get',
    params,
  })
}

/**
 * 获取发货详情
 */
export function getShippingById(id: number) {
  return request({
    url: `/shipping/${id}`,
    method: 'get',
  })
}

/**
 * 创建发货记录
 */
export function createShipping(data: ShippingCreateDTO) {
  return request({
    url: '/shipping',
    method: 'post',
    data,
  })
}

/**
 * 更新发货记录
 */
export function updateShipping(data: ShippingUpdateDTO) {
  return request({
    url: '/shipping',
    method: 'put',
    data,
  })
}

/**
 * 删除发货记录
 */
export function deleteShipping(ids: number[]) {
  return request({
    url: '/shipping',
    method: 'delete',
    data: ids,
  })
}

/**
 * 更新发货状态
 */
export function updateShippingStatus(id: number, status: string) {
  return request({
    url: `/shipping/${id}/status`,
    method: 'put',
    data: { status },
  })
}

// ========== 物流追踪相关 ==========

/**
 * 添加追踪记录
 */
export function addTrackingRecord(data: TrackingRecord) {
  return request({
    url: '/shipping/tracking',
    method: 'post',
    data,
  })
}

/**
 * 获取追踪记录列表
 */
export function getTrackingRecords(shippingId: number) {
  return request({
    url: `/shipping/tracking/${shippingId}`,
    method: 'get',
  })
}

/**
 * 删除追踪记录
 */
export function deleteTrackingRecord(id: number) {
  return request({
    url: `/shipping/tracking/${id}`,
    method: 'delete',
  })
}

/**
 * 生成发货单号
 */
export function generateShippingNo() {
  return request({
    url: '/shipping/generate-no',
    method: 'get',
  })
}
