package com.crm.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.product.domain.entity.CrmProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品分类Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<CrmProductCategory> {

    /**
     * 查询分类树
     *
     * @param parentId 父分类ID
     * @return 分类列表
     */
    List<CrmProductCategory> selectCategoryTree(@Param("parentId") Long parentId);

    /**
     * 根据分类编码查询
     *
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    CrmProductCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);
}
