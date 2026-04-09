package com.crm.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.core.domain.Result;
import com.crm.product.domain.dto.ProductCreateDTO;
import com.crm.product.domain.dto.ProductQuery;
import com.crm.product.domain.dto.ProductUpdateDTO;
import com.crm.product.domain.vo.ProductPriceVO;
import com.crm.product.domain.vo.ProductVO;
import com.crm.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品管理Controller
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Tag(name = "产品管理", description = "产品相关接口")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 分页查询产品
     */
    @Operation(summary = "分页查询产品", description = "根据条件分页查询产品列表")
    @GetMapping("/page")
    public Result<IPage<ProductVO>> getProductPage(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            ProductQuery query) {
        Page<ProductVO> page = new Page<>(pageNum, pageSize);
        IPage<ProductVO> result = productService.getProductPage(page, query);
        return Result.success(result);
    }

    /**
     * 获取产品详情
     */
    @Operation(summary = "获取产品详情", description = "根据ID获取产品详细信息")
    @GetMapping("/{id}")
    public Result<ProductVO> getProduct(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        ProductVO product = productService.getProductById(id);
        return Result.success(product);
    }

    /**
     * 创建产品
     */
    @Operation(summary = "创建产品", description = "创建新产品")
    @PostMapping
    public Result<Long> createProduct(@Valid @RequestBody ProductCreateDTO dto) {
        Long productId = productService.createProduct(dto);
        return Result.success(productId);
    }

    /**
     * 更新产品
     */
    @Operation(summary = "更新产品", description = "更新产品信息")
    @PutMapping
    public Result<Void> updateProduct(@Valid @RequestBody ProductUpdateDTO dto) {
        productService.updateProduct(dto);
        return Result.success();
    }

    /**
     * 删除产品
     */
    @Operation(summary = "删除产品", description = "批量删除产品")
    @DeleteMapping
    public Result<Void> deleteProducts(
            @Parameter(description = "产品ID列表") @RequestBody List<Long> ids) {
        productService.deleteProducts(ids);
        return Result.success();
    }

    /**
     * 生成产品编号
     */
    @Operation(summary = "生成产品编号", description = "自动生成产品编号")
    @GetMapping("/generate-no")
    public Result<String> generateProductNo() {
        String productNo = productService.generateProductNo();
        return Result.success(productNo);
    }

    /**
     * 获取产品价格信息
     */
    @Operation(summary = "获取产品价格", description = "获取产品价格信息(含多币种换算)")
    @GetMapping("/{id}/price")
    public Result<ProductPriceVO> getProductPrice(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        ProductPriceVO priceVO = productService.getProductPrice(id);
        return Result.success(priceVO);
    }

    /**
     * 更新产品价格
     */
    @Operation(summary = "更新产品价格", description = "更新产品的价格体系")
    @PutMapping("/{id}/price")
    public Result<Void> updateProductPrice(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @Parameter(description = "成本价") @RequestParam BigDecimal costPrice,
            @Parameter(description = "出厂价") @RequestParam BigDecimal factoryPrice,
            @Parameter(description = "FOB价格") @RequestParam BigDecimal fobPrice,
            @Parameter(description = "CIF价格") @RequestParam BigDecimal cifPrice) {
        productService.updateProductPrice(id, costPrice, factoryPrice, fobPrice, cifPrice);
        return Result.success();
    }

    /**
     * 更新库存
     */
    @Operation(summary = "更新库存", description = "增加或减少产品库存")
    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @Parameter(description = "变更数量(正数增加,负数减少)") @RequestParam Integer quantity) {
        productService.updateStock(id, quantity);
        return Result.success();
    }

    /**
     * 导入产品
     */
    @Operation(summary = "导入产品", description = "批量导入产品数据")
    @PostMapping("/import")
    public Result<String> importProducts(@RequestBody List<ProductCreateDTO> productList) {
        String result = productService.importProducts(productList);
        return Result.success(result);
    }

    /**
     * 导出产品
     */
    @Operation(summary = "导出产品", description = "根据条件导出产品数据")
    @GetMapping("/export")
    public Result<List<ProductVO>> exportProducts(ProductQuery query) {
        List<ProductVO> products = productService.exportProducts(query);
        return Result.success(products);
    }

    /**
     * 检查SKU唯一性
     */
    @Operation(summary = "检查SKU唯一性", description = "检查SKU编码是否已存在")
    @GetMapping("/check-sku")
    public Result<Boolean> checkSkuUnique(
            @Parameter(description = "SKU编码") @RequestParam String sku,
            @Parameter(description = "排除的产品ID") @RequestParam(required = false) Long excludeId) {
        boolean unique = productService.checkSkuUnique(sku, excludeId);
        return Result.success(unique);
    }
}
