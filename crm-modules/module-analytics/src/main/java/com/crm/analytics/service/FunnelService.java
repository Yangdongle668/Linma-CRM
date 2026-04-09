package com.crm.analytics.service;

import java.util.List;
import java.util.Map;

/**
 * 销售漏斗分析服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface FunnelService {

    /**
     * 获取销售漏斗数据
     * 线索→客户→询盘→报价→订单转化率
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 漏斗数据列表
     */
    List<Map<String, Object>> getSalesFunnel(String startDate, String endDate);

    /**
     * 获取各阶段转化率
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 转化率Map
     */
    Map<String, Object> getConversionRates(String startDate, String endDate);

    /**
     * 按销售人员统计漏斗数据
     *
     * @param ownerId 负责人ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 个人漏斗数据
     */
    List<Map<String, Object>> getPersonalFunnel(Long ownerId, String startDate, String endDate);
}
