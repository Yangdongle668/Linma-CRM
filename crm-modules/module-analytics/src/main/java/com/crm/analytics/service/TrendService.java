package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 趋势分析服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface TrendService {

    /**
     * 月度销售趋势
     *
     * @param year 年份
     * @return 月度趋势数据
     */
    List<Map<String, Object>> getMonthlySalesTrend(Integer year);

    /**
     * 季度销售趋势
     *
     * @param year 年份
     * @return 季度趋势数据
     */
    List<Map<String, Object>> getQuarterlySalesTrend(Integer year);

    /**
     * 年度销售趋势
     *
     * @return 年度趋势数据
     */
    List<Map<String, Object>> getYearlySalesTrend();

    /**
     * 同比增长率
     *
     * @param period 统计周期
     * @return 同比增长数据
     */
    Map<String, Object> getYearOverYearGrowth(String period);

    /**
     * 环比增长率
     *
     * @param period 统计周期
     * @return 环比增长数据
     */
    Map<String, Object> getMonthOverMonthGrowth(String period);

    /**
     * 获取销售趋势数据（前端Dashboard用）
     *
     * @param period 周期(week/month/year)
     * @return 销售趋势数据列表
     */
    List<Map<String, Object>> getSalesTrend(String period);
}
