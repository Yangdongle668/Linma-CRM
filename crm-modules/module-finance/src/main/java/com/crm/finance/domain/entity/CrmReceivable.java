package com.crm.finance.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收账款实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_receivable")
public class CrmReceivable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应收ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应收单号(自动生成)
     */
    private String receivableNo;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 订单号(冗余字段)
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称(冗余字段)
     */
    private String customerName;

    /**
     * 应收金额(原币)
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 已收金额
     */
    private BigDecimal receivedAmount;

    /**
     * 未收金额
     */
    private BigDecimal unpaidAmount;

    /**
     * 收款进度(%)
     */
    private BigDecimal paymentProgress;

    /**
     * 应收日期
     */
    private LocalDate receivableDate;

    /**
     * 到期日期
     */
    private LocalDate dueDate;

    /**
     * 账龄天数
     */
    private Integer agingDays;

    /**
     * 账龄区间(0-30/31-60/61-90/90+)
     */
    private String agingRange;

    /**
     * 状态(pending待收款/partial部分收款/paid已收款/overdue逾期)
     */
    private String status;

    /**
     * 付款方式(T/T,L/C等)
     */
    private String paymentTerms;

    /**
     * 备注
     */
    private String remark;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 所属部门ID
     */
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
