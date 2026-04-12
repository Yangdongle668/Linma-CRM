package com.crm.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.message.domain.dto.EmailSendDTO;
import com.crm.message.domain.entity.MsgEmail;
import com.crm.message.domain.entity.MsgEmailLog;
import com.crm.message.mapper.MsgEmailLogMapper;
import com.crm.message.mapper.MsgEmailMapper;
import com.crm.message.service.EmailService;
import com.crm.message.service.TemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 邮件服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateService templateService;
    private final MsgEmailLogMapper emailLogMapper;
    private final MsgEmailMapper emailMapper;

    // TODO: 从配置文件读取发件人邮箱
    private static final String FROM_EMAIL = "noreply@crm-system.com";

    @Override
    public boolean sendEmail(EmailSendDTO dto) {
        log.info("发送邮件 - 收件人:{}, 主题:{}", dto.getToEmail(), dto.getSubject());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(FROM_EMAIL);
            helper.setTo(dto.getToEmail());

            // 抄送
            if (dto.getCcEmails() != null && !dto.getCcEmails().isEmpty()) {
                helper.setCc(dto.getCcEmails().toArray(new String[0]));
            }

            helper.setSubject(dto.getSubject());
            helper.setText(dto.getContent(), true); // HTML内容

            // 添加附件
            if (dto.getAttachments() != null) {
                for (String attachmentPath : dto.getAttachments()) {
                    helper.addAttachment(new File(attachmentPath).getName(), new File(attachmentPath));
                }
            }

            mailSender.send(message);

            // 记录日志
            saveEmailLog(dto, "sent", null);

            log.info("邮件发送成功 - 收件人:{}", dto.getToEmail());
            return true;

        } catch (MessagingException e) {
            log.error("邮件发送失败 - 收件人:{}, 错误:{}", dto.getToEmail(), e.getMessage());
            saveEmailLog(dto, "failed", e.getMessage());
            return false;
        }
    }

    @Override
    @Async
    public void sendEmailAsync(EmailSendDTO dto) {
        log.info("异步发送邮件 - 收件人:{}", dto.getToEmail());
        sendEmail(dto);
    }

    @Override
    public boolean sendEmailWithTemplate(String toEmail, String templateCode, Map<String, Object> variables) {
        log.info("使用模板发送邮件 - 收件人:{}, 模板:{}", toEmail, templateCode);

        // 获取模板
        var template = templateService.getByCode(templateCode);
        if (template == null) {
            log.error("模板不存在 - 模板编码:{}", templateCode);
            return false;
        }

        // 渲染模板
        String content = templateService.renderTemplate(templateCode, variables);
        String subject = template.getEmailSubject() != null ? template.getEmailSubject() : template.getTitle();

        // 构建发送DTO
        EmailSendDTO dto = new EmailSendDTO();
        dto.setToEmail(toEmail);
        dto.setSubject(subject);
        dto.setContent(content);
        dto.setTemplateId(template.getId());

        return sendEmail(dto);
    }

    @Override
    public Map<String, Boolean> batchSendEmail(List<String> toEmails, String subject, String content) {
        log.info("批量发送邮件 - 数量:{}", toEmails.size());

        Map<String, Boolean> results = new HashMap<>();

        for (String email : toEmails) {
            EmailSendDTO dto = new EmailSendDTO();
            dto.setToEmail(email);
            dto.setSubject(subject);
            dto.setContent(content);

            boolean success = sendEmail(dto);
            results.put(email, success);
        }

        return results;
    }

    /**
     * 保存邮件日志
     */
    private void saveEmailLog(EmailSendDTO dto, String status, String errorMsg) {
        MsgEmailLog log = new MsgEmailLog();
        log.setSubject(dto.getSubject());
        log.setToEmail(dto.getToEmail());
        log.setFromEmail(FROM_EMAIL);
        log.setContent(dto.getContent());
        log.setStatus(status);
        log.setErrorMsg(errorMsg);
        log.setTemplateId(dto.getTemplateId());
        log.setBusinessType(dto.getBusinessType());
        log.setBusinessId(dto.getBusinessId());
        log.setSendTime(LocalDateTime.now());

        emailLogMapper.insert(log);
    }

    @Override
    public List<Map<String, Object>> getInboxEmails(Long userId, String folder, Integer pageNum, Integer pageSize) {
        log.info("获取收件箱邮件 - 用户ID:{}, 文件夹:{}", userId, folder);

        if (folder == null || folder.isEmpty()) {
            folder = "inbox";
        }

        LambdaQueryWrapper<MsgEmail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgEmail::getUserId, userId)
                .eq(MsgEmail::getDirection, "inbound")
                .eq(MsgEmail::getFolder, folder)
                .orderByDesc(MsgEmail::getMessageTime);

        // Pagination
        int start = (pageNum - 1) * pageSize;
        wrapper.last("LIMIT " + start + ", " + pageSize);

        List<MsgEmail> emails = emailMapper.selectList(wrapper);
        return emails.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getSentEmails(Long userId, Integer pageNum, Integer pageSize) {
        log.info("获取发件箱邮件 - 用户ID:{}", userId);

        LambdaQueryWrapper<MsgEmail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgEmail::getUserId, userId)
                .eq(MsgEmail::getDirection, "outbound")
                .eq(MsgEmail::getFolder, "sent")
                .orderByDesc(MsgEmail::getMessageTime);

        // Pagination
        int start = (pageNum - 1) * pageSize;
        wrapper.last("LIMIT " + start + ", " + pageSize);

        List<MsgEmail> emails = emailMapper.selectList(wrapper);
        return emails.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long emailId, Long userId) {
        log.info("标记邮件为已读 - 邮件ID:{}", emailId);
        
        MsgEmail email = emailMapper.selectById(emailId);
        if (email != null && email.getUserId().equals(userId)) {
            email.setIsRead(true);
            emailMapper.updateById(email);
        }
    }

    @Override
    public void toggleStar(Long emailId, Long userId) {
        log.info("切换邮件星标 - 邮件ID:{}", emailId);
        
        MsgEmail email = emailMapper.selectById(emailId);
        if (email != null && email.getUserId().equals(userId)) {
            email.setIsStarred(!Boolean.TRUE.equals(email.getIsStarred()));
            emailMapper.updateById(email);
        }
    }

    @Override
    public void deleteEmail(Long emailId, Long userId) {
        log.info("删除邮件 - 邮件ID:{}", emailId);
        
        MsgEmail email = emailMapper.selectById(emailId);
        if (email != null && email.getUserId().equals(userId)) {
            email.setDeleted(1);
            emailMapper.updateById(email);
        }
    }

    @Override
    public void moveToFolder(Long emailId, String folder, Long userId) {
        log.info("移动邮件到文件夹 - 邮件ID:{}, 文件夹:{}", emailId, folder);
        
        MsgEmail email = emailMapper.selectById(emailId);
        if (email != null && email.getUserId().equals(userId)) {
            email.setFolder(folder);
            emailMapper.updateById(email);
        }
    }

    /**
     * 将邮件实体转换为Map
     */
    private Map<String, Object> convertToMap(MsgEmail email) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", email.getId());
        map.put("from", email.getFromEmail());
        map.put("fromName", email.getFromName());
        map.put("to", email.getToEmail());
        map.put("subject", email.getSubject() != null ? email.getSubject() : "(无主题)");
        map.put("preview", email.getPreview());
        map.put("content", email.getContent());
        map.put("date", email.getMessageTime() != null 
                ? email.getMessageTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) 
                : LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        map.put("read", Boolean.TRUE.equals(email.getIsRead()));
        map.put("starred", Boolean.TRUE.equals(email.getIsStarred()));
        map.put("hasAttachment", Boolean.TRUE.equals(email.getHasAttachment()));
        map.put("attachments", email.getAttachments());
        map.put("folder", email.getFolder());
        map.put("direction", email.getDirection());
        return map;
    }
}
