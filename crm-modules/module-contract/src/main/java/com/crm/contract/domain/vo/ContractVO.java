package com.crm.contract.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同VO")
public class ContractVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    @Schema(description = "合同ID", example = "1")
    private Long id;

    /**
     * 合同号
     */
    @Schema(description = "合同号", example = "CON20260409001")
    private String contractNo;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", example = "Sales Contract - ABC Company")
    private String contractName;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", example = "sales")
    private String contractType;

    /**
     * 合同类型描述
     */
    @Schema(description = "合同类型描述", example = "销售合同")
    private String contractTypeDesc;

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
    @Schema(description = "状态", example = "approved")
    private String status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述", example = "已审批")
    private String statusDesc;

    /**
     * 审批人ID
     */
    @Schema(description = "审批人ID", example = "1")
    private Long approverId;

    /**
     * 审批人姓名
     */
    @Schema(description = "审批人姓名", example = "李四")
    private String approverName;

    /**
     * 审批时间
     */
    @Schema(description = "审批时间")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @Schema(description = "审批意见", example = "同意签订")
    private String approvalComment;

    /**
     * 电子签名URL
     */
    @Schema(description = "电子签名URL")
    private String electronicSignature;

    /**
     * 合同文件URL
     */
    @Schema(description = "合同文件URL(PDF)")
    private String contractFileUrl;

    /**
     * 关联订单ID
     */
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    /**
     * 关联订单号
     */
    @Schema(description = "订单号", example = "ORD20260409001")
    private String orderNo;

    /**
     * 关联报价单ID
     */
    @Schema(description = "报价单ID", example = "1")
    private Long quotationId;

    /**
     * 关联报价单号
     */
    @Schema(description = "报价单号", example = "QT20260409001")
    private String quotationNo;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long ownerId;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名", example = "张三")
    private String ownerName;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long departmentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称", example = "销售部")
    private String departmentName;

    /**
     * 归档时间
     */
    @Schema(description = "归档时间")
    private LocalDateTime archiveTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
