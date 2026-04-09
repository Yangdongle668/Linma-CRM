package com.crm.contract.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 合同审批DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同审批DTO")
public class ContractApprovalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    @Schema(description = "合同ID", example = "1")
    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /**
     * 审批结果(approve/reject)
     */
    @Schema(description = "审批结果(approve/reject)", example = "approve")
    @NotBlank(message = "审批结果不能为空")
    private String approvalResult;

    /**
     * 审批意见
     */
    @Schema(description = "审批意见", example = "同意签订")
    private String approvalComment;

    /**
     * 电子签名URL
     */
    @Schema(description = "电子签名URL")
    private String electronicSignature;
}
