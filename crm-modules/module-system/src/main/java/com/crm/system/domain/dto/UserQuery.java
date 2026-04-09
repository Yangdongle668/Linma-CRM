package com.crm.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询条件DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "用户查询条件")
public class UserQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名(模糊查询)
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称(模糊查询)
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 状态(0正常 1停用)
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 性别(0男 1女 2未知)
     */
    @Schema(description = "性别")
    private String gender;
}
