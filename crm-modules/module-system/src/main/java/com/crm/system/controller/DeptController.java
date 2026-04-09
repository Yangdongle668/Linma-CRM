package com.crm.system.controller;

import com.crm.common.core.domain.Result;
import com.crm.system.domain.entity.SysDept;
import com.crm.system.domain.vo.DeptTreeVO;
import com.crm.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "部门管理", description = "部门的增删改查、获取部门树等功能")
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "获取部门树形结构")
    @GetMapping("/tree")
    public Result<List<DeptTreeVO>> getDeptTree() {
        List<DeptTreeVO> tree = deptService.getDeptTree();
        return Result.success(tree);
    }

    @Operation(summary = "根据ID查询部门详情")
    @GetMapping("/{id}")
    public Result<SysDept> getDeptById(
            @Parameter(description = "部门ID") @PathVariable Long id) {
        SysDept dept = deptService.getDeptById(id);
        return Result.success(dept);
    }

    @Operation(summary = "查询所有正常状态的部门列表")
    @GetMapping("/list")
    public Result<List<SysDept>> listNormalDepts() {
        List<SysDept> depts = deptService.listNormalDepts();
        return Result.success(depts);
    }

    @Operation(summary = "创建部门")
    @PostMapping
    public Result<Void> createDept(
            @Parameter(description = "部门信息") @Valid @RequestBody SysDept dept) {
        boolean success = deptService.createDept(dept);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新部门")
    @PutMapping
    public Result<Void> updateDept(
            @Parameter(description = "部门信息") @Valid @RequestBody SysDept dept) {
        boolean success = deptService.updateDept(dept);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDept(
            @Parameter(description = "部门ID") @PathVariable Long id) {
        boolean success = deptService.deleteDept(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}
