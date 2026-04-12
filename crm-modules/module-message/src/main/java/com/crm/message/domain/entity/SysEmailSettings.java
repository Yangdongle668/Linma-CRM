package com.crm.message.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户邮件设置实体 - 存储SMTP/IMAP配置
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Data
@TableName("sys_email_settings")
@Schema(description = "用户邮件设置")
public class SysEmailSettings {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "邮箱地址")
    private String emailAddress;

    @Schema(description = "显示名称")
    private String displayName;

    // SMTP Configuration
    @Schema(description = "SMTP服务器地址")
    private String smtpHost;

    @Schema(description = "SMTP端口")
    private Integer smtpPort;

    @Schema(description = "SMTP用户名")
    private String smtpUsername;

    @Schema(description = "SMTP密码/授权码")
    private String smtpPassword;

    @Schema(description = "SMTP加密方式(none/ssl/tls)")
    private String smtpEncryption;

    @Schema(description = "是否需要SMTP认证")
    private Boolean smtpAuth;

    // IMAP Configuration
    @Schema(description = "IMAP服务器地址")
    private String imapHost;

    @Schema(description = "IMAP端口")
    private Integer imapPort;

    @Schema(description = "IMAP用户名")
    private String imapUsername;

    @Schema(description = "IMAP密码/授权码")
    private String imapPassword;

    @Schema(description = "IMAP加密方式(none/ssl/tls)")
    private String imapEncryption;

    // Settings
    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "是否为默认账户")
    private Boolean isDefault;

    @Schema(description = "同步间隔(分钟)")
    private Integer syncInterval;

    @Schema(description = "保留服务器副本")
    private Boolean keepServerCopy;

    @Schema(description = "自动添加签名")
    private String emailSignature;

    @Schema(description = "每页显示数量")
    private Integer pageSize;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @TableLogic
    @Schema(description = "逻辑删除")
    private Integer deleted;
}
