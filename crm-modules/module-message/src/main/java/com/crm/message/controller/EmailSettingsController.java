package com.crm.message.controller;

import com.crm.common.core.domain.Result;
import com.crm.message.domain.dto.EmailSettingsDTO;
import com.crm.message.domain.entity.SysEmailSettings;
import com.crm.message.service.EmailSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件设置控制器
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Tag(name = "邮件设置", description = "SMTP/IMAP配置管理")
@RestController
@RequestMapping("/message/email-settings")
@RequiredArgsConstructor
public class EmailSettingsController {

    private final EmailSettingsService emailSettingsService;

    @Operation(summary = "保存邮件设置")
    @PostMapping
    public Result<Long> saveSettings(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Valid @RequestBody EmailSettingsDTO dto) {
        Long id = emailSettingsService.saveSettings(userId, dto);
        return Result.success(id);
    }

    @Operation(summary = "更新邮件设置")
    @PutMapping
    public Result<Void> updateSettings(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Valid @RequestBody EmailSettingsDTO dto) {
        emailSettingsService.updateSettings(userId, dto);
        return Result.success();
    }

    @Operation(summary = "删除邮件设置")
    @DeleteMapping("/{id}")
    public Result<Void> deleteSettings(@PathVariable Long id) {
        emailSettingsService.deleteSettings(id);
        return Result.success();
    }

    @Operation(summary = "获取用户的邮件设置列表")
    @GetMapping("/list")
    public Result<List<SysEmailSettings>> getUserSettings(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        List<SysEmailSettings> settings = emailSettingsService.getUserSettings(userId);
        return Result.success(settings);
    }

    @Operation(summary = "获取默认邮件设置")
    @GetMapping("/default")
    public Result<SysEmailSettings> getDefaultSettings(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        SysEmailSettings settings = emailSettingsService.getDefaultSettings(userId);
        return Result.success(settings);
    }

    @Operation(summary = "获取设置详情")
    @GetMapping("/{id}")
    public Result<SysEmailSettings> getById(@PathVariable Long id) {
        SysEmailSettings settings = emailSettingsService.getById(id);
        return Result.success(settings);
    }

    @Operation(summary = "设置为默认账户")
    @PutMapping("/set-default")
    public Result<Void> setAsDefault(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "设置ID") @RequestParam Long settingsId) {
        emailSettingsService.setAsDefault(userId, settingsId);
        return Result.success();
    }

    @Operation(summary = "测试SMTP连接")
    @PostMapping("/test-smtp")
    public Result<Boolean> testSmtpConnection(@Valid @RequestBody EmailSettingsDTO dto) {
        boolean success = emailSettingsService.testSmtpConnection(dto);
        return Result.success(success);
    }

    @Operation(summary = "测试IMAP连接")
    @PostMapping("/test-imap")
    public Result<Boolean> testImapConnection(@Valid @RequestBody EmailSettingsDTO dto) {
        boolean success = emailSettingsService.testImapConnection(dto);
        return Result.success(success);
    }

    @Operation(summary = "手动同步邮件")
    @PostMapping("/sync/{settingsId}")
    public Result<Void> syncEmails(@PathVariable Long settingsId) {
        emailSettingsService.syncEmails(settingsId);
        return Result.success();
    }
}
