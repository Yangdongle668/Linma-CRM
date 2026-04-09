package com.crm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.system.domain.dto.RoleMenuDTO;
import com.crm.system.domain.dto.RoleQuery;
import com.crm.system.domain.entity.SysRole;
import com.crm.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "角色管理", description = "角色的增删改查、分配菜单、数据权限设置等功能")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @GetMapping("/page")
    public Result<IPage<SysRole>> pageRoles(
            @Parameter(description = "查询条件") RoleQuery query,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<SysRole> page = roleService.pageRoles(query, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "查询所有角色列表")
    @GetMapping("/list")
    public Result<List<SysRole>> listAllRoles() {
        List<SysRole> roles = roleService.listAllRoles();
        return Result.success(roles);
    }

    @Operation(summary = "根据ID查询角色详情")
    @GetMapping("/{id}")
    public Result<SysRole> getRoleById(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        SysRole role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @Operation(summary = "创建角色")
    @PostMapping
    public Result<Void> createRole(
            @Parameter(description = "角色信息") @Valid @RequestBody SysRole role) {
        boolean success = roleService.createRole(role);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新角色")
    @PutMapping
    public Result<Void> updateRole(
            @Parameter(description = "角色信息") @Valid @RequestBody SysRole role) {
        boolean success = roleService.updateRole(role);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteRoles(
            @Parameter(description = "角色ID列表") @PathVariable List<Long> ids) {
        boolean success = roleService.deleteRoles(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "修改角色状态")
    @PutMapping("/change-status")
    public Result<Void> changeStatus(
            @Parameter(description = "角色ID") @RequestParam Long roleId,
            @Parameter(description = "状态(0正常 1停用)") @RequestParam String status) {
        boolean success = roleService.changeStatus(roleId, status);
        return success ? Result.success() : Result.error("修改失败");
    }

    @Operation(summary = "为角色分配菜单")
    @PutMapping("/assign-menus")
    public Result<Void> assignMenus(
            @Parameter(description = "角色菜单分配信息") @Valid @RequestBody RoleMenuDTO dto) {
        boolean success = roleService.assignMenus(dto.getRoleId(), dto.getMenuIds());
        return success ? Result.success() : Result.error("分配失败");
    }

    @Operation(summary = "查询角色的菜单ID列表")
    @GetMapping("/menu-ids/{roleId}")
    public Result<List<Long>> getRoleMenuIds(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    @Operation(summary = "设置角色数据权限")
    @PutMapping("/data-scope")
    public Result<Void> setDataScope(
            @Parameter(description = "角色ID") @RequestParam Long roleId,
            @Parameter(description = "数据范围") @RequestParam String dataScope,
            @Parameter(description = "部门ID列表") @RequestBody(required = false) List<Long> deptIds) {
        boolean success = roleService.setDataScope(roleId, dataScope, deptIds);
        return success ? Result.success() : Result.error("设置失败");
    }

    @Operation(summary = "根据用户ID查询角色列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysRole>> getRolesByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysRole> roles = roleService.getRolesByUserId(userId);
        return Result.success(roles);
    }
}
