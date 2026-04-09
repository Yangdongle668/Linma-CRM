package com.crm.quotation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 报价单审批DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "报价单审批请求")
public class QuotationApprovalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "报价单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报价单ID不能为空")
    private Long quotationId;

    @Schema(description = "是否通过(true通过/false拒绝)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    @Schema(description = "审批意见")
    private String comment;
}
