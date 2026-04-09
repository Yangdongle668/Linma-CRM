package com.crm.system.controller;

import com.crm.common.core.domain.Result;
import com.crm.system.domain.entity.SysMenu;
import com.crm.system.domain.vo.MenuTreeVO;
import com.crm.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "菜单管理", description = "菜单的增删改查、获取菜单树等功能")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "获取菜单树形结构")
    @GetMapping("/tree")
    public Result<List<MenuTreeVO>> getMenuTree() {
        List<MenuTreeVO> tree = menuService.getMenuTree();
        return Result.success(tree);
    }

    @Operation(summary = "根据用户ID获取菜单树")
    @GetMapping("/tree/{userId}")
    public Result<List<MenuTreeVO>> getMenuTreeByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<MenuTreeVO> tree = menuService.getMenuTreeByUserId(userId);
        return Result.success(tree);
    }

    @Operation(summary = "根据角色ID获取菜单列表")
    @GetMapping("/role/{roleId}")
    public Result<List<SysMenu>> getMenusByRoleId(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<SysMenu> menus = menuService.getMenusByRoleId(roleId);
        return Result.success(menus);
    }

    @Operation(summary = "根据用户ID获取菜单列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysMenu>> getMenusByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysMenu> menus = menuService.getMenusByUserId(userId);
        return Result.success(menus);
    }

    @Operation(summary = "根据ID查询菜单详情")
    @GetMapping("/{id}")
    public Result<SysMenu> getMenuById(
            @Parameter(description = "菜单ID") @PathVariable Long id) {
        SysMenu menu = menuService.getMenuById(id);
        return Result.success(menu);
    }

    @Operation(summary = "创建菜单")
    @PostMapping
    public Result<Void> createMenu(
            @Parameter(description = "菜单信息") @Valid @RequestBody SysMenu menu) {
        boolean success = menuService.createMenu(menu);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新菜单")
    @PutMapping
    public Result<Void> updateMenu(
            @Parameter(description = "菜单信息") @Valid @RequestBody SysMenu menu) {
        boolean success = menuService.updateMenu(menu);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(
            @Parameter(description = "菜单ID") @PathVariable Long id) {
        boolean success = menuService.deleteMenu(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}
