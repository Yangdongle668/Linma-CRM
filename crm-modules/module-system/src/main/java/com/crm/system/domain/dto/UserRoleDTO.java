package com.crm.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户分配角色DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "用户分配角色请求")
public class UserRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色ID列表不能为空")
    @Schema(description = "角色ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> roleIds;
}
