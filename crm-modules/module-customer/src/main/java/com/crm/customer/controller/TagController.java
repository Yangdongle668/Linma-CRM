package com.crm.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.crm.common.core.domain.Result;
import com.crm.customer.domain.dto.TagCreateDTO;
import com.crm.customer.domain.vo.TagVO;
import com.crm.customer.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签管理控制器
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "标签管理", description = "标签的增删改查、客户打标等功能")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "分页查询标签列表")
    @GetMapping("/page")
    public Result<IPage<TagVO>> pageTags(
            @Parameter(description = "标签名称") @RequestParam(required = false) String tagName,
            @Parameter(description = "标签类型") @RequestParam(required = false) String tagType,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<TagVO> page = tagService.pageTags(tagName, tagType, pageNum, pageSize);
        return Result.success(page);
    }

    @Operation(summary = "获取所有标签列表")
    @GetMapping("/all")
    public Result<List<TagVO>> getAllTags() {
        List<TagVO> tags = tagService.getAllTags();
        return Result.success(tags);
    }

    @Operation(summary = "根据ID查询标签详情")
    @GetMapping("/{id}")
    public Result<TagVO> getTagById(
            @Parameter(description = "标签ID") @PathVariable Long id) {
        TagVO tag = tagService.getTagById(id);
        return Result.success(tag);
    }

    @Operation(summary = "创建标签")
    @PostMapping
    public Result<Void> createTag(
            @Parameter(description = "标签信息") @Valid @RequestBody TagCreateDTO dto) {
        boolean success = tagService.createTag(dto);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新标签")
    @PutMapping("/{id}")
    public Result<Void> updateTag(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Parameter(description = "标签名称") @RequestParam(required = false) String tagName,
            @Parameter(description = "标签颜色") @RequestParam(required = false) String tagColor,
            @Parameter(description = "自动打标规则") @RequestParam(required = false) String autoRule) {
        boolean success = tagService.updateTag(id, tagName, tagColor, autoRule);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{ids}")
    public Result<Void> deleteTags(
            @Parameter(description = "标签ID列表") @PathVariable List<Long> ids) {
        boolean success = tagService.deleteTags(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "为客户打标签")
    @PostMapping("/customer/{customerId}/add")
    public Result<Void> addTagsToCustomer(
            @Parameter(description = "客户ID") @PathVariable Long customerId,
            @Parameter(description = "标签ID列表") @RequestBody List<Long> tagIds) {
        boolean success = tagService.addTagsToCustomer(customerId, tagIds);
        return success ? Result.success() : Result.error("添加失败");
    }

    @Operation(summary = "移除客户的标签")
    @PostMapping("/customer/{customerId}/remove")
    public Result<Void> removeTagsFromCustomer(
            @Parameter(description = "客户ID") @PathVariable Long customerId,
            @Parameter(description = "标签ID列表") @RequestBody List<Long> tagIds) {
        boolean success = tagService.removeTagsFromCustomer(customerId, tagIds);
        return success ? Result.success() : Result.error("移除失败");
    }

    @Operation(summary = "获取客户的标签列表")
    @GetMapping("/customer/{customerId}")
    public Result<List<TagVO>> getCustomerTags(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<TagVO> tags = tagService.getCustomerTags(customerId);
        return Result.success(tags);
    }

    @Operation(summary = "执行自动打标规则")
    @PostMapping("/auto-tagging")
    public Result<Integer> executeAutoTagging() {
        int count = tagService.executeAutoTagging();
        return Result.success(count);
    }
}
