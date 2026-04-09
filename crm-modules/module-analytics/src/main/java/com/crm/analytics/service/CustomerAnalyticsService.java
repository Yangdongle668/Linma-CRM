package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 客户分析服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface CustomerAnalyticsService {

    /**
     * 新增客户数趋势
     *
     * @param period 统计周期(month/quarter/year)
     * @return 趋势数据列表
     */
    List<Map<String, Object>> getNewCustomerTrend(String period);

    /**
     * 客户分布统计(按国家)
     *
     * @return 国家分布数据
     */
    List<Map<String, Object>> getCustomerDistributionByCountry();

    /**
     * 客户分布统计(按行业)
     *
     * @return 行业分布数据
     */
    List<Map<String, Object>> getCustomerDistributionByIndustry();

    /**
     * 客户分布统计(按等级)
     *
     * @return 等级分布数据
     */
    List<Map<String, Object>> getCustomerDistributionByLevel();

    /**
     * RFM模型分析
     * Recency(最近购买时间)、Frequency(购买频率)、Monetary(消费金额)
     *
     * @return RFM分析结果
     */
    List<Map<String, Object>> getRFMAnalysis();

    /**
     * 流失预警客户列表
     *
     * @param days 天数阈值
     * @return 流失预警客户列表
     */
    List<Map<String, Object>> getChurnWarningCustomers(Integer days);
}
