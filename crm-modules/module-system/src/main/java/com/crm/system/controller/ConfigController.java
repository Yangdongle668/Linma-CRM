package com.crm.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.system.domain.entity.SysConfig;
import com.crm.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "参数配置管理", description = "系统参数配置的增删改查功能")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @Operation(summary = "分页查询参数配置列表")
    @GetMapping("/page")
    public Result<IPage<SysConfig>> pageConfigs(
            @Parameter(description = "参数名称") @RequestParam(required = false) String configName,
            @Parameter(description = "参数键名") @RequestParam(required = false) String configKey,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<SysConfig> page = configService.pageConfigs(configName, configKey, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "查询所有参数配置列表")
    @GetMapping("/list")
    public Result<List<SysConfig>> listAllConfigs() {
        List<SysConfig> configs = configService.listAllConfigs();
        return Result.success(configs);
    }

    @Operation(summary = "根据ID查询参数配置详情")
    @GetMapping("/{id}")
    public Result<SysConfig> getConfigById(
            @Parameter(description = "参数配置ID") @PathVariable Long id) {
        SysConfig config = configService.getConfigById(id);
        return Result.success(config);
    }

    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping("/value/{configKey}")
    public Result<String> getConfigValueByKey(
            @Parameter(description = "参数键名") @PathVariable String configKey) {
        String value = configService.getConfigValueByKey(configKey);
        return Result.success(value);
    }

    @Operation(summary = "创建参数配置")
    @PostMapping
    public Result<Void> createConfig(
            @Parameter(description = "参数配置信息") @Valid @RequestBody SysConfig config) {
        boolean success = configService.createConfig(config);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新参数配置")
    @PutMapping
    public Result<Void> updateConfig(
            @Parameter(description = "参数配置信息") @Valid @RequestBody SysConfig config) {
        boolean success = configService.updateConfig(config);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除参数配置")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteConfigs(
            @Parameter(description = "参数配置ID列表") @PathVariable List<Long> ids) {
        boolean success = configService.deleteConfigs(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "刷新参数配置缓存")
    @PostMapping("/refresh-cache")
    public Result<Void> refreshCache() {
        boolean success = configService.refreshCache();
        return success ? Result.success() : Result.error("刷新失败");
    }
}
