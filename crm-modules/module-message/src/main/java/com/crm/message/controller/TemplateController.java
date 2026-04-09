package com.crm.message.controller;

import com.crm.common.core.domain.Result;
import com.crm.message.domain.dto.TemplateCreateDTO;
import com.crm.message.domain.entity.MsgTemplate;
import com.crm.message.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息模板管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "消息模板管理", description = "模板的增删改查、模板渲染等功能")
@RestController
@RequestMapping("/message/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "创建模板")
    @PostMapping
    public Result<Long> createTemplate(
            @Parameter(description = "模板信息") @Valid @RequestBody TemplateCreateDTO dto) {
        Long templateId = templateService.createTemplate(dto);
        return Result.success(templateId);
    }

    @Operation(summary = "根据编码获取模板")
    @GetMapping("/code/{templateCode}")
    public Result<MsgTemplate> getByCode(
            @Parameter(description = "模板编码") @PathVariable String templateCode) {
        MsgTemplate template = templateService.getByCode(templateCode);
        return Result.success(template);
    }

    @Operation(summary = "渲染模板")
    @PostMapping("/render")
    public Result<String> renderTemplate(
            @Parameter(description = "模板编码") @RequestParam String templateCode,
            @Parameter(description = "模板变量") @RequestBody Map<String, Object> variables) {
        String content = templateService.renderTemplate(templateCode, variables);
        return Result.success(content);
    }

    @Operation(summary = "获取启用的模板列表")
    @GetMapping("/active")
    public Result<List<MsgTemplate>> getActiveTemplates(
            @Parameter(description = "模板类型") @RequestParam(required = false) String type) {
        List<MsgTemplate> templates = templateService.getActiveTemplates(type);
        return Result.success(templates);
    }
}
