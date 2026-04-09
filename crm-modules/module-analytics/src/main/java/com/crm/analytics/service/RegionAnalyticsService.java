package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 区域分析服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface RegionAnalyticsService {

    /**
     * 世界地图热力图数据
     * 按国家统计销售数据
     *
     * @param period 统计周期
     * @return 国家销售数据列表
     */
    List<Map<String, Object>> getWorldMapHeatmapData(String period);

    /**
     * 区域销售对比
     *
     * @param period 统计周期
     * @return 区域销售对比数据
     */
    List<Map<String, Object>> getRegionSalesComparison(String period);

    /**
     * 区域客户分布
     *
     * @return 区域客户分布数据
     */
    List<Map<String, Object>> getRegionCustomerDistribution();

    /**
     * 区域增长分析
     *
     * @param period 统计周期
     * @return 区域增长数据
     */
    List<Map<String, Object>> getRegionGrowthAnalysis(String period);
}
