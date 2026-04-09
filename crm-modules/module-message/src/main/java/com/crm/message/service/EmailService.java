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
}
