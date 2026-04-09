package com.crm.shipping.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 发货单查询条件
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "发货单查询条件")
public class ShippingQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发货单号")
    private String shippingNo;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "物流商(DHL/FedEx/UPS/海运/空运)")
    private String carrier;

    @Schema(description = "运单号")
    private String trackingNo;

    @Schema(description = "发货状态(pending待发货/shipped已发货/in_transit运输中/delivered已送达)")
    private String status;

    @Schema(description = "报关状态(pending待报关/clearing报关中/cleared已报关)")
    private String customsStatus;

    @Schema(description = "开始发货日期")
    private LocalDate startDate;

    @Schema(description = "结束发货日期")
    private LocalDate endDate;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "部门ID")
    private Long departmentId;
}
