package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户合并DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户合并信息")
public class CustomerMergeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主客户ID(保留的客户)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主客户ID不能为空")
    private Long mainCustomerId;

    @Schema(description = "被合并客户ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "被合并客户ID列表不能为空")
    private java.util.List<Long> mergeCustomerIds;

    @Schema(description = "备注")
    private String remark;
}
