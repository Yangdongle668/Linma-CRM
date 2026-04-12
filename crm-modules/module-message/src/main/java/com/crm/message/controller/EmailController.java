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

    @Operation(summary = "获取收件箱邮件列表")
    @GetMapping("/inbox")
    public Result<List<Map<String, Object>>> getInboxEmails(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "文件夹类型") @RequestParam(required = false, defaultValue = "inbox") String folder,
            @Parameter(description = "页码") @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        List<Map<String, Object>> emails = emailService.getInboxEmails(userId, folder, pageNum, pageSize);
        return Result.success(emails);
    }

    @Operation(summary = "获取发件箱邮件列表")
    @GetMapping("/sent")
    public Result<List<Map<String, Object>>> getSentEmails(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "页码") @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        List<Map<String, Object>> emails = emailService.getSentEmails(userId, pageNum, pageSize);
        return Result.success(emails);
    }

    @Operation(summary = "标记邮件为已读")
    @PutMapping("/{emailId}/read")
    public Result<Void> markAsRead(
            @PathVariable Long emailId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        emailService.markAsRead(emailId, userId);
        return Result.success();
    }

    @Operation(summary = "切换邮件星标状态")
    @PutMapping("/{emailId}/star")
    public Result<Void> toggleStar(
            @PathVariable Long emailId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        emailService.toggleStar(emailId, userId);
        return Result.success();
    }

    @Operation(summary = "删除邮件")
    @DeleteMapping("/{emailId}")
    public Result<Void> deleteEmail(
            @PathVariable Long emailId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        emailService.deleteEmail(emailId, userId);
        return Result.success();
    }

    @Operation(summary = "移动邮件到文件夹")
    @PutMapping("/{emailId}/move")
    public Result<Void> moveToFolder(
            @PathVariable Long emailId,
            @Parameter(description = "目标文件夹") @RequestParam String folder,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        emailService.moveToFolder(emailId, folder, userId);
        return Result.success();
    }
}
