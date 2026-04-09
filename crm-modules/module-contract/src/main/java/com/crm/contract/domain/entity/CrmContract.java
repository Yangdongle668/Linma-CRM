package com.crm.contract.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_contract")
public class CrmContract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同号(自动生成,格式:CON20260409001)
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同类型(sales/purchase/agency/nda)
     */
    private String contractType;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人ID
     */
    private Long contactId;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 供应商ID(采购合同)
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 签订日期
     */
    private LocalDate signDate;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 到期日期
     */
    private LocalDate expiryDate;

    /**
     * 付款方式
     */
    private String paymentTerms;

    /**
     * 交货方式
     */
    private String deliveryTerms;

    /**
     * 装运港
     */
    private String portOfLoading;

    /**
     * 目的港
     */
    private String portOfDestination;

    /**
     * 状态(draft/pending_approval/approved/rejected/archived/cancelled)
     */
    private String status;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    private String approvalComment;

    /**
     * 电子签名URL
     */
    private String electronicSignature;

    /**
     * 合同文件URL(PDF)
     */
    private String contractFileUrl;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 关联订单号
     */
    private String orderNo;

    /**
     * 关联报价单ID
     */
    private Long quotationId;

    /**
     * 关联报价单号
     */
    private String quotationNo;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 负责人姓名
     */
    private String ownerName;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 归档时间
     */
    private LocalDateTime archiveTime;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新者
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;
}
