package com.crm.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 订单创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单创建请求")
public class OrderCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "关联报价单ID(可选)")
    private Long quotationId;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "币种(USD/EUR/CNY/GBP等)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "币种不能为空")
    private String currency;

    @Schema(description = "贸易术语(FOB/CIF/DDP/EXW等)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "贸易术语不能为空")
    private String tradeTerm;

    @Schema(description = "装运港")
    private String portOfLoading;

    @Schema(description = "目的港")
    private String portOfDestination;

    @Schema(description = "付款方式(T/T,L/C/D/P等)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "付款方式不能为空")
    private String paymentTerms;

    @Schema(description = "订单日期")
    private LocalDate orderDate;

    @Schema(description = "要求交货日期")
    private LocalDate requiredDeliveryDate;

    @Schema(description = "明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotEmpty(message = "订单明细不能为空")
    private List<OrderItemDTO> items;

    @Schema(description = "备注")
    private String remark;
}
