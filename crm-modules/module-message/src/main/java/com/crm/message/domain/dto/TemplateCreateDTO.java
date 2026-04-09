package com.crm.message.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息模板创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "消息模板创建信息")
public class TemplateCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "模板编码不能为空")
    @Schema(description = "模板编码(唯一标识)", required = true)
    private String templateCode;

    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称", required = true)
    private String templateName;

    @NotBlank(message = "模板类型不能为空")
    @Schema(description = "模板类型(email邮件/notification站内消息/sms短信)", required = true)
    private String type;

    @Schema(description = "模板标题")
    private String title;

    @NotBlank(message = "模板内容不能为空")
    @Schema(description = "模板内容(支持变量占位符,如${customerName})", required = true)
    private String content;

    @Schema(description = "邮件主题(仅邮件类型使用)")
    private String emailSubject;

    @Schema(description = "备注")
    private String remark;
}
