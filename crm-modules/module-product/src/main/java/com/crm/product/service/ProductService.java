package com.crm.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.product.domain.dto.ProductCreateDTO;
import com.crm.product.domain.dto.ProductQuery;
import com.crm.product.domain.dto.ProductUpdateDTO;
import com.crm.product.domain.entity.CrmProduct;
import com.crm.product.domain.vo.ProductPriceVO;
import com.crm.product.domain.vo.ProductVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品Service接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ProductService extends IService<CrmProduct> {

    /**
     * 分页查询产品
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 产品分页列表
     */
    IPage<ProductVO> getProductPage(Page<ProductVO> page, ProductQuery query);

    /**
     * 根据ID查询产品详情
     *
     * @param id 产品ID
     * @return 产品详情
     */
    ProductVO getProductById(Long id);

    /**
     * 创建产品
     *
     * @param dto 创建DTO
     * @return 产品ID
     */
    Long createProduct(ProductCreateDTO dto);

    /**
     * 更新产品
     *
     * @param dto 更新DTO
     */
    void updateProduct(ProductUpdateDTO dto);

    /**
     * 删除产品
     *
     * @param ids 产品ID列表
     */
    void deleteProducts(List<Long> ids);

    /**
     * 生成产品编号
     *
     * @return 产品编号
     */
    String generateProductNo();

    /**
     * 获取产品价格信息(含多币种换算)
     *
     * @param productId 产品ID
     * @return 价格信息
     */
    ProductPriceVO getProductPrice(Long productId);

    /**
     * 更新产品价格
     *
     * @param productId 产品ID
     * @param costPrice 成本价
     * @param factoryPrice 出厂价
     * @param fobPrice FOB价格
     * @param cifPrice CIF价格
     */
    void updateProductPrice(Long productId, BigDecimal costPrice, BigDecimal factoryPrice, 
                           BigDecimal fobPrice, BigDecimal cifPrice);

    /**
     * 更新库存
     *
     * @param productId 产品ID
     * @param quantity 变更数量(正数增加,负数减少)
     */
    void updateStock(Long productId, Integer quantity);

    /**
     * 批量导入产品
     *
     * @param productList 产品列表
     * @return 导入结果
     */
    String importProducts(List<ProductCreateDTO> productList);

    /**
     * 导出产品
     *
     * @param query 查询条件
     * @return 产品列表
     */
    List<ProductVO> exportProducts(ProductQuery query);

    /**
     * 检查SKU是否唯一
     *
     * @param sku SKU编码
     * @param excludeId 排除的ID
     * @return 是否唯一
     */
    boolean checkSkuUnique(String sku, Long excludeId);

    /**
     * 检查产品编号是否唯一
     *
     * @param productNo 产品编号
     * @return 是否唯一
     */
    boolean checkProductNoUnique(String productNo);
}
