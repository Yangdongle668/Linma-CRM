package com.crm.contract.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.core.domain.Result;
import com.crm.contract.domain.dto.ContractTemplateCreateDTO;
import com.crm.contract.domain.dto.ContractTemplateQuery;
import com.crm.contract.domain.dto.ContractTemplateUpdateDTO;
import com.crm.contract.domain.vo.ContractTemplateVO;
import com.crm.contract.service.ContractTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同模板管理Controller
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "合同模板管理", description = "合同模板相关接口")
@RestController
@RequestMapping("/contract/template")
@RequiredArgsConstructor
public class ContractTemplateController {

    private final ContractTemplateService templateService;

    /**
     * 分页查询模板
     */
    @Operation(summary = "分页查询模板", description = "根据条件分页查询合同模板列表")
    @GetMapping("/page")
    public Result<IPage<ContractTemplateVO>> getTemplatePage(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            ContractTemplateQuery query) {
        Page<ContractTemplateVO> page = new Page<>(pageNum, pageSize);
        IPage<ContractTemplateVO> result = templateService.getTemplatePage(page, query);
        return Result.success(result);
    }

    /**
     * 获取模板详情
     */
    @Operation(summary = "获取模板详情", description = "根据ID获取模板详细信息")
    @GetMapping("/{id}")
    public Result<ContractTemplateVO> getTemplate(
            @Parameter(description = "模板ID") @PathVariable Long id) {
        ContractTemplateVO template = templateService.getTemplateById(id);
        return Result.success(template);
    }

    /**
     * 创建模板
     */
    @Operation(summary = "创建模板", description = "创建新的合同模板")
    @PostMapping
    public Result<Long> createTemplate(@Valid @RequestBody ContractTemplateCreateDTO dto) {
        Long templateId = templateService.createTemplate(dto);
        return Result.success(templateId);
    }

    /**
     * 更新模板
     */
    @Operation(summary = "更新模板", description = "更新合同模板信息")
    @PutMapping
    public Result<Void> updateTemplate(@Valid @RequestBody ContractTemplateUpdateDTO dto) {
        templateService.updateTemplate(dto);
        return Result.success();
    }

    /**
     * 删除模板
     */
    @Operation(summary = "删除模板", description = "批量删除合同模板")
    @DeleteMapping
    public Result<Void> deleteTemplates(
            @Parameter(description = "模板ID列表") @RequestBody List<Long> ids) {
        templateService.deleteTemplates(ids);
        return Result.success();
    }

    /**
     * 查询模板列表
     */
    @Operation(summary = "查询模板列表", description = "根据条件查询合同模板列表")
    @GetMapping("/list")
    public Result<List<ContractTemplateVO>> listTemplates(ContractTemplateQuery query) {
        List<ContractTemplateVO> templates = templateService.listTemplates(query);
        return Result.success(templates);
    }

    /**
     * 获取默认模板
     */
    @Operation(summary = "获取默认模板", description = "获取指定类型和语言的默认模板")
    @GetMapping("/default")
    public Result<com.crm.contract.domain.entity.CrmContractTemplate> getDefaultTemplate(
            @Parameter(description = "合同类型") @RequestParam String contractType,
            @Parameter(description = "语言") @RequestParam(defaultValue = "zh") String language) {
        com.crm.contract.domain.entity.CrmContractTemplate template = 
            templateService.getDefaultTemplate(contractType, language);
        return Result.success(template);
    }

    /**
     * 设置默认模板
     */
    @Operation(summary = "设置默认模板", description = "将指定模板设置为默认模板")
    @PostMapping("/{id}/set-default")
    public Result<Void> setDefaultTemplate(
            @Parameter(description = "模板ID") @PathVariable Long id) {
        templateService.setDefaultTemplate(id);
        return Result.success();
    }

    /**
     * 检查模板编码唯一性
     */
    @Operation(summary = "检查模板编码唯一性", description = "检查模板编码是否已存在")
    @GetMapping("/check-code")
    public Result<Boolean> checkTemplateCodeUnique(
            @Parameter(description = "模板编码") @RequestParam String templateCode,
            @Parameter(description = "排除的模板ID") @RequestParam(required = false) Long excludeId) {
        boolean unique = templateService.checkTemplateCodeUnique(templateCode, excludeId);
        return Result.success(unique);
    }
}
