package com.crm.finance.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 付款审批DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "付款审批信息")
public class PaymentApprovalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "应付ID不能为空")
    @Schema(description = "应付ID", required = true)
    private Long payableId;

    @NotNull(message = "审批结果不能为空")
    @Schema(description = "审批结果(approved批准/rejected拒绝)", required = true)
    private String approvalResult;

    @Schema(description = "审批意见")
    private String approvalRemark;
}
