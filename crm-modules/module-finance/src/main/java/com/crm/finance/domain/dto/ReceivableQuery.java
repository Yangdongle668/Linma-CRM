package com.crm.finance.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 应收账款查询条件
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "应收账款查询条件")
public class ReceivableQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应收单号")
    private String receivableNo;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "状态(pending待收款/partial部分收款/paid已收款/overdue逾期)")
    private String status;

    @Schema(description = "账龄区间(0-30/31-60/61-90/90+)")
    private String agingRange;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "负责人ID")
    private Long ownerId;
}
