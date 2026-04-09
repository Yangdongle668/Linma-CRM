package com.crm.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单明细")
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "明细ID(更新时传入)")
    private Long id;

    @Schema(description = "行号")
    private Integer lineNo;

    @Schema(description = "产品ID")
    private Long productId;

    @Schema(description = "产品编号")
    private String productNo;

    @Schema(description = "产品名称(英文)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    @Schema(description = "产品名称(中文)")
    private String productNameCn;

    @Schema(description = "产品规格/型号")
    private String specification;

    @Schema(description = "产品描述")
    private String description;

    @Schema(description = "单位(PCS/SET/KG等)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "单位不能为空")
    private String unit;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    @DecimalMin(value = "0.0001", message = "数量必须大于0")
    private BigDecimal quantity;

    @Schema(description = "单价(原币)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.01", message = "单价必须大于0")
    private BigDecimal unitPrice;

    @Schema(description = "成本价(CNY)")
    private BigDecimal costPrice;

    @Schema(description = "包装方式")
    private String packingMethod;

    @Schema(description = "每箱数量")
    private Integer quantityPerCarton;

    @Schema(description = "单箱毛重(KG)")
    private BigDecimal grossWeightPerCarton;

    @Schema(description = "单箱净重(KG)")
    private BigDecimal netWeightPerCarton;

    @Schema(description = "单箱体积(CBM)")
    private BigDecimal volumePerCarton;

    @Schema(description = "HS编码")
    private String hsCode;

    @Schema(description = "备注")
    private String remark;
}
