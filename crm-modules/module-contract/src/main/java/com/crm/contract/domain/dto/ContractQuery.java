package com.crm.contract.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 合同查询DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同查询DTO")
public class ContractQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同号
     */
    @Schema(description = "合同号", example = "CON20260409001")
    private String contractNo;

    /**
     * 合同名称(模糊搜索)
     */
    @Schema(description = "合同名称", example = "Sales")
    private String contractName;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型(sales/purchase/agency/nda)", example = "sales")
    private String contractType;

    /**
     * 客户名称(模糊搜索)
     */
    @Schema(description = "客户名称", example = "ABC")
    private String customerName;

    /**
     * 状态
     */
    @Schema(description = "状态(draft/pending_approval/approved/rejected/archived/cancelled)", example = "approved")
    private String status;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long ownerId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long departmentId;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @Schema(description = "结束日期")
    private LocalDate endDate;

    /**
     * 最低金额
     */
    @Schema(description = "最低金额", example = "10000")
    private java.math.BigDecimal minAmount;

    /**
     * 最高金额
     */
    @Schema(description = "最高金额", example = "100000")
    private java.math.BigDecimal maxAmount;
}
