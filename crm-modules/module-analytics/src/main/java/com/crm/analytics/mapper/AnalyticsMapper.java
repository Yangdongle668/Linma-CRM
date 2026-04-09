package com.crm.analytics.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据分析Mapper接口
 * 用于复杂统计查询
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface AnalyticsMapper {

    /**
     * 获取销售漏斗统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 漏斗统计数据
     */
    List<Map<String, Object>> getSalesFunnelStats(@Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * 获取RFM模型数据
     *
     * @return RFM数据列表
     */
    List<Map<String, Object>> getRFMData();

    /**
     * 获取产品销售排行
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 数量限制
     * @return 产品排行列表
     */
    List<Map<String, Object>> getProductSalesRanking(@Param("startDate") String startDate,
                                                       @Param("endDate") String endDate,
                                                       @Param("limit") Integer limit);

    /**
     * 获取区域销售统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 区域统计数据
     */
    List<Map<String, Object>> getRegionSalesStats(@Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);
}
