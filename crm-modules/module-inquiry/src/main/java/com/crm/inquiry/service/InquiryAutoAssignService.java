package com.crm.inquiry.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 询盘自动分配服务
 * 支持三种分配策略:
 * 1. round_robin - 轮询分配
 * 2. load_balance - 负载均衡(按待处理询盘数)
 * 3. region_match - 区域匹配(按客户国家匹配销售区域)
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
public class InquiryAutoAssignService {

    // 轮询计数器
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);

    /**
     * 轮询分配策略
     * 按照销售人员顺序轮流分配询盘
     *
     * @param salesUserIds 销售人员ID列表
     * @return 分配的负责人ID
     */
    public Long roundRobinAssign(Long[] salesUserIds) {
        if (salesUserIds == null || salesUserIds.length == 0) {
            log.warn("没有可用的销售人员");
            return null;
        }

        int index = roundRobinCounter.getAndIncrement() % salesUserIds.length;
        if (index < 0) {
            index = -index;
        }

        Long assignedUserId = salesUserIds[index];
        log.info("轮询分配询盘给销售人员: {}", assignedUserId);
        return assignedUserId;
    }

    /**
     * 负载均衡分配策略
     * 将询盘分配给当前待处理询盘最少的销售人员
     *
     * @param salesUserIds 销售人员ID列表
     * @param pendingCounts 每个销售人员的待处理询盘数
     * @return 分配的负责人ID
     */
    public Long loadBalanceAssign(Long[] salesUserIds, Integer[] pendingCounts) {
        if (salesUserIds == null || salesUserIds.length == 0) {
            log.warn("没有可用的销售人员");
            return null;
        }

        if (pendingCounts == null || pendingCounts.length != salesUserIds.length) {
            log.warn("待处理询盘数数组与销售人员数组不匹配");
            return salesUserIds[0];
        }

        // 找到待处理询盘最少的销售人员
        int minIndex = 0;
        int minCount = pendingCounts[0];
        for (int i = 1; i < pendingCounts.length; i++) {
            if (pendingCounts[i] < minCount) {
                minCount = pendingCounts[i];
                minIndex = i;
            }
        }

        Long assignedUserId = salesUserIds[minIndex];
        log.info("负载均衡分配询盘给销售人员: {}, 当前待处理数: {}", assignedUserId, minCount);
        return assignedUserId;
    }

    /**
     * 区域匹配分配策略
     * 根据客户所在国家/地区匹配对应的销售区域负责人
     *
     * @param destinationCountry 目的国家
     * @param regionSalesMap 区域与销售人员的映射关系
     * @return 分配的负责人ID
     */
    public Long regionMatchAssign(String destinationCountry, Map<String, Long> regionSalesMap) {
        if (destinationCountry == null || regionSalesMap == null || regionSalesMap.isEmpty()) {
            log.warn("目的国家或区域映射为空");
            return null;
        }

        // 直接匹配国家
        Long ownerId = regionSalesMap.get(destinationCountry.toUpperCase());
        if (ownerId != null) {
            log.info("区域匹配分配询盘(国家: {})给销售人员: {}", destinationCountry, ownerId);
            return ownerId;
        }

        // 按大洲匹配
        String continent = getContinentByCountry(destinationCountry);
        if (continent != null) {
            ownerId = regionSalesMap.get(continent);
            if (ownerId != null) {
                log.info("区域匹配分配询盘(大洲: {})给销售人员: {}", continent, ownerId);
                return ownerId;
            }
        }

        log.warn("未找到国家 {} 的匹配销售人员", destinationCountry);
        return null;
    }

    /**
     * 根据国家获取所属大洲
     *
     * @param country 国家名称
     * @return 大洲名称
     */
    private String getContinentByCountry(String country) {
        // 简化的国家-大洲映射，实际项目中应从数据库获取
        Map<String, String> countryContinentMap = new HashMap<>();
        countryContinentMap.put("USA", "NORTH_AMERICA");
        countryContinentMap.put("CANADA", "NORTH_AMERICA");
        countryContinentMap.put("MEXICO", "NORTH_AMERICA");
        countryContinentMap.put("UK", "EUROPE");
        countryContinentMap.put("GERMANY", "EUROPE");
        countryContinentMap.put("FRANCE", "EUROPE");
        countryContinentMap.put("ITALY", "EUROPE");
        countryContinentMap.put("SPAIN", "EUROPE");
        countryContinentMap.put("CHINA", "ASIA");
        countryContinentMap.put("JAPAN", "ASIA");
        countryContinentMap.put("KOREA", "ASIA");
        countryContinentMap.put("INDIA", "ASIA");
        countryContinentMap.put("THAILAND", "ASIA");
        countryContinentMap.put("VIETNAM", "ASIA");
        countryContinentMap.put("AUSTRALIA", "OCEANIA");
        countryContinentMap.put("NEW ZEALAND", "OCEANIA");
        countryContinentMap.put("BRAZIL", "SOUTH_AMERICA");
        countryContinentMap.put("ARGENTINA", "SOUTH_AMERICA");
        countryContinentMap.put("SOUTH AFRICA", "AFRICA");
        countryContinentMap.put("EGYPT", "AFRICA");
        countryContinentMap.put("NIGERIA", "AFRICA");

        return countryContinentMap.get(country.toUpperCase());
    }
}
