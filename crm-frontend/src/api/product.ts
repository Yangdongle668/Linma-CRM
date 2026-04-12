import request from '@/utils/request'

// 产品接口定义
export interface Product {
  id?: number
  productName: string
  productCode?: string
  categoryId?: number
  categoryName?: string
  price?: number
  costPrice?: number
  unit?: string
  specification?: string
  status?: string
  createdTime?: string
}

export interface ProductQuery {
  pageNum?: number
  pageSize?: number
  productName?: string
  productCode?: string
  categoryId?: number
  status?: string
}

export interface ProductCreateDTO {
  productName: string
  productCode?: string
  categoryId: number
  price: number
  costPrice?: number
  unit?: string
  specification?: string
  remark?: string
}

export interface ProductUpdateDTO {
  id: number
  productName?: string
  price?: number
  costPrice?: number
  unit?: string
  specification?: string
  status?: string
  remark?: string
}

/**
 * 分页查询产品列表
 */
export function getProductList(params: ProductQuery) {
  return request({
    url: '/product/page',
    method: 'get',
    params,
  })
}

/**
 * 获取产品详情
 */
export function getProductById(id: number) {
  return request({
    url: `/product/${id}`,
    method: 'get',
  })
}

/**
 * 创建产品
 */
export function createProduct(data: ProductCreateDTO) {
  return request({
    url: '/product',
    method: 'post',
    data,
  })
}

/**
 * 更新产品
 */
export function updateProduct(data: ProductUpdateDTO) {
  return request({
    url: '/product',
    method: 'put',
    data,
  })
}

/**
 * 删除产品
 */
export function deleteProduct(ids: number[]) {
  return request({
    url: '/product',
    method: 'delete',
    data: ids,
  })
}

/**
 * 生成产品编号
 */
export function generateProductNo() {
  return request({
    url: '/product/generate-no',
    method: 'get',
  })
}

/**
 * 获取产品价格信息
 */
export function getProductPrice(id: number) {
  return request({
    url: `/product/${id}/price`,
    method: 'get',
  })
}

/**
 * 更新产品价格
 */
export function updateProductPrice(id: number, price: number) {
  return request({
    url: `/product/${id}/price`,
    method: 'put',
    data: { price },
  })
}
