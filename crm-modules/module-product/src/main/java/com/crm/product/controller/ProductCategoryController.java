package com.crm.product.controller;

import com.crm.common.core.domain.Result;
import com.crm.product.domain.dto.ProductCategoryCreateDTO;
import com.crm.product.domain.dto.ProductCategoryQuery;
import com.crm.product.domain.dto.ProductCategoryUpdateDTO;
import com.crm.product.domain.entity.CrmProductCategory;
import com.crm.product.domain.vo.ProductCategoryTreeVO;
import com.crm.product.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品分类Controller
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "产品分类管理", description = "产品分类相关接口")
@RestController
@RequestMapping("/product/category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    /**
     * 获取分类树
     */
    @Operation(summary = "获取分类树", description = "获取所有产品分类的树形结构")
    @GetMapping("/tree")
    public Result<List<ProductCategoryTreeVO>> getCategoryTree() {
        List<ProductCategoryTreeVO> tree = productCategoryService.getCategoryTree();
        return Result.success(tree);
    }

    /**
     * 根据父ID获取子分类
     */
    @Operation(summary = "获取子分类", description = "根据父分类ID获取子分类列表")
    @GetMapping("/children/{parentId}")
    public Result<List<ProductCategoryTreeVO>> getChildren(
            @Parameter(description = "父分类ID") @PathVariable Long parentId) {
        List<ProductCategoryTreeVO> children = productCategoryService.getChildrenByParentId(parentId);
        return Result.success(children);
    }

    /**
     * 查询分类列表
     */
    @Operation(summary = "查询分类列表", description = "根据条件查询产品分类列表")
    @GetMapping("/list")
    public Result<List<CrmProductCategory>> listCategories(ProductCategoryQuery query) {
        List<CrmProductCategory> list = productCategoryService.listCategories(query);
        return Result.success(list);
    }

    /**
     * 获取分类详情
     */
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详细信息")
    @GetMapping("/{id}")
    public Result<CrmProductCategory> getCategory(
            @Parameter(description = "分类ID") @PathVariable Long id) {
        CrmProductCategory category = productCategoryService.getById(id);
        return Result.success(category);
    }

    /**
     * 创建分类
     */
    @Operation(summary = "创建分类", description = "创建新的产品分类")
    @PostMapping
    public Result<Long> createCategory(@Valid @RequestBody ProductCategoryCreateDTO dto) {
        Long categoryId = productCategoryService.createCategory(dto);
        return Result.success(categoryId);
    }

    /**
     * 更新分类
     */
    @Operation(summary = "更新分类", description = "更新产品分类信息")
    @PutMapping
    public Result<Void> updateCategory(@Valid @RequestBody ProductCategoryUpdateDTO dto) {
        productCategoryService.updateCategory(dto);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @Operation(summary = "删除分类", description = "删除产品分类(会级联删除子分类)")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable Long id) {
        productCategoryService.deleteCategory(id);
        return Result.success();
    }
}
