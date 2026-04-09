package com.crm.inquiry.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 询盘实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_inquiry")
public class CrmInquiry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 询盘ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 询盘号(自动生成,格式:INQ20260409001)
     */
    private String inquiryNo;

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
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 询盘来源(alibaba/website/email/exhibition/referral)
     */
    private String source;

    /**
     * 优先级(HIGH/MEDIUM/LOW)
     */
    private String priority;

    /**
     * 状态(new/processing/quoted/converted/closed)
     */
    private String status;

    /**
     * 主题
     */
    private String subject;

    /**
     * 询盘内容
     */
    private String content;

    /**
     * 感兴趣的产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 需求数量
     */
    private Integer quantity;

    /**
     * 目标价格
     */
    private BigDecimal targetPrice;

    /**
     * 币种
     */
    private String currency;

    /**
     * 目的国家
     */
    private String destinationCountry;

    /**
     * 期望交货期
     */
    private LocalDateTime expectedDeliveryDate;

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
     * 最后回复时间
     */
    private LocalDateTime lastReplyTime;

    /**
     * 回复次数
     */
    private Integer replyCount;

    /**
     * 转为报价单ID
     */
    private Long quotationId;

    /**
     * 转为订单ID
     */
    private Long orderId;

    /**
     * 关闭原因
     */
    private String closeReason;

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
