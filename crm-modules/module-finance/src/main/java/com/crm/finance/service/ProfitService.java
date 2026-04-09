package com.crm.finance.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 利润核算服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ProfitService {

    /**
     * 计算单笔订单利润
     *
     * @param orderId 订单ID
     * @return 利润信息Map
     */
    Map<String, Object> calculateOrderProfit(Long orderId);

    /**
     * 获取利润统计报表
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 利润统计数据
     */
    Map<String, Object> getProfitStatistics(String startDate, String endDate);

    /**
     * 获取月度利润趋势
     *
     * @param year 年份
     * @return 月度利润列表
     */
    List<Map<String, Object>> getMonthlyProfitTrend(Integer year);

    /**
     * 获取产品利润排行
     *
     * @param limit 数量限制
     * @return 产品利润排行列表
     */
    List<Map<String, Object>> getProductProfitRanking(Integer limit);

    /**
     * 获取客户利润贡献排行
     *
     * @param limit 数量限制
     * @return 客户利润贡献列表
     */
    List<Map<String, Object>> getCustomerProfitContribution(Integer limit);
}
