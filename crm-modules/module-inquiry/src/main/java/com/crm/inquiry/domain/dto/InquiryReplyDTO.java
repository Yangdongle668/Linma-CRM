package com.crm.inquiry.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 询盘回复DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "询盘回复DTO")
public class InquiryReplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 询盘ID
     */
    @Schema(description = "询盘ID", example = "1")
    @NotNull(message = "询盘ID不能为空")
    private Long inquiryId;

    /**
     * 回复内容
     */
    @Schema(description = "回复内容", example = "Thank you for your inquiry...")
    @NotBlank(message = "回复内容不能为空")
    private String replyContent;

    /**
     * 回复方式(email/phone/whatsapp/wechat)
     */
    @Schema(description = "回复方式(email/phone/whatsapp/wechat)", example = "email")
    private String replyMethod;

    /**
     * 附件URL(多个用逗号分隔)
     */
    @Schema(description = "附件URL")
    private String attachments;

    /**
     * 是否标记为已报价
     */
    @Schema(description = "是否标记为已报价", example = "true")
    private Boolean markAsQuoted;
}
