package com.crm.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 生产进度更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "生产进度更新请求")
public class ProductionProgressDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "生产进度(%)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生产进度不能为空")
    @DecimalMin(value = "0", message = "生产进度不能小于0")
    @DecimalMax(value = "100", message = "生产进度不能大于100")
    private BigDecimal progress;

    @Schema(description = "预计完成日期")
    private LocalDate estimatedCompletionDate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "照片URL列表(关键节点拍照上传)")
    private java.util.List<String> photoUrls;
}
