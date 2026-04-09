package com.crm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.system.domain.entity.SysDictData;
import com.crm.system.domain.entity.SysDictType;
import com.crm.system.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "字典管理", description = "字典类型和字典数据的增删改查功能")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    // ==================== 字典类型相关接口 ====================

    @Operation(summary = "分页查询字典类型列表")
    @GetMapping("/type/page")
    public Result<IPage<SysDictType>> pageDictTypes(
            @Parameter(description = "字典名称") @RequestParam(required = false) String dictName,
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<SysDictType> page = dictService.pageDictTypes(dictName, dictType, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "查询所有字典类型列表")
    @GetMapping("/type/list")
    public Result<List<SysDictType>> listAllDictTypes() {
        List<SysDictType> types = dictService.listAllDictTypes();
        return Result.success(types);
    }

    @Operation(summary = "根据ID查询字典类型详情")
    @GetMapping("/type/{id}")
    public Result<SysDictType> getDictTypeById(
            @Parameter(description = "字典类型ID") @PathVariable Long id) {
        SysDictType dictType = dictService.getDictTypeById(id);
        return Result.success(dictType);
    }

    @Operation(summary = "创建字典类型")
    @PostMapping("/type")
    public Result<Void> createDictType(
            @Parameter(description = "字典类型信息") @Valid @RequestBody SysDictType dictType) {
        boolean success = dictService.createDictType(dictType);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新字典类型")
    @PutMapping("/type")
    public Result<Void> updateDictType(
            @Parameter(description = "字典类型信息") @Valid @RequestBody SysDictType dictType) {
        boolean success = dictService.updateDictType(dictType);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/type/{ids}")
    public Result<Void> deleteDictTypes(
            @Parameter(description = "字典类型ID列表") @PathVariable List<Long> ids) {
        boolean success = dictService.deleteDictTypes(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    // ==================== 字典数据相关接口 ====================

    @Operation(summary = "根据字典类型查询字典数据列表")
    @GetMapping("/data/type/{dictType}")
    public Result<List<SysDictData>> getDictDataByType(
            @Parameter(description = "字典类型") @PathVariable String dictType) {
        List<SysDictData> dataList = dictService.getDictDataByType(dictType);
        return Result.success(dataList);
    }

    @Operation(summary = "分页查询字典数据列表")
    @GetMapping("/data/page")
    public Result<IPage<SysDictData>> pageDictData(
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType,
            @Parameter(description = "字典标签") @RequestParam(required = false) String dictLabel,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<SysDictData> page = dictService.pageDictData(dictType, dictLabel, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询字典数据详情")
    @GetMapping("/data/{id}")
    public Result<SysDictData> getDictDataById(
            @Parameter(description = "字典数据ID") @PathVariable Long id) {
        SysDictData dictData = dictService.getDictDataById(id);
        return Result.success(dictData);
    }

    @Operation(summary = "创建字典数据")
    @PostMapping("/data")
    public Result<Void> createDictData(
            @Parameter(description = "字典数据信息") @Valid @RequestBody SysDictData dictData) {
        boolean success = dictService.createDictData(dictData);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新字典数据")
    @PutMapping("/data")
    public Result<Void> updateDictData(
            @Parameter(description = "字典数据信息") @Valid @RequestBody SysDictData dictData) {
        boolean success = dictService.updateDictData(dictData);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除字典数据")
    @DeleteMapping("/data/{ids}")
    public Result<Void> deleteDictData(
            @Parameter(description = "字典数据ID列表") @PathVariable List<Long> ids) {
        boolean success = dictService.deleteDictData(ids);
        return success ? Result.success() : Result.error("删除失败");
    }
}
