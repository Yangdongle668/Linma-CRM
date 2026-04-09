package com.crm.quotation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 报价单主表实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_quotation")
public class CrmQuotation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报价单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报价单号(自动生成,格式:QT20260409001)
     */
    private String quotationNo;

    /**
     * 版本号(每次修改递增)
     */
    private Integer version;

    /**
     * 关联询盘ID(可选)
     */
    private Long inquiryId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称(冗余字段,便于显示)
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
     * 币种(USD/EUR/CNY/GBP等)
     */
    private String currency;

    /**
     * 汇率(相对于基准货币CNY)
     */
    private BigDecimal exchangeRate;

    /**
     * 贸易术语(FOB/CIF/DDP/EXW等)
     */
    private String tradeTerm;

    /**
     * 装运港
     */
    private String portOfLoading;

    /**
     * 目的港
     */
    private String portOfDestination;

    /**
     * 付款方式(T/T,L/C,D/P等)
     */
    private String paymentTerms;

    /**
     * 交货期(天)
     */
    private Integer deliveryDays;

    /**
     * 报价有效期(天)
     */
    private Integer validityDays;

    /**
     * 报价日期
     */
    private LocalDate quotationDate;

    /**
     * 有效期至
     */
    private LocalDate validUntil;

    /**
     * 总金额(原币)
     */
    private BigDecimal totalAmount;

    /**
     * 总成本(CNY)
     */
    private BigDecimal totalCost;

    /**
     * 利润金额(CNY)
     */
    private BigDecimal profitAmount;

    /**
     * 利润率(%)
     */
    private BigDecimal profitMargin;

    /**
     * 状态(draft草稿/pending_approval待审批/approved已批准/rejected已拒绝/sent已发送/converted已转订单/expired已过期)
     */
    private String status;

    /**
     * 是否需要审批(利润率<10%时需要)
     */
    private Boolean needApproval;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    private String approvalComment;

    /**
     * PDF文件路径
     */
    private String pdfPath;

    /**
     * 邮件发送次数
     */
    private Integer emailSendCount;

    /**
     * 最后发送时间
     */
    private LocalDateTime lastSendTime;

    /**
     * 邮件打开次数
     */
    private Integer emailOpenCount;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 所属部门ID
     */
    private Long departmentId;

    /**
     * 备注
     */
    private String remark;

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
