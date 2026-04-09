package com.crm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.system.domain.dto.PasswordResetDTO;
import com.crm.system.domain.dto.UserCreateDTO;
import com.crm.system.domain.dto.UserQuery;
import com.crm.system.domain.dto.UserUpdateDTO;
import com.crm.system.domain.vo.UserVO;
import com.crm.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "用户管理", description = "用户的增删改查、导入导出、密码重置等功能")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    public Result<IPage<UserVO>> pageUsers(
            @Parameter(description = "查询条件") UserQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<UserVO> page = userService.pageUsers(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        return Result.success(user);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public Result<Void> createUser(
            @Parameter(description = "用户信息") @Valid @RequestBody UserCreateDTO dto) {
        boolean success = userService.createUser(dto);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新用户")
    @PutMapping
    public Result<Void> updateUser(
            @Parameter(description = "用户信息") @Valid @RequestBody UserUpdateDTO dto) {
        boolean success = userService.updateUser(dto);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteUsers(
            @Parameter(description = "用户ID列表") @PathVariable List<Long> ids) {
        boolean success = userService.deleteUsers(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "重置密码")
    @PutMapping("/reset-password")
    public Result<Void> resetPassword(
            @Parameter(description = "密码重置信息") @Valid @RequestBody PasswordResetDTO dto) {
        boolean success = userService.resetPassword(dto.getUserId(), dto.getNewPassword());
        return success ? Result.success() : Result.error("重置失败");
    }

    @Operation(summary = "修改用户状态")
    @PutMapping("/change-status")
    public Result<Void> changeStatus(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "状态(0正常 1停用)") @RequestParam String status) {
        boolean success = userService.changeStatus(userId, status);
        return success ? Result.success() : Result.error("修改失败");
    }

    @Operation(summary = "分配用户角色")
    @PutMapping("/assign-roles")
    public Result<Void> assignRoles(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        boolean success = userService.assignRoles(userId, roleIds);
        return success ? Result.success() : Result.error("分配失败");
    }

    @Operation(summary = "导出用户数据")
    @GetMapping("/export")
    public Result<List<UserVO>> exportUsers(
            @Parameter(description = "查询条件") UserQuery query) {
        List<UserVO> users = userService.exportUsers(query);
        return Result.success(users);
    }

    @Operation(summary = "导入用户数据")
    @PostMapping("/import")
    public Result<Void> importUsers(
            @Parameter(description = "用户列表") @RequestBody List<UserCreateDTO> userList) {
        boolean success = userService.importUsers(userList);
        return success ? Result.success() : Result.error("导入失败");
    }
}
