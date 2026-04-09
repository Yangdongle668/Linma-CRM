package com.crm.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.product.domain.entity.CrmProduct;
import com.crm.product.domain.dto.ProductQuery;
import com.crm.product.domain.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface ProductMapper extends BaseMapper<CrmProduct> {

    /**
     * 分页查询产品(带关联信息)
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 产品列表
     */
    Page<ProductVO> selectProductPage(Page<ProductVO> page, @Param("query") ProductQuery query);

    /**
     * 根据产品编号查询
     *
     * @param productNo 产品编号
     * @return 产品信息
     */
    CrmProduct selectByProductNo(@Param("productNo") String productNo);

    /**
     * 根据SKU查询
     *
     * @param sku SKU编码
     * @return 产品信息
     */
    CrmProduct selectBySku(@Param("sku") String sku);

    /**
     * 批量导出产品
     *
     * @param query 查询条件
     * @return 产品列表
     */
    List<ProductVO> selectProductForExport(@Param("query") ProductQuery query);
}
