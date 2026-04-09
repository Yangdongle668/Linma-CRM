package com.crm.quotation.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报价单视图对象
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "报价单视图")
public class QuotationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "报价单ID")
    private Long id;

    @Schema(description = "报价单号")
    private String quotationNo;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "关联询盘ID")
    private Long inquiryId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "汇率")
    private BigDecimal exchangeRate;

    @Schema(description = "贸易术语")
    private String tradeTerm;

    @Schema(description = "装运港")
    private String portOfLoading;

    @Schema(description = "目的港")
    private String portOfDestination;

    @Schema(description = "付款方式")
    private String paymentTerms;

    @Schema(description = "交货期(天)")
    private Integer deliveryDays;

    @Schema(description = "报价有效期(天)")
    private Integer validityDays;

    @Schema(description = "报价日期")
    private LocalDate quotationDate;

    @Schema(description = "有效期至")
    private LocalDate validUntil;

    @Schema(description = "总金额(原币)")
    private BigDecimal totalAmount;

    @Schema(description = "总成本(CNY)")
    private BigDecimal totalCost;

    @Schema(description = "利润金额(CNY)")
    private BigDecimal profitAmount;

    @Schema(description = "利润率(%)")
    private BigDecimal profitMargin;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "是否需要审批")
    private Boolean needApproval;

    @Schema(description = "审批人ID")
    private Long approverId;

    @Schema(description = "审批人姓名")
    private String approverName;

    @Schema(description = "审批时间")
    private LocalDateTime approvalTime;

    @Schema(description = "审批意见")
    private String approvalComment;

    @Schema(description = "PDF文件路径")
    private String pdfPath;

    @Schema(description = "邮件发送次数")
    private Integer emailSendCount;

    @Schema(description = "最后发送时间")
    private LocalDateTime lastSendTime;

    @Schema(description = "邮件打开次数")
    private Integer emailOpenCount;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "负责人姓名")
    private String ownerName;

    @Schema(description = "所属部门ID")
    private Long departmentId;

    @Schema(description = "所属部门名称")
    private String departmentName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "明细列表")
    private List<QuotationItemVO> items;

    @Schema(description = "创建者")
    private Long createdBy;

    @Schema(description = "创建者姓名")
    private String createdByName;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    /**
     * 获取状态描述
     */
    public String getStatusDesc() {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case "draft" -> "草稿";
            case "pending_approval" -> "待审批";
            case "approved" -> "已批准";
            case "rejected" -> "已拒绝";
            case "sent" -> "已发送";
            case "converted" -> "已转订单";
            case "expired" -> "已过期";
            default -> status;
        };
    }
}
