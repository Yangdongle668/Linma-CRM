package com.crm.message.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件发送DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "邮件发送信息")
public class EmailSendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "收件人邮箱不能为空")
    @Email(message = "收件人邮箱格式不正确")
    @Schema(description = "收件人邮箱", required = true)
    private String toEmail;

    @Schema(description = "抄送邮箱列表")
    private List<String> ccEmails;

    @NotBlank(message = "邮件主题不能为空")
    @Schema(description = "邮件主题", required = true)
    private String subject;

    @Schema(description = "邮件内容(HTML)")
    private String content;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "模板变量Map")
    private java.util.Map<String, Object> templateVariables;

    @Schema(description = "附件路径列表")
    private List<String> attachments;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "业务ID")
    private Long businessId;
}
