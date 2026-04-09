package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 首页仪表盘服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface DashboardService {

    /**
     * 获取今日待办事项
     *
     * @param userId 用户ID
     * @return 待办事项列表
     */
    List<Map<String, Object>> getTodayTodos(Long userId);

    /**
     * 获取本月业绩数据
     *
     * @param userId 用户ID
     * @return 本月业绩数据
     */
    Map<String, Object> getMonthlyPerformance(Long userId);

    /**
     * 获取关键指标
     *
     * @return 关键指标Map
     */
    Map<String, Object> getKeyMetrics();

    /**
     * 获取仪表盘完整数据
     *
     * @param userId 用户ID
     * @return 仪表盘数据
     */
    Map<String, Object> getDashboardData(Long userId);
}
