package com.crm.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品价格VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "产品价格VO")
public class ProductPriceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @Schema(description = "产品ID", example = "1")
    private Long productId;

    /**
     * 产品编号
     */
    @Schema(description = "产品编号", example = "PRD20260409001")
    private String productNo;

    /**
     * 产品名称(英文)
     */
    @Schema(description = "产品名称(英文)", example = "LED Light")
    private String productName;

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
     * EUR价格(根据汇率换算)
     */
    @Schema(description = "EUR价格", example = "2.30")
    private BigDecimal eurPrice;

    /**
     * GBP价格(根据汇率换算)
     */
    @Schema(description = "GBP价格", example = "2.00")
    private BigDecimal gbpPrice;

    /**
     * JPY价格(根据汇率换算)
     */
    @Schema(description = "JPY价格", example = "375.00")
    private BigDecimal jpyPrice;

    /**
     * 利润率(%)
     */
    @Schema(description = "利润率(%)", example = "20.00")
    private BigDecimal profitMargin;

    /**
     * 当前币种
     */
    @Schema(description = "当前币种", example = "USD")
    private String currency;

    /**
     * USD对CNY汇率
     */
    @Schema(description = "USD对CNY汇率", example = "7.2000")
    private BigDecimal usdToCnyRate;

    /**
     * EUR对CNY汇率
     */
    @Schema(description = "EUR对CNY汇率", example = "7.8000")
    private BigDecimal eurToCnyRate;

    /**
     * GBP对CNY汇率
     */
    @Schema(description = "GBP对CNY汇率", example = "9.0000")
    private BigDecimal gbpToCnyRate;

    /**
     * JPY对CNY汇率
     */
    @Schema(description = "JPY对CNY汇率", example = "0.0480")
    private BigDecimal jpyToCnyRate;
}
