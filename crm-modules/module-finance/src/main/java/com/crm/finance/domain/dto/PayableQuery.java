package com.crm.finance.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 应付账款查询条件
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "应付账款查询条件")
public class PayableQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应付单号")
    private String payableNo;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "供应商ID")
    private Long supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "状态(pending待付款/partial部分付款/paid已付款/overdue逾期)")
    private String status;

    @Schema(description = "审批状态(draft草稿/pending审批中/approved已批准/rejected已拒绝)")
    private String approvalStatus;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "负责人ID")
    private Long ownerId;
}
