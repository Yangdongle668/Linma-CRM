package com.crm.shipping.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 发货单创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "发货单创建信息")
public class ShippingCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "关联订单ID", required = true)
    private Long orderId;

    @NotBlank(message = "物流商不能为空")
    @Schema(description = "物流商(DHL/FedEx/UPS/海运/空运)", required = true)
    private String carrier;

    @Schema(description = "运单号")
    private String trackingNo;

    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @Schema(description = "预计到达日期")
    private LocalDate estimatedArrivalDate;

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

    @Schema(description = "起运港")
    private String portOfLoading;

    @Schema(description = "目的港")
    private String portOfDestination;

    @Schema(description = "贸易术语(FOB/CIF/DDP等)")
    private String tradeTerm;

    @Schema(description = "备注")
    private String remark;
}
