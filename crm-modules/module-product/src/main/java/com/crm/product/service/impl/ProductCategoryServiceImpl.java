package com.crm.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.product.domain.dto.ProductCategoryCreateDTO;
import com.crm.product.domain.dto.ProductCategoryQuery;
import com.crm.product.domain.dto.ProductCategoryUpdateDTO;
import com.crm.product.domain.entity.CrmProductCategory;
import com.crm.product.domain.vo.ProductCategoryTreeVO;
import com.crm.product.mapper.ProductCategoryMapper;
import com.crm.product.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品分类Service实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, CrmProductCategory> 
        implements ProductCategoryService {

    private final ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategoryTreeVO> getCategoryTree() {
        // 获取所有正常状态的分类
        LambdaQueryWrapper<CrmProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmProductCategory::getStatus, 1)
               .orderByAsc(CrmProductCategory::getSortOrder);
        List<CrmProductCategory> allCategories = this.list(wrapper);
        
        // 构建树形结构
        return buildTree(allCategories, 0L);
    }

    @Override
    public List<ProductCategoryTreeVO> getChildrenByParentId(Long parentId) {
        LambdaQueryWrapper<CrmProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmProductCategory::getParentId, parentId)
               .eq(CrmProductCategory::getStatus, 1)
               .orderByAsc(CrmProductCategory::getSortOrder);
        List<CrmProductCategory> categories = this.list(wrapper);
        
        return categories.stream().map(this::convertToTreeVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(ProductCategoryCreateDTO dto) {
        // 检查分类编码唯一性
        if (StrUtil.isNotBlank(dto.getCategoryCode())) {
            if (!checkCategoryCodeUnique(dto.getCategoryCode(), null)) {
                throw new BusinessException("分类编码已存在");
            }
        }

        CrmProductCategory category = BeanUtil.copyProperties(dto, CrmProductCategory.class);
        
        // 计算层级
        if (dto.getParentId() != null && dto.getParentId() > 0) {
            CrmProductCategory parent = this.getById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException("父分类不存在");
            }
            category.setLevel(parent.getLevel() + 1);
        } else {
            category.setParentId(0L);
            category.setLevel(1);
        }

        this.save(category);
        log.info("创建产品分类成功, ID: {}, 名称: {}", category.getId(), category.getCategoryNameCn());
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(ProductCategoryUpdateDTO dto) {
        CrmProductCategory existCategory = this.getById(dto.getId());
        if (existCategory == null) {
            throw new BusinessException("分类不存在");
        }

        // 检查分类编码唯一性
        if (StrUtil.isNotBlank(dto.getCategoryCode())) {
            if (!checkCategoryCodeUnique(dto.getCategoryCode(), dto.getId())) {
                throw new BusinessException("分类编码已存在");
            }
        }

        // 不能将自己设置为自己的子分类
        if (dto.getParentId() != null && dto.getParentId().equals(dto.getId())) {
            throw new BusinessException("不能将自己设置为父分类");
        }

        CrmProductCategory category = BeanUtil.copyProperties(dto, CrmProductCategory.class);
        
        // 更新层级
        if (dto.getParentId() != null && dto.getParentId() > 0) {
            CrmProductCategory parent = this.getById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException("父分类不存在");
            }
            category.setLevel(parent.getLevel() + 1);
        } else {
            category.setParentId(0L);
            category.setLevel(1);
        }

        this.updateById(category);
        log.info("更新产品分类成功, ID: {}", category.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        CrmProductCategory category = this.getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 递归删除子分类
        deleteChildren(id);
        
        log.info("删除产品分类成功, ID: {}, 名称: {}", id, category.getCategoryNameCn());
    }

    @Override
    public List<CrmProductCategory> listCategories(ProductCategoryQuery query) {
        LambdaQueryWrapper<CrmProductCategory> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(query.getCategoryName())) {
            wrapper.and(w -> w.like(CrmProductCategory::getCategoryNameCn, query.getCategoryName())
                             .or()
                             .like(CrmProductCategory::getCategoryNameEn, query.getCategoryName()));
        }
        
        if (StrUtil.isNotBlank(query.getCategoryCode())) {
            wrapper.eq(CrmProductCategory::getCategoryCode, query.getCategoryCode());
        }
        
        if (query.getStatus() != null) {
            wrapper.eq(CrmProductCategory::getStatus, query.getStatus());
        }
        
        if (query.getParentId() != null) {
            wrapper.eq(CrmProductCategory::getParentId, query.getParentId());
        }
        
        wrapper.orderByAsc(CrmProductCategory::getSortOrder);
        
        return this.list(wrapper);
    }

    @Override
    public boolean checkCategoryCodeUnique(String categoryCode, Long excludeId) {
        LambdaQueryWrapper<CrmProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmProductCategory::getCategoryCode, categoryCode);
        if (excludeId != null) {
            wrapper.ne(CrmProductCategory::getId, excludeId);
        }
        return this.count(wrapper) == 0;
    }

    /**
     * 递归删除子分类
     *
     * @param parentId 父分类ID
     */
    private void deleteChildren(Long parentId) {
        LambdaQueryWrapper<CrmProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmProductCategory::getParentId, parentId);
        List<CrmProductCategory> children = this.list(wrapper);
        
        for (CrmProductCategory child : children) {
            deleteChildren(child.getId());
        }
        
        this.removeById(parentId);
    }

    /**
     * 构建树形结构
     *
     * @param allCategories 所有分类
     * @param parentId 父分类ID
     * @return 树形结构列表
     */
    private List<ProductCategoryTreeVO> buildTree(List<CrmProductCategory> allCategories, Long parentId) {
        List<ProductCategoryTreeVO> tree = new ArrayList<>();
        
        for (CrmProductCategory category : allCategories) {
            if (category.getParentId().equals(parentId)) {
                ProductCategoryTreeVO node = convertToTreeVO(category);
                List<ProductCategoryTreeVO> children = buildTree(allCategories, category.getId());
                node.setChildren(children);
                tree.add(node);
            }
        }
        
        return tree;
    }

    /**
     * 转换为树VO
     *
     * @param category 分类实体
     * @return 树VO
     */
    private ProductCategoryTreeVO convertToTreeVO(CrmProductCategory category) {
        ProductCategoryTreeVO vo = new ProductCategoryTreeVO();
        BeanUtil.copyProperties(category, vo);
        return vo;
    }
}
