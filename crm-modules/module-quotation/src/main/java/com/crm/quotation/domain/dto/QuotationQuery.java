package com.crm.quotation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 报价单查询条件
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "报价单查询条件")
public class QuotationQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "报价单号")
    private String quotationNo;

    @Schema(description = "状态(draft/pending_approval/approved/rejected/sent/converted/expired)")
    private String status;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "贸易术语")
    private String tradeTerm;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "是否需要审批")
    private Boolean needApproval;

    @Schema(description = "关联询盘ID")
    private Long inquiryId;

    @Schema(description = "最小金额")
    private java.math.BigDecimal minAmount;

    @Schema(description = "最大金额")
    private java.math.BigDecimal maxAmount;

    @Schema(description = "关键词(搜索客户名称、产品名等)")
    private String keyword;
}
