package com.crm.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.customer.domain.entity.CrmFollowUp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 跟进记录Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmFollowUpMapper extends BaseMapper<CrmFollowUp> {

    /**
     * 根据客户ID获取跟进记录列表（按时间倒序）
     *
     * @param customerId 客户ID
     * @return 跟进记录列表
     */
    List<CrmFollowUp> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 查询今日需跟进的客户列表
     *
     * @param todayStart 今日开始时间
     * @param todayEnd 今日结束时间
     * @return 跟进记录列表
     */
    List<CrmFollowUp> selectTodayFollowUps(
            @Param("todayStart") LocalDateTime todayStart,
            @Param("todayEnd") LocalDateTime todayEnd);

    /**
     * 查询即将到期的跟进提醒
     *
     * @param now 当前时间
     * @param endTime 结束时间
     * @return 跟进记录列表
     */
    List<CrmFollowUp> selectUpcomingReminders(
            @Param("now") LocalDateTime now,
            @Param("endTime") LocalDateTime endTime);
}
