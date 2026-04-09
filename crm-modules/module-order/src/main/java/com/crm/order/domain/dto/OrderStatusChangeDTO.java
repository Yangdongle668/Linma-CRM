package com.crm.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单状态变更DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "订单状态变更请求")
public class OrderStatusChangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "目标状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "目标状态不能为空")
    private String targetStatus;

    @Schema(description = "变更原因/备注")
    private String reason;
}
