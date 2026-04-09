package com.crm.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 订单查询条件
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单查询条件")
public class OrderQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "状态(pending_confirm/producing/completed/pending_shipment/shipped/completed/cancelled)")
    private String status;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "贸易术语")
    private String tradeTerm;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "关联报价单ID")
    private Long quotationId;

    @Schema(description = "最小金额")
    private java.math.BigDecimal minAmount;

    @Schema(description = "最大金额")
    private java.math.BigDecimal maxAmount;

    @Schema(description = "关键词(搜索客户名称、产品名等)")
    private String keyword;
}
