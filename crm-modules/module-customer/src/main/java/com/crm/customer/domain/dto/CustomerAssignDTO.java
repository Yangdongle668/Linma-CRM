package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 客户分配DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户分配信息")
public class CustomerAssignDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户ID列表不能为空")
    private List<Long> customerIds;

    @Schema(description = "负责人ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "负责人ID不能为空")
    private Long ownerId;

    @Schema(description = "备注")
    private String remark;
}
