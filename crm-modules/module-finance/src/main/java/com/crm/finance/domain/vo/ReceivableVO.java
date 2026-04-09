package com.crm.finance.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收账款VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "应收账款视图对象")
public class ReceivableVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "应收ID")
    private Long id;

    @Schema(description = "应收单号")
    private String receivableNo;

    @Schema(description = "关联订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "应收金额(原币)")
    private BigDecimal amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "已收金额")
    private BigDecimal receivedAmount;

    @Schema(description = "未收金额")
    private BigDecimal unpaidAmount;

    @Schema(description = "收款进度(%)")
    private BigDecimal paymentProgress;

    @Schema(description = "应收日期")
    private LocalDate receivableDate;

    @Schema(description = "到期日期")
    private LocalDate dueDate;

    @Schema(description = "账龄天数")
    private Integer agingDays;

    @Schema(description = "账龄区间(0-30/31-60/61-90/90+)")
    private String agingRange;

    @Schema(description = "状态(pending待收款/partial部分收款/paid已收款/overdue逾期)")
    private String status;

    @Schema(description = "付款方式")
    private String paymentTerms;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "负责人姓名")
    private String ownerName;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
