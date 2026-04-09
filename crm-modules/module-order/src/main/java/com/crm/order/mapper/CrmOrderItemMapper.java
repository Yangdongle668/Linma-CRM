package com.crm.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.order.domain.entity.CrmOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmOrderItemMapper extends BaseMapper<CrmOrderItem> {

    /**
     * 根据订单ID查询明细列表
     *
     * @param orderId 订单ID
     * @return 明细列表
     */
    List<CrmOrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量插入订单明细
     *
     * @param items 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("items") List<CrmOrderItem> items);

    /**
     * 根据订单ID删除明细
     *
     * @param orderId 订单ID
     * @return 影响行数
     */
    int deleteByOrderId(@Param("orderId") Long orderId);
}
