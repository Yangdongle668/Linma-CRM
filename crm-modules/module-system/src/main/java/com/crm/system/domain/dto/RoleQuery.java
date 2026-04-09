package com.crm.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色查询条件DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "角色查询条件")
public class RoleQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称(模糊查询)
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @Schema(description = "角色权限字符串")
    private String roleKey;

    /**
     * 状态(0正常 1停用)
     */
    @Schema(description = "状态")
    private String status;
}
