package com.crm.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单视图对象
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单视图")
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "关联报价单ID")
    private Long quotationId;

    @Schema(description = "报价单号")
    private String quotationNo;

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

    @Schema(description = "订单日期")
    private LocalDate orderDate;

    @Schema(description = "要求交货日期")
    private LocalDate requiredDeliveryDate;

    @Schema(description = "实际交货日期")
    private LocalDate actualDeliveryDate;

    @Schema(description = "总金额(原币)")
    private BigDecimal totalAmount;

    @Schema(description = "总成本(CNY)")
    private BigDecimal totalCost;

    @Schema(description = "利润金额(CNY)")
    private BigDecimal profitAmount;

    @Schema(description = "利润率(%)")
    private BigDecimal profitMargin;

    @Schema(description = "已收款金额")
    private BigDecimal receivedAmount;

    @Schema(description = "收款进度(%)")
    private BigDecimal paymentProgress;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "生产进度(%)")
    private BigDecimal productionProgress;

    @Schema(description = "预计完成日期")
    private LocalDate estimatedCompletionDate;

    @Schema(description = "PI形式发票号")
    private String piInvoiceNo;

    @Schema(description = "CI商业发票号")
    private String ciInvoiceNo;

    @Schema(description = "装箱单号")
    private String packingListNo;

    @Schema(description = "提单号")
    private String billOfLadingNo;

    @Schema(description = "出口退税金额")
    private BigDecimal taxRefundAmount;

    @Schema(description = "退税率(%)")
    private BigDecimal taxRefundRate;

    @Schema(description = "总体积(CBM)")
    private BigDecimal totalVolume;

    @Schema(description = "总毛重(KG)")
    private BigDecimal totalGrossWeight;

    @Schema(description = "总净重(KG)")
    private BigDecimal totalNetWeight;

    @Schema(description = "箱数")
    private Integer cartonCount;

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
    private List<OrderItemVO> items;

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
            case "pending_confirm" -> "待确认";
            case "producing" -> "生产中";
            case "completed" -> "已完工";
            case "pending_shipment" -> "待发货";
            case "shipped" -> "已发货";
            case "cancelled" -> "已取消";
            default -> status;
        };
    }
}
