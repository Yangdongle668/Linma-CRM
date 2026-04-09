package com.crm.finance.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应付账款VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "应付账款视图对象")
public class PayableVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应付ID")
    private Long id;

    @Schema(description = "应付单号")
    private String payableNo;

    @Schema(description = "关联订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "供应商ID")
    private Long supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "应付金额")
    private BigDecimal amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "已付金额")
    private BigDecimal paidAmount;

    @Schema(description = "未付金额")
    private BigDecimal unpaidAmount;

    @Schema(description = "付款进度(%)")
    private BigDecimal paymentProgress;

    @Schema(description = "应付日期")
    private LocalDate payableDate;

    @Schema(description = "到期日期")
    private LocalDate dueDate;

    @Schema(description = "状态(pending待付款/partial部分付款/paid已付款/overdue逾期)")
    private String status;

    @Schema(description = "付款方式")
    private String paymentTerms;

    @Schema(description = "付款申请状态(draft草稿/pending审批中/approved已批准/rejected已拒绝)")
    private String approvalStatus;

    @Schema(description = "审批人ID")
    private Long approvedBy;

    @Schema(description = "审批人姓名")
    private String approvedByName;

    @Schema(description = "审批时间")
    private LocalDateTime approvedTime;

    @Schema(description = "审批意见")
    private String approvalRemark;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "负责人姓名")
    private String ownerName;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
