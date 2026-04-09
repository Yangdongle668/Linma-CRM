package com.crm.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.customer.domain.dto.TagCreateDTO;
import com.crm.customer.domain.entity.CrmTag;
import com.crm.customer.domain.vo.TagVO;

import java.util.List;

/**
 * 标签服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface TagService extends IService<CrmTag> {

    /**
     * 分页查询标签列表
     *
     * @param tagName 标签名称（模糊搜索）
     * @param tagType 标签类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 标签分页数据
     */
    IPage<TagVO> pageTags(String tagName, String tagType, int pageNum, int pageSize);

    /**
     * 获取所有标签列表
     *
     * @return 标签列表
     */
    List<TagVO> getAllTags();

    /**
     * 根据ID查询标签详情
     *
     * @param id 标签ID
     * @return 标签详情
     */
    TagVO getTagById(Long id);

    /**
     * 创建标签
     *
     * @param dto 标签创建信息
     * @return 是否成功
     */
    boolean createTag(TagCreateDTO dto);

    /**
     * 更新标签
     *
     * @param id 标签ID
     * @param tagName 标签名称
     * @param tagColor 标签颜色
     * @param autoRule 自动打标规则
     * @return 是否成功
     */
    boolean updateTag(Long id, String tagName, String tagColor, String autoRule);

    /**
     * 删除标签
     *
     * @param ids 标签ID列表
     * @return 是否成功
     */
    boolean deleteTags(List<Long> ids);

    /**
     * 为客户打标签
     *
     * @param customerId 客户ID
     * @param tagIds 标签ID列表
     * @return 是否成功
     */
    boolean addTagsToCustomer(Long customerId, List<Long> tagIds);

    /**
     * 移除客户的标签
     *
     * @param customerId 客户ID
     * @param tagIds 标签ID列表
     * @return 是否成功
     */
    boolean removeTagsFromCustomer(Long customerId, List<Long> tagIds);

    /**
     * 获取客户的标签列表
     *
     * @param customerId 客户ID
     * @return 标签列表
     */
    List<TagVO> getCustomerTags(Long customerId);

    /**
     * 执行自动打标规则（定时任务调用）
     *
     * @return 被打标的客户数量
     */
    int executeAutoTagging();
}
