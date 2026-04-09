package com.crm.message.controller;

import com.crm.common.core.domain.Result;
import com.crm.message.domain.dto.EmailSendDTO;
import com.crm.message.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 邮件服务控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "邮件服务", description = "邮件发送、模板邮件、批量发送等功能")
@RestController
@RequestMapping("/message/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "发送邮件")
    @PostMapping("/send")
    public Result<Boolean> sendEmail(
            @Parameter(description = "邮件信息") @Valid @RequestBody EmailSendDTO dto) {
        boolean success = emailService.sendEmail(dto);
        return Result.success(success);
    }

    @Operation(summary = "异步发送邮件")
    @PostMapping("/send-async")
    public Result<Void> sendEmailAsync(
            @Parameter(description = "邮件信息") @Valid @RequestBody EmailSendDTO dto) {
        emailService.sendEmailAsync(dto);
        return Result.success();
    }

    @Operation(summary = "使用模板发送邮件")
    @PostMapping("/send-with-template")
    public Result<Boolean> sendEmailWithTemplate(
            @Parameter(description = "收件人邮箱") @RequestParam String toEmail,
            @Parameter(description = "模板编码") @RequestParam String templateCode,
            @Parameter(description = "模板变量") @RequestBody Map<String, Object> variables) {
        boolean success = emailService.sendEmailWithTemplate(toEmail, templateCode, variables);
        return Result.success(success);
    }

    @Operation(summary = "批量发送邮件")
    @PostMapping("/batch-send")
    public Result<Map<String, Boolean>> batchSendEmail(
            @Parameter(description = "收件人邮箱列表") @RequestBody List<String> toEmails,
            @Parameter(description = "主题") @RequestParam String subject,
            @Parameter(description = "内容") @RequestParam String content) {
        Map<String, Boolean> results = emailService.batchSendEmail(toEmails, subject, content);
        return Result.success(results);
    }
}
