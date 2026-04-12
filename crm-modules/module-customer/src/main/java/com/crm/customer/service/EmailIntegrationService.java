package com.crm.customer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 邮件集成服务接口 - 实现邮件与客户的自动关联
 *
 * @author CRM System
 * @since 2026-04-12
 */
public interface EmailIntegrationService {

    /**
     * 从邮箱地址提取域名
     */
    String extractDomainFromEmail(String email);

    /**
     * 根据邮箱域名查找关联客户
     */
    List<Map<String, Object>> findCustomersByDomain(String domain);

    /**
     * 根据邮箱地址查找最匹配的客户
     */
    Map<String, Object> findBestMatchCustomerByEmail(String email);

    /**
     * 发送邮件并自动创建跟进记录
     */
    Long sendEmailAndCreateFollowUp(Long customerId, Map<String, Object> emailData);

    /**
     * 接收邮件并自动关联客户
     */
    void processIncomingEmail(Map<String, Object> emailData);

    /**
     * 批量处理收件箱邮件，自动关联客户
     */
    int processInboxEmails(Long emailSettingsId);

    /**
     * 更新客户的邮件统计信息
     */
    void updateCustomerEmailStats(Long customerId, LocalDateTime lastEmailTime, Integer emailCount);

    /**
     * 获取客户的邮件往来历史
     */
    List<Map<String, Object>> getCustomerEmailHistory(Long customerId, Integer limit);

    /**
     * 生成跟进记录摘要
     */
    String generateFollowUpSummary(Map<String, Object> emailData);

    /**
     * 分析邮件情感倾向
     */
    String analyzeEmailSentiment(String content);

    /**
     * 打开系统默认邮件客户端
     */
    String generateMailtoLink(String email, String subject, String body);
}
