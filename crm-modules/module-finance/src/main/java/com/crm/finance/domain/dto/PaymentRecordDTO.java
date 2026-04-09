package com.crm.finance.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收款记录DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "收款记录信息")
public class PaymentRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "应收ID不能为空")
    @Schema(description = "应收ID", required = true)
    private Long receivableId;

    @NotNull(message = "收款金额不能为空")
    @Schema(description = "收款金额", required = true)
    private BigDecimal amount;

    @Schema(description = "收款日期")
    private LocalDate paymentDate;

    @Schema(description = "收款方式")
    private String paymentMethod;

    @Schema(description = "备注")
    private String remark;
}
