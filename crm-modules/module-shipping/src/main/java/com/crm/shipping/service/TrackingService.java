package com.crm.shipping.service;

import com.crm.shipping.domain.vo.ShippingVO;

import java.util.Map;

/**
 * 物流追踪服务接口
 * 预留17track API集成接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface TrackingService {

    /**
     * 查询物流轨迹
     * 集成17track API或其他物流商API
     *
     * @param trackingNo 运单号
     * @param carrier 物流商
     * @return 物流轨迹信息
     */
    Map<String, Object> trackShipment(String trackingNo, String carrier);

    /**
     * 批量查询物流轨迹
     *
     * @param shippings 发货单列表
     * @return 物流轨迹Map,key为发货单ID
     */
    Map<Long, Map<String, Object>> batchTrackShippings(java.util.List<ShippingVO> shippings);

    /**
     * 订阅物流追踪
     * 当物流状态更新时接收通知
     *
     * @param trackingNo 运单号
     * @param carrier 物流商
     * @return 是否订阅成功
     */
    boolean subscribeTracking(String trackingNo, String carrier);

    /**
     * 取消物流追踪订阅
     *
     * @param trackingNo 运单号
     * @param carrier 物流商
     * @return 是否取消成功
     */
    boolean unsubscribeTracking(String trackingNo, String carrier);
}
