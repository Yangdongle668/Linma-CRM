package com.crm.contract.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同更新DTO")
public class ContractUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    @Schema(description = "合同ID", example = "1")
    @NotNull(message = "合同ID不能为空")
    private Long id;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", example = "Sales Contract - ABC Company")
    private String contractName;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型(sales/purchase/agency/nda)", example = "sales")
    private String contractType;

    /**
     * 客户ID
     */
    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    /**
     * 客户名称
     */
    @Schema(description = "客户名称", example = "ABC Company")
    private String customerName;

    /**
     * 联系人ID
     */
    @Schema(description = "联系人ID", example = "1")
    private Long contactId;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名", example = "John Smith")
    private String contactName;

    /**
     * 供应商ID
     */
    @Schema(description = "供应商ID", example = "1")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称", example = "XYZ Supplier")
    private String supplierName;

    /**
     * 合同金额
     */
    @Schema(description = "合同金额", example = "50000.00")
    private BigDecimal contractAmount;

    /**
     * 币种
     */
    @Schema(description = "币种", example = "USD")
    private String currency;

    /**
     * 汇率
     */
    @Schema(description = "汇率", example = "7.2000")
    private BigDecimal exchangeRate;

    /**
     * 签订日期
     */
    @Schema(description = "签订日期")
    private LocalDate signDate;

    /**
     * 生效日期
     */
    @Schema(description = "生效日期")
    private LocalDate effectiveDate;

    /**
     * 到期日期
     */
    @Schema(description = "到期日期")
    private LocalDate expiryDate;

    /**
     * 付款方式
     */
    @Schema(description = "付款方式", example = "T/T 30% deposit, 70% before shipment")
    private String paymentTerms;

    /**
     * 交货方式
     */
    @Schema(description = "交货方式", example = "FOB Shanghai")
    private String deliveryTerms;

    /**
     * 装运港
     */
    @Schema(description = "装运港", example = "Shanghai")
    private String portOfLoading;

    /**
     * 目的港
     */
    @Schema(description = "目的港", example = "Los Angeles")
    private String portOfDestination;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "draft")
    private String status;

    /**
     * 关联订单ID
     */
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    /**
     * 关联报价单ID
     */
    @Schema(description = "报价单ID", example = "1")
    private Long quotationId;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
