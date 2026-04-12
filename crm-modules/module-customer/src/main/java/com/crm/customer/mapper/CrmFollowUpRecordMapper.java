package com.crm.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.customer.domain.entity.CrmFollowUpRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客户跟进记录Mapper
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Mapper
public interface CrmFollowUpRecordMapper extends BaseMapper<CrmFollowUpRecord> {

    /**
     * 查询客户的跟进记录列表
     */
    List<Map<String, Object>> selectFollowUpRecordsByCustomerId(@Param("customerId") Long customerId);

    /**
     * 统计客户的跟进记录数量（按类型）
     */
    List<Map<String, Object>> countFollowUpsByType(@Param("customerId") Long customerId);

    /**
     * 查询待跟进的记录
     */
    List<Map<String, Object>> selectPendingFollowUps(@Param("userId") Long userId);
}
