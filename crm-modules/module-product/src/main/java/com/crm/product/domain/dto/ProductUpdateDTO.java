package com.crm.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "产品更新DTO")
public class ProductUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @Schema(description = "产品ID", example = "1")
    @NotNull(message = "产品ID不能为空")
    private Long id;

    /**
     * 产品名称(英文)
     */
    @Schema(description = "产品名称(英文)", example = "LED Light")
    @NotBlank(message = "产品名称(英文)不能为空")
    private String productName;

    /**
     * 产品名称(中文)
     */
    @Schema(description = "产品名称(中文)", example = "LED灯")
    private String productNameCn;

    /**
     * 产品分类ID
     */
    @Schema(description = "产品分类ID", example = "1")
    @NotNull(message = "产品分类不能为空")
    private Long categoryId;

    /**
     * SKU编码
     */
    @Schema(description = "SKU编码", example = "LED-001")
    private String sku;

    /**
     * 规格型号
     */
    @Schema(description = "规格型号", example = "10W")
    private String specification;

    /**
     * 产品描述
     */
    @Schema(description = "产品描述", example = "High brightness LED light")
    private String description;

    /**
     * 单位
     */
    @Schema(description = "单位", example = "PCS")
    @NotBlank(message = "单位不能为空")
    private String unit;

    /**
     * 成本价(CNY)
     */
    @Schema(description = "成本价(CNY)", example = "10.50")
    private BigDecimal costPrice;

    /**
     * 出厂价(CNY)
     */
    @Schema(description = "出厂价(CNY)", example = "15.00")
    private BigDecimal factoryPrice;

    /**
     * FOB价格(USD)
     */
    @Schema(description = "FOB价格(USD)", example = "2.50")
    private BigDecimal fobPrice;

    /**
     * CIF价格(USD)
     */
    @Schema(description = "CIF价格(USD)", example = "3.00")
    private BigDecimal cifPrice;

    /**
     * 币种
     */
    @Schema(description = "币种", example = "USD")
    private String currency;

    /**
     * 库存数量
     */
    @Schema(description = "库存数量", example = "1000")
    private Integer stockQuantity;

    /**
     * 最小起订量(MOQ)
     */
    @Schema(description = "最小起订量", example = "100")
    private Integer minOrderQuantity;

    /**
     * HS编码
     */
    @Schema(description = "HS编码", example = "9405409000")
    private String hsCode;

    /**
     * 退税率(%)
     */
    @Schema(description = "退税率(%)", example = "13.00")
    private BigDecimal taxRefundRate;

    /**
     * 毛重(KG)
     */
    @Schema(description = "毛重(KG)", example = "0.5")
    private BigDecimal grossWeight;

    /**
     * 净重(KG)
     */
    @Schema(description = "净重(KG)", example = "0.4")
    private BigDecimal netWeight;

    /**
     * 包装尺寸(长*宽*高 cm)
     */
    @Schema(description = "包装尺寸", example = "10*5*5")
    private String packingSize;

    /**
     * 产品图片URL列表
     */
    @Schema(description = "产品图片URL列表")
    private String images;

    /**
     * 主图URL
     */
    @Schema(description = "主图URL")
    private String mainImage;

    /**
     * 状态(1上架 0下架)
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
