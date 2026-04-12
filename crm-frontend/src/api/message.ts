import request from '@/utils/request'

// 邮件发送接口定义
export interface EmailSendDTO {
  to: string
  cc?: string
  bcc?: string
  subject: string
  content: string
  isHtml?: boolean
  attachments?: string[]
}

export interface TemplateCreateDTO {
  templateCode: string
  templateName: string
  templateType?: string
  subject: string
  content: string
  language?: string
  remark?: string
}

export interface Template {
  id?: number
  templateCode: string
  templateName: string
  templateType?: string
  subject: string
  content: string
  language?: string
  status?: string
  remark?: string
  createdTime?: string
}

export interface NotificationQuery {
  pageNum?: number
  pageSize?: number
  type?: string
  status?: string
  userId?: number
}

export interface Notification {
  id?: number
  userId?: number
  type?: string
  title: string
  content: string
  status?: string
  createdTime?: string
}

/**
 * 发送邮件
 */
export function sendEmail(data: EmailSendDTO) {
  return request({
    url: '/message/email/send',
    method: 'post',
    data,
  })
}

/**
 * 异步发送邮件
 */
export function sendEmailAsync(data: EmailSendDTO) {
  return request({
    url: '/message/email/send-async',
    method: 'post',
    data,
  })
}

/**
 * 使用模板发送邮件
 */
export function sendEmailWithTemplate(toEmail: string, templateCode: string, variables: Record<string, any>) {
  return request({
    url: '/message/email/send-with-template',
    method: 'post',
    params: { toEmail, templateCode },
    data: variables,
  })
}

/**
 * 批量发送邮件
 */
export function batchSendEmail(toEmails: string[], subject: string, content: string) {
  return request({
    url: '/message/email/batch-send',
    method: 'post',
    params: { subject, content },
    data: toEmails,
  })
}

/**
 * 创建邮件模板
 */
export function createTemplate(data: TemplateCreateDTO) {
  return request({
    url: '/message/template',
    method: 'post',
    data,
  })
}

/**
 * 更新邮件模板
 */
export function updateTemplate(data: Template) {
  return request({
    url: '/message/template',
    method: 'put',
    data,
  })
}

/**
 * 删除邮件模板
 */
export function deleteTemplate(ids: number[]) {
  return request({
    url: `/message/template/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 获取模板列表
 */
export function getTemplateList(params?: any) {
  return request({
    url: '/message/template/list',
    method: 'get',
    params,
  })
}

/**
 * 获取模板详情
 */
export function getTemplateById(id: number) {
  return request({
    url: `/message/template/${id}`,
    method: 'get',
  })
}

/**
 * 获取通知列表
 */
export function getNotificationList(params: NotificationQuery) {
  return request({
    url: '/message/notification/list',
    method: 'get',
    params,
  })
}

/**
 * 标记通知为已读
 */
export function markNotificationAsRead(id: number) {
  return request({
    url: `/message/notification/${id}/read`,
    method: 'put',
  })
}

/**
 * 批量标记通知为已读
 */
export function batchMarkAsRead(ids: number[]) {
  return request({
    url: '/message/notification/batch-read',
    method: 'put',
    data: ids,
  })
}

/**
 * 删除通知
 */
export function deleteNotification(ids: number[]) {
  return request({
    url: `/message/notification/${ids.join(',')}`,
    method: 'delete',
  })
}

/**
 * 获取未读通知数量
 */
export function getUnreadNotificationCount() {
  return request({
    url: '/message/notification/unread-count',
    method: 'get',
  })
}

// ==================== 邮箱设置相关接口 ====================

export interface EmailSettingsDTO {
  id?: number
  emailAddress: string
  displayName?: string
  smtpHost: string
  smtpPort: number
  smtpUsername: string
  smtpPassword: string
  smtpEncryption?: string
  imapHost: string
  imapPort: number
  imapUsername: string
  imapPassword: string
  imapEncryption?: string
  syncInterval?: number
  keepServerCopy?: boolean
  isDefault?: boolean
}

export interface EmailSettings {
  id?: number
  userId?: number
  emailAddress: string
  displayName?: string
  smtpHost: string
  smtpPort: number
  smtpUsername: string
  smtpPassword: string
  smtpEncryption?: string
  imapHost: string
  imapPort: number
  imapUsername: string
  imapPassword: string
  imapEncryption?: string
  syncInterval?: number
  keepServerCopy?: boolean
  isDefault?: boolean
  status?: string
  lastSyncTime?: string
  createdTime?: string
}

/**
 * 保存邮件设置
 */
export function saveEmailSettings(userId: number, data: EmailSettingsDTO) {
  return request({
    url: '/message/email-settings',
    method: 'post',
    params: { userId },
    data,
  })
}

/**
 * 更新邮件设置
 */
export function updateEmailSettings(userId: number, data: EmailSettingsDTO) {
  return request({
    url: '/message/email-settings',
    method: 'put',
    params: { userId },
    data,
  })
}

/**
 * 删除邮件设置
 */
export function deleteEmailSettings(id: number) {
  return request({
    url: `/message/email-settings/${id}`,
    method: 'delete',
  })
}

/**
 * 获取用户的邮件设置列表
 */
export function getEmailSettingsList(userId: number) {
  return request({
    url: '/message/email-settings/list',
    method: 'get',
    params: { userId },
  })
}

/**
 * 获取默认邮件设置
 */
export function getDefaultEmailSettings(userId: number) {
  return request({
    url: '/message/email-settings/default',
    method: 'get',
    params: { userId },
  })
}

/**
 * 获取设置详情
 */
export function getEmailSettingsById(id: number) {
  return request({
    url: `/message/email-settings/${id}`,
    method: 'get',
  })
}

/**
 * 设置为默认账户
 */
export function setEmailAsDefault(userId: number, settingsId: number) {
  return request({
    url: '/message/email-settings/set-default',
    method: 'put',
    params: { userId, settingsId },
  })
}

/**
 * 测试SMTP连接
 */
export function testSmtpConnection(data: EmailSettingsDTO) {
  return request({
    url: '/message/email-settings/test-smtp',
    method: 'post',
    data,
  })
}

/**
 * 测试IMAP连接
 */
export function testImapConnection(data: EmailSettingsDTO) {
  return request({
    url: '/message/email-settings/test-imap',
    method: 'post',
    data,
  })
}

/**
 * 手动同步邮件
 */
export function syncEmails(settingsId: number) {
  return request({
    url: `/message/email-settings/sync/${settingsId}`,
    method: 'post',
  })
}

/**
 * 获取收件箱邮件列表
 */
export function getInboxEmails(userId: number, folder?: string, pageNum?: number, pageSize?: number) {
  return request({
    url: '/message/email/inbox',
    method: 'get',
    params: { userId, folder, pageNum, pageSize },
  })
}

/**
 * 获取发件箱邮件列表
 */
export function getSentEmails(userId: number, pageNum?: number, pageSize?: number) {
  return request({
    url: '/message/email/sent',
    method: 'get',
    params: { userId, pageNum, pageSize },
  })
}

/**
 * 标记邮件为已读
 */
export function markEmailAsRead(emailId: number, userId: number) {
  return request({
    url: `/message/email/${emailId}/read`,
    method: 'put',
    params: { userId },
  })
}

/**
 * 切换邮件星标
 */
export function toggleEmailStar(emailId: number, userId: number) {
  return request({
    url: `/message/email/${emailId}/star`,
    method: 'put',
    params: { userId },
  })
}

/**
 * 删除邮件
 */
export function deleteEmail(emailId: number, userId: number) {
  return request({
    url: `/message/email/${emailId}`,
    method: 'delete',
    params: { userId },
  })
}

/**
 * 移动邮件到文件夹
 */
export function moveEmailToFolder(emailId: number, folder: string, userId: number) {
  return request({
    url: `/message/email/${emailId}/move`,
    method: 'put',
    params: { folder, userId },
  })
}
