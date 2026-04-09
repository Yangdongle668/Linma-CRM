package com.crm.message.service.impl;

import com.crm.message.domain.dto.EmailSendDTO;
import com.crm.message.domain.entity.MsgEmailLog;
import com.crm.message.mapper.MsgEmailLogMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
