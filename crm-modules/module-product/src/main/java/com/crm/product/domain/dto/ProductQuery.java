package com.crm.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品查询DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "产品查询DTO")
public class ProductQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品名称(模糊搜索)
     */
    @Schema(description = "产品名称", example = "LED")
    private String productName;

    /**
     * 产品编号
     */
    @Schema(description = "产品编号", example = "PRD20260409001")
    private String productNo;

    /**
     * SKU编码
     */
    @Schema(description = "SKU编码", example = "LED-001")
    private String sku;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    /**
     * 状态(1上架 0下架)
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * HS编码
     */
    @Schema(description = "HS编码", example = "9405409000")
    private String hsCode;

    /**
     * 最低价格
     */
    @Schema(description = "最低价格", example = "1.00")
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    @Schema(description = "最高价格", example = "100.00")
    private BigDecimal maxPrice;

    /**
     * 是否有库存(1有库存 0无库存)
     */
    @Schema(description = "是否有库存", example = "1")
    private Integer hasStock;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long ownerId;
}
