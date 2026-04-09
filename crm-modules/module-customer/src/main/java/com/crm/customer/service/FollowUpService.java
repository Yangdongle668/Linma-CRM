package com.crm.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.customer.domain.dto.FollowUpCreateDTO;
import com.crm.customer.domain.entity.CrmFollowUp;
import com.crm.customer.domain.vo.FollowUpVO;

import java.util.List;

/**
 * 跟进记录服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface FollowUpService extends IService<CrmFollowUp> {

    /**
     * 分页查询跟进记录列表
     *
     * @param customerId 客户ID（可选）
     * @param followUserId 跟进人ID（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 跟进记录分页数据
     */
    IPage<FollowUpVO> pageFollowUps(Long customerId, Long followUserId, int pageNum, int pageSize);

    /**
     * 根据ID查询跟进记录详情
     *
     * @param id 跟进记录ID
     * @return 跟进记录详情
     */
    FollowUpVO getFollowUpById(Long id);

    /**
     * 获取客户的跟进记录时间轴
     *
     * @param customerId 客户ID
     * @return 跟进记录列表（按时间倒序）
     */
    List<FollowUpVO> getFollowUpTimeline(Long customerId);

    /**
     * 创建跟进记录
     *
     * @param dto 跟进记录创建信息
     * @return 是否成功
     */
    boolean createFollowUp(FollowUpCreateDTO dto);

    /**
     * 更新跟进记录
     *
     * @param id 跟进记录ID
     * @param followContent 跟进内容
     * @param nextPlan 下一步计划
     * @param nextFollowTime 下次跟进时间
     * @param satisfaction 客户满意度
     * @return 是否成功
     */
    boolean updateFollowUp(Long id, String followContent, String nextPlan, String nextFollowTime, Integer satisfaction);

    /**
     * 删除跟进记录
     *
     * @param ids 跟进记录ID列表
     * @return 是否成功
     */
    boolean deleteFollowUps(List<Long> ids);

    /**
     * 设置跟进提醒
     *
     * @param followUpId 跟进记录ID
     * @param nextFollowTime 下次跟进时间
     * @return 是否成功
     */
    boolean setReminder(Long followUpId, String nextFollowTime);

    /**
     * 今日需跟进客户列表
     *
     * @return 今日需跟进的跟进记录列表
     */
    List<FollowUpVO> getTodayFollowUps();

    /**
     * 即将到期的跟进提醒
     *
     * @param hours 未来多少小时
     * @return 即将到期的跟进记录列表
     */
    List<FollowUpVO> getUpcomingReminders(Integer hours);
}
