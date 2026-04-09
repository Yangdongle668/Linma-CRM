package com.crm.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.order.domain.dto.*;
import com.crm.order.domain.entity.CrmOrder;
import com.crm.order.domain.vo.OrderVO;

import java.util.List;

/**
 * 订单业务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface OrderService extends IService<CrmOrder> {

    /**
     * 分页查询订单列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 订单分页数据
     */
    IPage<OrderVO> pageOrders(OrderQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    OrderVO getOrderById(Long id);

    /**
     * 创建订单
     *
     * @param dto 订单创建信息
     * @return 订单ID
     */
    Long createOrder(OrderCreateDTO dto);

    /**
     * 更新订单
     *
     * @param dto 订单更新信息
     * @return 是否成功
     */
    boolean updateOrder(OrderUpdateDTO dto);

    /**
     * 删除订单
     *
     * @param ids 订单ID列表
     * @return 是否成功
     */
    boolean deleteOrders(List<Long> ids);

    /**
     * 变更订单状态
     *
     * @param dto 状态变更信息
     * @return 是否成功
     */
    boolean changeStatus(OrderStatusChangeDTO dto);

    /**
     * 更新生产进度
     *
     * @param dto 生产进度信息
     * @return 是否成功
     */
    boolean updateProductionProgress(ProductionProgressDTO dto);

    /**
     * 从报价单创建订单
     *
     * @param quotationId 报价单ID
     * @return 订单ID
     */
    Long createFromQuotation(Long quotationId);

    /**
     * 生成PI形式发票
     *
     * @param orderId 订单ID
     * @return PI发票号
     */
    String generatePiInvoice(Long orderId);

    /**
     * 生成CI商业发票
     *
     * @param orderId 订单ID
     * @return CI发票号
     */
    String generateCiInvoice(Long orderId);

    /**
     * 生成装箱单
     *
     * @param orderId 订单ID
     * @return 装箱单号
     */
    String generatePackingList(Long orderId);

    /**
     * 确认收款
     *
     * @param orderId 订单ID
     * @param amount 收款金额
     * @return 是否成功
     */
    boolean confirmPayment(Long orderId, java.math.BigDecimal amount);

    /**
     * 生成订单号
     *
     * @return 订单号(格式: ORD20260409001)
     */
    String generateOrderNo();

    /**
     * 导出订单数据(Excel)
     *
     * @param query 查询条件
     * @return 订单列表
     */
    List<OrderVO> exportOrders(OrderQuery query);

    /**
     * 复制订单
     *
     * @param orderId 原订单ID
     * @return 新订单ID
     */
    Long copyOrder(Long orderId);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param reason 取消原因
     * @return 是否成功
     */
    boolean cancelOrder(Long orderId, String reason);

    /**
     * 获取即将交货的订单列表
     *
     * @param days 天数
     * @return 即将交货的订单列表
     */
    List<OrderVO> getUpcomingDeliveryOrders(Integer days);

    /**
     * 获取生产中的订单列表
     *
     * @return 生产中订单列表
     */
    List<OrderVO> getProducingOrders();
}
