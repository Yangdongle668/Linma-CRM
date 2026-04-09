package com.crm.inquiry.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 询盘创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "询盘创建DTO")
public class InquiryCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @Schema(description = "询盘来源(alibaba/website/email/exhibition/referral)", example = "alibaba")
    @NotBlank(message = "询盘来源不能为空")
    private String source;

    /**
     * 优先级
     */
    @Schema(description = "优先级(HIGH/MEDIUM/LOW)", example = "MEDIUM")
    private String priority;

    /**
     * 主题
     */
    @Schema(description = "主题", example = "Inquiry about LED lights")
    @NotBlank(message = "主题不能为空")
    private String subject;

    /**
     * 询盘内容
     */
    @Schema(description = "询盘内容", example = "We are interested in your LED lights...")
    @NotBlank(message = "询盘内容不能为空")
    private String content;

    /**
     * 感兴趣的产品ID
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
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
