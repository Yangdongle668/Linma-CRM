package com.crm.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 订单更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单更新请求")
public class OrderUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单ID不能为空")
    private Long id;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "贸易术语")
    private String tradeTerm;

    @Schema(description = "装运港")
    private String portOfLoading;

    @Schema(description = "目的港")
    private String portOfDestination;

    @Schema(description = "付款方式")
    private String paymentTerms;

    @Schema(description = "要求交货日期")
    private LocalDate requiredDeliveryDate;

    @Schema(description = "明细列表")
    @Valid
    @NotEmpty(message = "订单明细不能为空")
    private List<OrderItemDTO> items;

    @Schema(description = "备注")
    private String remark;
}
