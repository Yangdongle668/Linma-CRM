package com.crm.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.order.domain.entity.CrmOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmOrderMapper extends BaseMapper<CrmOrder> {

    /**
     * 分页查询订单列表(支持高级搜索)
     *
     * @param page 分页对象
     * @param customerId 客户ID
     * @param status 状态
     * @param orderNo 订单号
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param ownerId 负责人ID
     * @return 订单分页数据
     */
    IPage<CrmOrder> selectOrderPage(
            Page<CrmOrder> page,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("orderNo") String orderNo,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("ownerId") Long ownerId
    );

    /**
     * 根据客户ID查询订单列表
     *
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<CrmOrder> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 根据报价单ID查询订单
     *
     * @param quotationId 报价单ID
     * @return 订单
     */
    CrmOrder selectByQuotationId(@Param("quotationId") Long quotationId);

    /**
     * 查询待确认的订单列表
     *
     * @return 待确认订单列表
     */
    List<CrmOrder> selectPendingConfirm();

    /**
     * 查询生产中的订单列表
     *
     * @return 生产中订单列表
     */
    List<CrmOrder> selectProducingOrders();

    /**
     * 查询即将交货的订单列表
     *
     * @param days 天数
     * @return 即将交货的订单列表
     */
    List<CrmOrder> selectUpcomingDelivery(@Param("days") Integer days);
}
