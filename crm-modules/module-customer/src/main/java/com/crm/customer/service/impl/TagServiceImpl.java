package com.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.customer.domain.dto.TagCreateDTO;
import com.crm.customer.domain.entity.CrmCustomerTag;
import com.crm.customer.domain.entity.CrmTag;
import com.crm.customer.domain.vo.TagVO;
import com.crm.customer.mapper.CrmCustomerTagMapper;
import com.crm.customer.mapper.CrmTagMapper;
import com.crm.customer.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<CrmTagMapper, CrmTag> implements TagService {

    private final CrmTagMapper tagMapper;
    private final CrmCustomerTagMapper customerTagMapper;

    @Override
    public IPage<TagVO> pageTags(String tagName, String tagType, int pageNum, int pageSize) {
        Page<CrmTag> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CrmTag> wrapper = new LambdaQueryWrapper<>();
        
        // 标签名称模糊搜索
        if (StrUtil.isNotBlank(tagName)) {
            wrapper.like(CrmTag::getTagName, tagName);
        }
        
        // 标签类型过滤
        if (StrUtil.isNotBlank(tagType)) {
            wrapper.eq(CrmTag::getTagType, tagType);
        }
        
        wrapper.orderByDesc(CrmTag::getUsageCount)
               .orderByAsc(CrmTag::getCreatedTime);
        
        IPage<CrmTag> tagPage = this.page(page, wrapper);
        return tagPage.convert(this::convertToVO);
    }

    @Override
    public List<TagVO> getAllTags() {
        LambdaQueryWrapper<CrmTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CrmTag::getUsageCount);
        
        List<CrmTag> tags = this.list(wrapper);
        return tags.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public TagVO getTagById(Long id) {
        CrmTag tag = this.getById(id);
        if (tag == null) {
            return null;
        }
        return convertToVO(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTag(TagCreateDTO dto) {
        // 检查标签名称是否重复
        LambdaQueryWrapper<CrmTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmTag::getTagName, dto.getTagName());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("标签名称已存在");
        }

        CrmTag tag = new CrmTag();
        BeanUtil.copyProperties(dto, tag);
        
        // 设置默认值
        if (StrUtil.isBlank(tag.getTagType())) {
            tag.setTagType("manual");
        }
        if (tag.getUsageCount() == null) {
            tag.setUsageCount(0);
        }
        
        return this.save(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTag(Long id, String tagName, String tagColor, String autoRule) {
        CrmTag tag = this.getById(id);
        if (tag == null) {
            throw new RuntimeException("标签不存在");
        }

        // 如果修改了标签名称，检查是否重复
        if (StrUtil.isNotBlank(tagName) && !tagName.equals(tag.getTagName())) {
            LambdaQueryWrapper<CrmTag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CrmTag::getTagName, tagName);
            if (this.count(wrapper) > 0) {
                throw new RuntimeException("标签名称已存在");
            }
            tag.setTagName(tagName);
        }

        if (StrUtil.isNotBlank(tagColor)) {
            tag.setTagColor(tagColor);
        }
        
        if (autoRule != null) {
            tag.setAutoRule(autoRule);
        }
        
        return this.updateById(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTags(List<Long> ids) {
        // TODO: 删除标签前需要检查是否有客户使用该标签
        
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTagsToCustomer(Long customerId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return false;
        }

        // 批量插入客户标签关联
        int result = customerTagMapper.batchInsert(customerId, tagIds);
        
        // 更新标签使用次数
        for (Long tagId : tagIds) {
            CrmTag tag = this.getById(tagId);
            if (tag != null) {
                tag.setUsageCount(tag.getUsageCount() + 1);
                this.updateById(tag);
            }
        }
        
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTagsFromCustomer(Long customerId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return false;
        }

        // 删除客户标签关联
        for (Long tagId : tagIds) {
            CrmCustomerTag customerTag = new CrmCustomerTag();
            customerTag.setCustomerId(customerId);
            customerTag.setTagId(tagId);
            customerTagMapper.deleteById(customerTag);
            
            // 更新标签使用次数
            CrmTag tag = this.getById(tagId);
            if (tag != null && tag.getUsageCount() > 0) {
                tag.setUsageCount(tag.getUsageCount() - 1);
                this.updateById(tag);
            }
        }
        
        return true;
    }

    @Override
    public List<TagVO> getCustomerTags(Long customerId) {
        List<Long> tagIds = customerTagMapper.selectTagIdsByCustomerId(customerId);
        if (tagIds == null || tagIds.isEmpty()) {
            return List.of();
        }
        
        List<CrmTag> tags = this.listByIds(tagIds);
        return tags.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeAutoTagging() {
        // 查询所有自动标签
        LambdaQueryWrapper<CrmTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmTag::getTagType, "auto");
        
        List<CrmTag> autoTags = this.list(wrapper);
        if (autoTags.isEmpty()) {
            return 0;
        }
        
        int taggedCustomerCount = 0;
        
        // TODO: 实现自动打标逻辑
        // 根据每个标签的autoRule规则，匹配符合条件的客户并自动打标签
        
        for (CrmTag tag : autoTags) {
            if (StrUtil.isBlank(tag.getAutoRule())) {
                continue;
            }
            
            // 解析自动打标规则（JSON格式）
            // 根据规则条件查询符合条件的客户
            // 为客户添加标签
            
            log.info("执行自动打标规则：{}", tag.getTagName());
        }
        
        return taggedCustomerCount;
    }

    /**
     * 实体转VO
     */
    private TagVO convertToVO(CrmTag tag) {
        TagVO vo = new TagVO();
        BeanUtil.copyProperties(tag, vo);
        return vo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TagServiceImpl.class);
}
