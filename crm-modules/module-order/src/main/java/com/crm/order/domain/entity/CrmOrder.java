package com.crm.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_order")
public class CrmOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号(自动生成,格式:ORD20260409001)
     */
    private String orderNo;

    /**
     * 关联报价单ID
     */
    private Long quotationId;

    /**
     * 报价单号(冗余字段)
     */
    private String quotationNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称(冗余字段)
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
     * 订单日期
     */
    private LocalDate orderDate;

    /**
     * 要求交货日期
     */
    private LocalDate requiredDeliveryDate;

    /**
     * 实际交货日期
     */
    private LocalDate actualDeliveryDate;

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
     * 已收款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 收款进度(%)
     */
    private BigDecimal paymentProgress;

    /**
     * 状态机: pending_confirm待确认/producing生产中/completed已完工/pending_shipment待发货/shipped已发货/completed已完成/cancelled已取消
     */
    private String status;

    /**
     * 生产进度(%)
     */
    private BigDecimal productionProgress;

    /**
     * 预计完成日期
     */
    private LocalDate estimatedCompletionDate;

    /**
     * PI形式发票号
     */
    private String piInvoiceNo;

    /**
     * CI商业发票号
     */
    private String ciInvoiceNo;

    /**
     * 装箱单号
     */
    private String packingListNo;

    /**
     * 提单号
     */
    private String billOfLadingNo;

    /**
     * 出口退税金额
     */
    private BigDecimal taxRefundAmount;

    /**
     * 退税率(%)
     */
    private BigDecimal taxRefundRate;

    /**
     * 总体积(CBM)
     */
    private BigDecimal totalVolume;

    /**
     * 总毛重(KG)
     */
    private BigDecimal totalGrossWeight;

    /**
     * 总净重(KG)
     */
    private BigDecimal totalNetWeight;

    /**
     * 箱数
     */
    private Integer cartonCount;

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
