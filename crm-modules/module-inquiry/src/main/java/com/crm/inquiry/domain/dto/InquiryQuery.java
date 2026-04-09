package com.crm.inquiry.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 询盘查询DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "询盘查询DTO")
public class InquiryQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 询盘号
     */
    @Schema(description = "询盘号", example = "INQ20260409001")
    private String inquiryNo;

    /**
     * 客户名称(模糊搜索)
     */
    @Schema(description = "客户名称", example = "ABC")
    private String customerName;

    /**
     * 联系人姓名(模糊搜索)
     */
    @Schema(description = "联系人姓名", example = "John")
    private String contactName;

    /**
     * 询盘来源
     */
    @Schema(description = "询盘来源(alibaba/website/email/exhibition/referral)", example = "alibaba")
    private String source;

    /**
     * 优先级
     */
    @Schema(description = "优先级(HIGH/MEDIUM/LOW)", example = "HIGH")
    private String priority;

    /**
     * 状态
     */
    @Schema(description = "状态(new/processing/quoted/converted/closed)", example = "new")
    private String status;

    /**
     * 主题(模糊搜索)
     */
    @Schema(description = "主题", example = "LED")
    private String subject;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long ownerId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long departmentId;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /**
     * 是否已转化(1已转化 0未转化)
     */
    @Schema(description = "是否已转化", example = "0")
    private Integer isConverted;
}
