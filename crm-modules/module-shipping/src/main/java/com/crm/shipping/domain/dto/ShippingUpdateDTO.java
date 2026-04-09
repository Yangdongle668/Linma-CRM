package com.crm.shipping.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 发货单更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "发货单更新信息")
public class ShippingUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "发货单ID不能为空")
    @Schema(description = "发货单ID", required = true)
    private Long id;

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

    @Schema(description = "备注")
    private String remark;
}
