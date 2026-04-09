package com.crm.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单明细视图")
public class OrderItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "明细ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "行号")
    private Integer lineNo;

    @Schema(description = "产品ID")
    private Long productId;

    @Schema(description = "产品编号")
    private String productNo;

    @Schema(description = "产品名称(英文)")
    private String productName;

    @Schema(description = "产品名称(中文)")
    private String productNameCn;

    @Schema(description = "产品规格/型号")
    private String specification;

    @Schema(description = "产品描述")
    private String description;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "订单数量")
    private BigDecimal quantity;

    @Schema(description = "已生产数量")
    private BigDecimal producedQuantity;

    @Schema(description = "已发货数量")
    private BigDecimal shippedQuantity;

    @Schema(description = "单价(原币)")
    private BigDecimal unitPrice;

    @Schema(description = "总金额(原币)")
    private BigDecimal totalAmount;

    @Schema(description = "成本价(CNY)")
    private BigDecimal costPrice;

    @Schema(description = "总成本(CNY)")
    private BigDecimal totalCost;

    @Schema(description = "利润率(%)")
    private BigDecimal profitMargin;

    @Schema(description = "包装方式")
    private String packingMethod;

    @Schema(description = "每箱数量")
    private Integer quantityPerCarton;

    @Schema(description = "箱数")
    private Integer cartonCount;

    @Schema(description = "总毛重(KG)")
    private BigDecimal totalGrossWeight;

    @Schema(description = "总净重(KG)")
    private BigDecimal totalNetWeight;

    @Schema(description = "总体积(CBM)")
    private BigDecimal totalVolume;

    @Schema(description = "HS编码")
    private String hsCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
