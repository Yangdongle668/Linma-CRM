package com.crm.quotation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 报价单创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "报价单创建请求")
public class QuotationCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "关联询盘ID(可选)")
    private Long inquiryId;

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

    @Schema(description = "付款方式(T/T,L/C,D/P等)")
    private String paymentTerms;

    @Schema(description = "交货期(天)")
    private Integer deliveryDays;

    @Schema(description = "报价有效期(天)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报价有效期不能为空")
    @Min(value = 1, message = "报价有效期至少为1天")
    private Integer validityDays;

    @Schema(description = "报价日期")
    private LocalDate quotationDate;

    @Schema(description = "明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotEmpty(message = "报价单明细不能为空")
    private List<QuotationItemDTO> items;

    @Schema(description = "备注")
    private String remark;
}
