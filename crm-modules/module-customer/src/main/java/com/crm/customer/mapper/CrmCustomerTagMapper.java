package com.crm.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.customer.domain.entity.CrmCustomerTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户标签关联Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmCustomerTagMapper extends BaseMapper<CrmCustomerTag> {

    /**
     * 根据客户ID获取标签ID列表
     *
     * @param customerId 客户ID
     * @return 标签ID列表
     */
    List<Long> selectTagIdsByCustomerId(@Param("customerId") Long customerId);

    /**
     * 删除客户的所有标签关联
     *
     * @param customerId 客户ID
     * @return 影响行数
     */
    int deleteByCustomerId(@Param("customerId") Long customerId);

    /**
     * 批量插入客户标签关联
     *
     * @param customerId 客户ID
     * @param tagIds 标签ID列表
     * @return 影响行数
     */
    int batchInsert(@Param("customerId") Long customerId, @Param("tagIds") List<Long> tagIds);
}
