package com.crm.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色分配菜单DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "角色分配菜单请求")
public class RoleMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roleId;

    /**
     * 菜单ID列表
     */
    @NotEmpty(message = "菜单ID列表不能为空")
    @Schema(description = "菜单ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> menuIds;
}
