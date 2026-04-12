package com.crm.message.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 邮件设置DTO
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Data
@Schema(description = "邮件设置")
public class EmailSettingsDTO {

    @Schema(description = "设置ID(更新时必填)")
    private Long id;

    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址", example = "user@example.com")
    private String emailAddress;

    @Schema(description = "显示名称", example = "张三")
    private String displayName;

    // SMTP Configuration
    @NotBlank(message = "SMTP服务器不能为空")
    @Schema(description = "SMTP服务器", example = "smtp.qq.com")
    private String smtpHost;

    @Schema(description = "SMTP端口", example = "465")
    private Integer smtpPort;

    @NotBlank(message = "SMTP用户名不能为空")
    @Schema(description = "SMTP用户名", example = "user@qq.com")
    private String smtpUsername;

    @NotBlank(message = "SMTP密码/授权码不能为空")
    @Schema(description = "SMTP密码/授权码")
    private String smtpPassword;

    @Schema(description = "SMTP加密方式", example = "ssl", allowableValues = {"none", "ssl", "tls"})
    private String smtpEncryption;

    @Schema(description = "是否需要SMTP认证", example = "true")
    private Boolean smtpAuth;

    // IMAP Configuration
    @NotBlank(message = "IMAP服务器不能为空")
    @Schema(description = "IMAP服务器", example = "imap.qq.com")
    private String imapHost;

    @Schema(description = "IMAP端口", example = "993")
    private Integer imapPort;

    @NotBlank(message = "IMAP用户名不能为空")
    @Schema(description = "IMAP用户名", example = "user@qq.com")
    private String imapUsername;

    @NotBlank(message = "IMAP密码/授权码不能为空")
    @Schema(description = "IMAP密码/授权码")
    private String imapPassword;

    @Schema(description = "IMAP加密方式", example = "ssl", allowableValues = {"none", "ssl", "tls"})
    private String imapEncryption;

    // Settings
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;

    @Schema(description = "是否为默认账户", example = "true")
    private Boolean isDefault;

    @Schema(description = "同步间隔(分钟)", example = "5")
    private Integer syncInterval;

    @Schema(description = "保留服务器副本", example = "true")
    private Boolean keepServerCopy;

    @Schema(description = "邮件签名")
    private String emailSignature;

    @Schema(description = "每页显示数量", example = "20")
    private Integer pageSize;
}
