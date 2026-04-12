package com.crm.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.customer.domain.entity.CrmCustomer;
import com.crm.customer.domain.entity.CrmFollowUpRecord;
import com.crm.customer.mapper.CrmCustomerMapper;
import com.crm.customer.mapper.CrmFollowUpRecordMapper;
import com.crm.customer.service.EmailIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件集成服务实现类 - 实现邮件与客户的自动关联
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailIntegrationServiceImpl implements EmailIntegrationService {

    private final CrmCustomerMapper customerMapper;
    private final CrmFollowUpRecordMapper followUpRecordMapper;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );

    @Override
    public String extractDomainFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()) {
            return matcher.group(1).toLowerCase();
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> findCustomersByDomain(String domain) {
        log.info("查找域名 {} 关联的客户", domain);

        if (domain == null || domain.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询邮箱域名匹配的客户
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmCustomer::getEmailDomain, domain)
                .eq(CrmCustomer::getDeleted, 0)
                .orderByDesc(CrmCustomer::getLastEmailTime)
                .orderByDesc(CrmCustomer::getCreatedTime);

        List<CrmCustomer> customers = customerMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (CrmCustomer customer : customers) {
            Map<String, Object> customerMap = convertToMap(customer);
            result.add(customerMap);
        }

        log.info("找到 {} 个关联客户", result.size());
        return result;
    }

    @Override
    public Map<String, Object> findBestMatchCustomerByEmail(String email) {
        log.info("查找邮箱 {} 的最佳匹配客户", email);

        if (email == null || email.isEmpty()) {
            return null;
        }

        String domain = extractDomainFromEmail(email);

        // 首先尝试精确匹配邮箱（从联系人表）
        // TODO: 实现联系人邮箱精确匹配

        // 然后尝试域名匹配
        if (domain != null) {
            List<Map<String, Object>> customers = findCustomersByDomain(domain);
            if (!customers.isEmpty()) {
                // 返回最近有邮件往来的客户
                return customers.get(0);
            }
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendEmailAndCreateFollowUp(Long customerId, Map<String, Object> emailData) {
        log.info("发送邮件并创建跟进记录 - 客户ID: {}", customerId);

        CrmCustomer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }

        // 提取邮件信息
        String subject = (String) emailData.getOrDefault("subject", "");
        String content = (String) emailData.getOrDefault("content", "");
        String toEmail = (String) emailData.getOrDefault("to", "");
        String ccEmail = (String) emailData.getOrDefault("cc", "");
        LocalDateTime sentTime = LocalDateTime.now();

        // 更新客户邮件统计
        customer.setEmailCount(customer.getEmailCount() == null ? 1 : customer.getEmailCount() + 1);
        customer.setLastEmailTime(sentTime);

        // 如果是客户的第一个邮箱，提取并保存域名
        if (customer.getEmailDomain() == null && toEmail != null) {
            String domain = extractDomainFromEmail(toEmail);
            customer.setEmailDomain(domain);
        }

        customerMapper.updateById(customer);

        // 创建跟进记录
        CrmFollowUpRecord followUp = new CrmFollowUpRecord();
        followUp.setCustomerId(customerId);
        followUp.setFollowType("email");
        followUp.setSubject(subject != null ? subject : "发送邮件");
        followUp.setContent(generateFollowUpSummary(emailData));
        followUp.setStartTime(sentTime);
        followUp.setStatus("completed");
        followUp.setDirection((String) emailData.getOrDefault("direction", "outbound"));
        followUp.setCreatedBy((Long) emailData.getOrDefault("userId", 1L));
        followUp.setCreatedTime(LocalDateTime.now());

        followUpRecordMapper.insert(followUp);

        // 创建活动记录
        // TODO: 实现活动记录创建

        log.info("跟进记录创建成功 - ID: {}", followUp.getId());
        return followUp.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processIncomingEmail(Map<String, Object> emailData) {
        log.info("处理接收邮件");

        String fromEmail = (String) emailData.get("from");
        String subject = (String) emailData.get("subject");
        String content = (String) emailData.get("content");
        LocalDateTime receivedTime = (LocalDateTime) emailData.getOrDefault("receivedTime", LocalDateTime.now());

        // 查找关联客户
        Map<String, Object> customer = findBestMatchCustomerByEmail(fromEmail);

        if (customer != null) {
            Long customerId = ((Number) customer.get("id")).longValue();

            // 更新客户邮件统计
            CrmCustomer crmCustomer = customerMapper.selectById(customerId);
            if (crmCustomer != null) {
                crmCustomer.setEmailCount(crmCustomer.getEmailCount() == null ? 1 : crmCustomer.getEmailCount() + 1);
                crmCustomer.setLastEmailTime(receivedTime);
                customerMapper.updateById(crmCustomer);
            }

            // 创建跟进记录
            CrmFollowUpRecord followUp = new CrmFollowUpRecord();
            followUp.setCustomerId(customerId);
            followUp.setFollowType("email");
            followUp.setSubject("收到邮件: " + (subject != null ? subject : "(无主题)"));
            followUp.setContent("发件人: " + fromEmail + "\n\n" + generateFollowUpSummary(emailData));
            followUp.setStartTime(receivedTime);
            followUp.setStatus("completed");
            followUp.setDirection("inbound");
            followUp.setCreatedBy((Long) emailData.getOrDefault("ownerId", 1L));
            followUp.setCreatedTime(LocalDateTime.now());

            followUpRecordMapper.insert(followUp);

            log.info("接收邮件已关联到客户 ID: {}, 跟进记录 ID: {}", customerId, followUp.getId());
        } else {
            log.info("未找到邮箱 {} 的关联客户", fromEmail);
            // TODO: 可以创建潜在客户或发送通知
        }
    }

    @Override
    public int processInboxEmails(Long emailSettingsId) {
        log.info("处理收件箱邮件 - 设置ID: {}", emailSettingsId);

        // TODO: 实现IMAP邮件同步和自动关联
        // 1. 连接IMAP服务器
        // 2. 获取新邮件
        // 3. 对每封邮件调用 processIncomingEmail
        // 4. 返回处理的邮件数量

        return 0;
    }

    @Override
    public void updateCustomerEmailStats(Long customerId, LocalDateTime lastEmailTime, Integer emailCount) {
        CrmCustomer customer = customerMapper.selectById(customerId);
        if (customer != null) {
            if (lastEmailTime != null) {
                customer.setLastEmailTime(lastEmailTime);
            }
            if (emailCount != null) {
                customer.setEmailCount(emailCount);
            }
            customerMapper.updateById(customer);
        }
    }

    @Override
    public List<Map<String, Object>> getCustomerEmailHistory(Long customerId, Integer limit) {
        log.info("获取客户邮件历史 - 客户ID: {}, 限制: {}", customerId, limit);

        if (limit == null || limit <= 0) {
            limit = 50;
        }

        // 查询该客户的邮件跟进记录
        LambdaQueryWrapper<CrmFollowUpRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmFollowUpRecord::getCustomerId, customerId)
                .eq(CrmFollowUpRecord::getFollowType, "email")
                .eq(CrmFollowUpRecord::getDeleted, 0)
                .orderByDesc(CrmFollowUpRecord::getStartTime)
                .last("LIMIT " + limit);

        List<CrmFollowUpRecord> followUps = followUpRecordMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (CrmFollowUpRecord followUp : followUps) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", followUp.getId());
            item.put("subject", followUp.getSubject());
            item.put("content", followUp.getContent());
            item.put("time", followUp.getStartTime());
            item.put("status", followUp.getStatus());
            result.add(item);
        }

        return result;
    }

    @Override
    public String generateFollowUpSummary(Map<String, Object> emailData) {
        StringBuilder summary = new StringBuilder();

        String from = (String) emailData.get("from");
        String to = (String) emailData.get("to");
        String cc = (String) emailData.get("cc");
        String subject = (String) emailData.get("subject");
        String direction = (String) emailData.getOrDefault("direction", "outbound");

        if ("inbound".equals(direction)) {
            summary.append("【收到邮件】\n");
            if (from != null) summary.append("发件人: ").append(from).append("\n");
        } else {
            summary.append("【发送邮件】\n");
            if (to != null) summary.append("收件人: ").append(to).append("\n");
        }

        if (cc != null && !cc.isEmpty()) {
            summary.append("抄送: ").append(cc).append("\n");
        }

        if (subject != null) {
            summary.append("主题: ").append(subject).append("\n");
        }

        summary.append("时间: ").append(LocalDateTime.now());

        return summary.toString();
    }

    @Override
    public String analyzeEmailSentiment(String content) {
        if (content == null || content.isEmpty()) {
            return "neutral";
        }

        // 简单的情感分析 - 基于关键词
        // TODO: 可以集成NLP服务进行更准确的情感分析

        String lowerContent = content.toLowerCase();

        int positiveScore = 0;
        int negativeScore = 0;

        // 积极关键词
        String[] positiveWords = {"thank", "thanks", "great", "good", "excellent", "happy", "pleased",
                "满意", "感谢", "谢谢", "好", "很好", "优秀", "高兴"};
        for (String word : positiveWords) {
            if (lowerContent.contains(word)) {
                positiveScore++;
            }
        }

        // 消极关键词
        String[] negativeWords = {"problem", "issue", "complaint", "delay", "cancel", "angry",
                "问题", "投诉", "延迟", "取消", "生气", "不满意", "失望"};
        for (String word : negativeWords) {
            if (lowerContent.contains(word)) {
                negativeScore++;
            }
        }

        if (positiveScore > negativeScore) {
            return "positive";
        } else if (negativeScore > positiveScore) {
            return "negative";
        } else {
            return "neutral";
        }
    }

    @Override
    public String generateMailtoLink(String email, String subject, String body) {
        if (email == null || email.isEmpty()) {
            return "";
        }

        StringBuilder link = new StringBuilder("mailto:");
        link.append(email);

        List<String> params = new ArrayList<>();

        if (subject != null && !subject.isEmpty()) {
            params.add("subject=" + URLEncoder.encode(subject, StandardCharsets.UTF_8));
        }

        if (body != null && !body.isEmpty()) {
            params.add("body=" + URLEncoder.encode(body, StandardCharsets.UTF_8));
        }

        if (!params.isEmpty()) {
            link.append("?");
            link.append(String.join("&", params));
        }

        return link.toString();
    }

    /**
     * 将客户实体转换为Map
     */
    private Map<String, Object> convertToMap(CrmCustomer customer) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", customer.getId());
        map.put("customerNo", customer.getCustomerNo());
        map.put("companyName", customer.getCompanyName());
        map.put("companyNameCn", customer.getCompanyNameCn());
        map.put("emailDomain", customer.getEmailDomain());
        map.put("website", customer.getWebsite());
        map.put("industry", customer.getIndustry());
        map.put("level", customer.getLevel());
        map.put("priority", customer.getPriority());
        map.put("country", customer.getCountry());
        map.put("emailCount", customer.getEmailCount());
        map.put("lastEmailTime", customer.getLastEmailTime());
        map.put("totalOrder", customer.getTotalOrder());
        return map;
    }
}
