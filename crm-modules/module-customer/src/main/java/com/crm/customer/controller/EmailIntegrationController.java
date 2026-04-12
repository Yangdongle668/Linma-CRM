package com.crm.customer.controller;

import com.crm.common.core.domain.Result;
import com.crm.customer.service.EmailIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 邮件集成控制器 - 实现邮件与客户的自动关联
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Tag(name = "邮件集成", description = "邮件与客户关联、自动跟进记录生成")
@RestController
@RequestMapping("/customer/email-integration")
@RequiredArgsConstructor
public class EmailIntegrationController {

    private final EmailIntegrationService emailIntegrationService;

    @Operation(summary = "从邮箱提取域名")
    @GetMapping("/extract-domain")
    public Result<String> extractDomain(
            @Parameter(description = "邮箱地址") @RequestParam String email) {
        String domain = emailIntegrationService.extractDomainFromEmail(email);
        return Result.success(domain);
    }

    @Operation(summary = "根据域名查找关联客户")
    @GetMapping("/find-by-domain")
    public Result<List<Map<String, Object>>> findByDomain(
            @Parameter(description = "邮箱域名") @RequestParam String domain) {
        List<Map<String, Object>> customers = emailIntegrationService.findCustomersByDomain(domain);
        return Result.success(customers);
    }

    @Operation(summary = "根据邮箱查找最佳匹配客户")
    @GetMapping("/find-by-email")
    public Result<Map<String, Object>> findByEmail(
            @Parameter(description = "邮箱地址") @RequestParam String email) {
        Map<String, Object> customer = emailIntegrationService.findBestMatchCustomerByEmail(email);
        return Result.success(customer);
    }

    @Operation(summary = "发送邮件并创建跟进记录")
    @PostMapping("/send-and-follow-up")
    public Result<Long> sendAndFollowUp(
            @Parameter(description = "客户ID") @RequestParam Long customerId,
            @RequestBody Map<String, Object> emailData) {
        Long followUpId = emailIntegrationService.sendEmailAndCreateFollowUp(customerId, emailData);
        return Result.success(followUpId);
    }

    @Operation(summary = "处理接收邮件（自动关联客户）")
    @PostMapping("/process-incoming")
    public Result<Void> processIncoming(@RequestBody Map<String, Object> emailData) {
        emailIntegrationService.processIncomingEmail(emailData);
        return Result.success();
    }

    @Operation(summary = "获取客户邮件历史")
    @GetMapping("/email-history/{customerId}")
    public Result<List<Map<String, Object>>> getEmailHistory(
            @PathVariable Long customerId,
            @Parameter(description = "数量限制") @RequestParam(required = false, defaultValue = "50") Integer limit) {
        List<Map<String, Object>> history = emailIntegrationService.getCustomerEmailHistory(customerId, limit);
        return Result.success(history);
    }

    @Operation(summary = "分析邮件情感")
    @PostMapping("/analyze-sentiment")
    public Result<String> analyzeSentiment(
            @Parameter(description = "邮件内容") @RequestBody String content) {
        String sentiment = emailIntegrationService.analyzeEmailSentiment(content);
        return Result.success(sentiment);
    }

    @Operation(summary = "生成mailto链接")
    @GetMapping("/mailto-link")
    public Result<String> generateMailtoLink(
            @Parameter(description = "收件人邮箱") @RequestParam String email,
            @Parameter(description = "主题") @RequestParam(required = false) String subject,
            @Parameter(description = "正文") @RequestParam(required = false) String body) {
        String link = emailIntegrationService.generateMailtoLink(email, subject, body);
        return Result.success(link);
    }

    @Operation(summary = "打开系统邮件客户端")
    @GetMapping("/open-mail-client")
    public Result<Map<String, String>> openMailClient(
            @Parameter(description = "收件人邮箱") @RequestParam String email,
            @Parameter(description = "主题") @RequestParam(required = false) String subject,
            @Parameter(description = "正文") @RequestParam(required = false) String body) {
        String mailtoLink = emailIntegrationService.generateMailtoLink(email, subject, body);

        Map<String, String> response = new java.util.HashMap<>();
        response.put("mailtoLink", mailtoLink);
        response.put("instruction", "点击链接打开系统默认邮件客户端");

        return Result.success(response);
    }
}
