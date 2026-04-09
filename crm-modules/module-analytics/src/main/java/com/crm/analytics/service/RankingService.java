package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 业绩排行榜服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface RankingService {

    /**
     * 销售额排行榜
     *
     * @param period 统计周期(month/quarter/year)
     * @param limit 数量限制
     * @return 销售额排行列表
     */
    List<Map<String, Object>> getSalesRanking(String period, Integer limit);

    /**
     * 订单数排行榜
     *
     * @param period 统计周期
     * @param limit 数量限制
     * @return 订单数排行列表
     */
    List<Map<String, Object>> getOrderCountRanking(String period, Integer limit);

    /**
     * 团队对比数据
     *
     * @param period 统计周期
     * @return 团队对比数据
     */
    List<Map<String, Object>> getTeamComparison(String period);

    /**
     * 新客户开发排行榜
     *
     * @param period 统计周期
     * @param limit 数量限制
     * @return 新客户开发排行
     */
    List<Map<String, Object>> getNewCustomerRanking(String period, Integer limit);
}
