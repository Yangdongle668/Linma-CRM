package com.crm.message.service;

import com.crm.message.domain.dto.EmailSendDTO;

/**
 * 邮件服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface EmailService {

    /**
     * 发送邮件
     *
     * @param dto 邮件发送信息
     * @return 是否成功
     */
    boolean sendEmail(EmailSendDTO dto);

    /**
     * 异步发送邮件
     *
     * @param dto 邮件发送信息
     */
    void sendEmailAsync(EmailSendDTO dto);

    /**
     * 使用模板发送邮件
     *
     * @param toEmail 收件人邮箱
     * @param templateCode 模板编码
     * @param variables 模板变量
     * @return 是否成功
     */
    boolean sendEmailWithTemplate(String toEmail, String templateCode, java.util.Map<String, Object> variables);

    /**
     * 批量发送邮件
     *
     * @param toEmails 收件人邮箱列表
     * @param subject 主题
     * @param content 内容
     * @return 发送结果Map,key为邮箱,value为是否成功
     */
    java.util.Map<String, Boolean> batchSendEmail(java.util.List<String> toEmails, String subject, String content);

    /**
     * 获取收件箱邮件列表
     *
     * @param userId 用户ID
     * @param folder 文件夹类型
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 邮件列表
     */
    java.util.List<java.util.Map<String, Object>> getInboxEmails(Long userId, String folder, Integer pageNum, Integer pageSize);

    /**
     * 获取发件箱邮件列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 邮件列表
     */
    java.util.List<java.util.Map<String, Object>> getSentEmails(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 标记邮件为已读
     *
     * @param emailId 邮件ID
     * @param userId 用户ID
     */
    void markAsRead(Long emailId, Long userId);

    /**
     * 切换邮件星标状态
     *
     * @param emailId 邮件ID
     * @param userId 用户ID
     */
    void toggleStar(Long emailId, Long userId);

    /**
     * 删除邮件
     *
     * @param emailId 邮件ID
     * @param userId 用户ID
     */
    void deleteEmail(Long emailId, Long userId);

    /**
     * 移动邮件到文件夹
     *
     * @param emailId 邮件ID
     * @param folder 目标文件夹
     * @param userId 用户ID
     */
    void moveToFolder(Long emailId, String folder, Long userId);
}
