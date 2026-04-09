package com.crm.quotation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 报价单邮件发送DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "报价单邮件发送请求")
public class QuotationEmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "报价单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报价单ID不能为空")
    private Long quotationId;

    @Schema(description = "收件人邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收件人邮箱不能为空")
    private String recipientEmail;

    @Schema(description = "收件人姓名")
    private String recipientName;

    @Schema(description = "邮件主题")
    private String subject;

    @Schema(description = "邮件正文")
    private String body;

    @Schema(description = "是否附加PDF文件", defaultValue = "true")
    private Boolean attachPdf = true;

    @Schema(description = "语言(en/cn)", defaultValue = "en")
    private String language = "en";
}
