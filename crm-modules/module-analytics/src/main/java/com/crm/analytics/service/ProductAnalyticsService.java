package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 产品分析服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ProductAnalyticsService {

    /**
     * 热销产品TOP10
     *
     * @param period 统计周期
     * @param limit 数量限制
     * @return 热销产品列表
     */
    List<Map<String, Object>> getHotProducts(String period, Integer limit);

    /**
     * 滞销产品预警
     *
     * @param days 天数阈值
     * @return 滞销产品列表
     */
    List<Map<String, Object>> getSlowMovingProducts(Integer days);

    /**
     * 产品销售趋势
     *
     * @param productId 产品ID
     * @param period 统计周期
     * @return 销售趋势数据
     */
    List<Map<String, Object>> getProductSalesTrend(Long productId, String period);

    /**
     * 产品分类销售统计
     *
     * @param period 统计周期
     * @return 分类销售统计
     */
    List<Map<String, Object>> getCategorySalesStatistics(String period);
}
