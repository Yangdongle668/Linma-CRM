package com.crm.shipping.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发货单VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "发货单视图对象")
public class ShippingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发货单ID")
    private Long id;

    @Schema(description = "发货单号")
    private String shippingNo;

    @Schema(description = "关联订单ID")
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

    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @Schema(description = "预计到达日期")
    private LocalDate estimatedArrivalDate;

    @Schema(description = "实际到达日期")
    private LocalDate actualArrivalDate;

    @Schema(description = "箱数")
    private Integer cartonCount;

    @Schema(description = "总体积(CBM)")
    private BigDecimal totalVolume;

    @Schema(description = "总毛重(KG)")
    private BigDecimal totalGrossWeight;

    @Schema(description = "总净重(KG)")
    private BigDecimal totalNetWeight;

    @Schema(description = "报关状态(pending待报关/clearing报关中/cleared已报关)")
    private String customsStatus;

    @Schema(description = "报关单号")
    private String customsDeclarationNo;

    @Schema(description = "发货状态(pending待发货/shipped已发货/in_transit运输中/delivered已送达)")
    private String status;

    @Schema(description = "起运港")
    private String portOfLoading;

    @Schema(description = "目的港")
    private String portOfDestination;

    @Schema(description = "贸易术语")
    private String tradeTerm;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "负责人姓名")
    private String ownerName;

    @Schema(description = "所属部门ID")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
}
