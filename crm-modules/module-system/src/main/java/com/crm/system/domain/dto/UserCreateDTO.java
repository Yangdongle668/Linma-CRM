package com.crm.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "用户创建请求")
public class UserCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空")
    @Schema(description = "部门ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 64, message = "用户名长度必须在2-64之间")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 30, message = "昵称长度不能超过30")
    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 性别(0男 1女 2未知)
     */
    @Schema(description = "性别")
    private String gender;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 状态(0正常 1停用)
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
