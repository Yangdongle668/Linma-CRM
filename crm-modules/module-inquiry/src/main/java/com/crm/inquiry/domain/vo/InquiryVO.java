package com.crm.inquiry.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 询盘VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "询盘VO")
public class InquiryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 询盘ID
     */
    @Schema(description = "询盘ID", example = "1")
    private Long id;

    /**
     * 询盘号
     */
    @Schema(description = "询盘号", example = "INQ20260409001")
    private String inquiryNo;

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
     * 联系人邮箱
     */
    @Schema(description = "联系人邮箱", example = "john@example.com")
    private String contactEmail;

    /**
     * 询盘来源
     */
    @Schema(description = "询盘来源", example = "alibaba")
    private String source;

    /**
     * 来源描述
     */
    @Schema(description = "来源描述", example = "阿里巴巴国际站")
    private String sourceDesc;

    /**
     * 优先级
     */
    @Schema(description = "优先级", example = "MEDIUM")
    private String priority;

    /**
     * 优先级描述
     */
    @Schema(description = "优先级描述", example = "中优先级")
    private String priorityDesc;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "processing")
    private String status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述", example = "处理中")
    private String statusDesc;

    /**
     * 主题
     */
    @Schema(description = "主题", example = "Inquiry about LED lights")
    private String subject;

    /**
     * 询盘内容
     */
    @Schema(description = "询盘内容")
    private String content;

    /**
     * 产品ID
     */
    @Schema(description = "产品ID", example = "1")
    private Long productId;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称", example = "LED Light")
    private String productName;

    /**
     * 需求数量
     */
    @Schema(description = "需求数量", example = "1000")
    private Integer quantity;

    /**
     * 目标价格
     */
    @Schema(description = "目标价格", example = "2.00")
    private BigDecimal targetPrice;

    /**
     * 币种
     */
    @Schema(description = "币种", example = "USD")
    private String currency;

    /**
     * 目的国家
     */
    @Schema(description = "目的国家", example = "USA")
    private String destinationCountry;

    /**
     * 期望交货期
     */
    @Schema(description = "期望交货期")
    private LocalDateTime expectedDeliveryDate;

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
     * 最后回复时间
     */
    @Schema(description = "最后回复时间")
    private LocalDateTime lastReplyTime;

    /**
     * 回复次数
     */
    @Schema(description = "回复次数", example = "3")
    private Integer replyCount;

    /**
     * 转为报价单ID
     */
    @Schema(description = "报价单ID", example = "1")
    private Long quotationId;

    /**
     * 报价单号
     */
    @Schema(description = "报价单号", example = "QT20260409001")
    private String quotationNo;

    /**
     * 转为订单ID
     */
    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    /**
     * 订单号
     */
    @Schema(description = "订单号", example = "ORD20260409001")
    private String orderNo;

    /**
     * 关闭原因
     */
    @Schema(description = "关闭原因")
    private String closeReason;

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
