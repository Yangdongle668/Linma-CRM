package com.crm.finance.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应付账款实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_payable")
public class CrmPayable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应付ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应付单号(自动生成)
     */
    private String payableNo;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 订单号(冗余字段 - 不存储在数据库中)
     */
    @TableField(exist = false)
    private String orderNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称(不存储在数据库中)
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * 应付金额
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 已付金额
     */
    private BigDecimal paidAmount;

    /**
     * 未付金额(计算字段)
     */
    @TableField(exist = false)
    private BigDecimal unpaidAmount;

    /**
     * 付款进度(%)(计算字段)
     */
    @TableField(exist = false)
    private BigDecimal paymentProgress;

    /**
     * 应付日期(计算字段)
     */
    @TableField(exist = false)
    private LocalDate payableDate;

    /**
     * 到期日期
     */
    private LocalDate dueDate;

    /**
     * 状态(pending待付款/partial部分付款/paid已付款/overdue逾期)
     */
    private String status;

    /**
     * 付款方式(T/T,L/C等)
     */
    @TableField(exist = false)
    private String paymentTerms;

    /**
     * 付款申请状态(draft草稿/pending审批中/approved已批准/rejected已拒绝)
     */
    private String approvalStatus;

    /**
     * 审批人ID
     */
    private Long approvedBy;

    /**
     * 审批时间
     */
    private LocalDateTime approvedTime;

    /**
     * 审批意见
     */
    private String approvalRemark;

    /**
     * 备注
     */
    private String remark;

    /**
     * 负责人ID
     */
    @TableField(exist = false)
    private Long ownerId;

    /**
     * 所属部门ID
     */
    @TableField(exist = false)
    private Long departmentId;

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
}
