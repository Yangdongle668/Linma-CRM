import request from '@/utils/request'

export interface Contract {
  id?: number
  contractNo?: string
  customerId?: number
  customerName?: string
  amount?: number
  currency?: string
  status?: string
  signDate?: string
  startDate?: string
  endDate?: string
  remark?: string
  createdTime?: string
}

export interface ContractQuery {
  pageNum?: number
  pageSize?: number
  contractNo?: string
  customerName?: string
  status?: string
  startDate?: string
  endDate?: string
}

export interface ContractCreateDTO {
  customerId: number
  amount: number
  currency?: string
  signDate?: string
  startDate?: string
  endDate?: string
  paymentTerms?: string
  deliveryTerms?: string
  remark?: string
}

export interface ContractUpdateDTO {
  id: number
  amount?: number
  currency?: string
  signDate?: string
  startDate?: string
  endDate?: string
  status?: string
  remark?: string
}

export interface ContractApprovalDTO {
  contractId: number
  approved: boolean
  remark?: string
}

/**
 * 分页查询合同列表
 */
export function getContractList(params: ContractQuery) {
  return request({
    url: '/contract/page',
    method: 'get',
    params,
  })
}

/**
 * 获取合同详情
 */
export function getContractById(id: number) {
  return request({
    url: `/contract/${id}`,
    method: 'get',
  })
}

/**
 * 创建合同
 */
export function createContract(data: ContractCreateDTO) {
  return request({
    url: '/contract',
    method: 'post',
    data,
  })
}

/**
 * 更新合同
 */
export function updateContract(data: ContractUpdateDTO) {
  return request({
    url: '/contract',
    method: 'put',
    data,
  })
}

/**
 * 删除合同
 */
export function deleteContract(ids: number[]) {
  return request({
    url: '/contract',
    method: 'delete',
    data: ids,
  })
}

/**
 * 提交审批
 */
export function submitForApproval(id: number) {
  return request({
    url: `/contract/${id}/submit-approval`,
    method: 'post',
  })
}

/**
 * 审批合同
 */
export function approveContract(data: ContractApprovalDTO) {
  return request({
    url: '/contract/approve',
    method: 'post',
    data,
  })
}

/**
 * 签署合同
 */
export function signContract(id: number, signDate: string) {
  return request({
    url: `/contract/${id}/sign`,
    method: 'put',
    data: { signDate },
  })
}

/**
 * 导出合同PDF
 */
export function exportContractPdf(id: number) {
  return request({
    url: `/contract/${id}/pdf`,
    method: 'get',
    responseType: 'blob',
  })
}

/**
 * 生成合同编号
 */
export function generateContractNo() {
  return request({
    url: '/contract/generate-no',
    method: 'get',
  })
}

// ========== 合同模板相关 ==========

export interface ContractTemplate {
  id?: number
  templateName: string
  templateCode?: string
  templateType?: string
  content: string
  status?: string
  remark?: string
  createdTime?: string
}

/**
 * 获取合同模板列表
 */
export function getTemplateList(params?: any) {
  return request({
    url: '/contract/template/list',
    method: 'get',
    params,
  })
}

/**
 * 获取合同模板详情
 */
export function getTemplateById(id: number) {
  return request({
    url: `/contract/template/${id}`,
    method: 'get',
  })
}

/**
 * 创建合同模板
 */
export function createTemplate(data: ContractTemplate) {
  return request({
    url: '/contract/template',
    method: 'post',
    data,
  })
}

/**
 * 更新合同模板
 */
export function updateTemplate(data: ContractTemplate) {
  return request({
    url: '/contract/template',
    method: 'put',
    data,
  })
}

/**
 * 删除合同模板
 */
export function deleteTemplate(ids: number[]) {
  return request({
    url: '/contract/template',
    method: 'delete',
    data: ids,
  })
}
