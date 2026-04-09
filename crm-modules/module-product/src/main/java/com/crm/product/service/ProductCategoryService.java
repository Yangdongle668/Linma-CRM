package com.crm.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.product.domain.dto.ProductCategoryCreateDTO;
import com.crm.product.domain.dto.ProductCategoryQuery;
import com.crm.product.domain.dto.ProductCategoryUpdateDTO;
import com.crm.product.domain.entity.CrmProductCategory;
import com.crm.product.domain.vo.ProductCategoryTreeVO;

import java.util.List;

/**
 * 产品分类Service接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ProductCategoryService extends IService<CrmProductCategory> {

    /**
     * 获取分类树
     *
     * @return 分类树列表
     */
    List<ProductCategoryTreeVO> getCategoryTree();

    /**
     * 根据父ID获取子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<ProductCategoryTreeVO> getChildrenByParentId(Long parentId);

    /**
     * 创建分类
     *
     * @param dto 创建DTO
     * @return 分类ID
     */
    Long createCategory(ProductCategoryCreateDTO dto);

    /**
     * 更新分类
     *
     * @param dto 更新DTO
     */
    void updateCategory(ProductCategoryUpdateDTO dto);

    /**
     * 删除分类(级联删除子分类)
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 查询分类列表
     *
     * @param query 查询条件
     * @return 分类列表
     */
    List<CrmProductCategory> listCategories(ProductCategoryQuery query);

    /**
     * 检查分类编码是否唯一
     *
     * @param categoryCode 分类编码
     * @param excludeId 排除的ID
     * @return 是否唯一
     */
    boolean checkCategoryCodeUnique(String categoryCode, Long excludeId);
}
