package com.crm.shipping.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.shipping.domain.dto.ShippingCreateDTO;
import com.crm.shipping.domain.dto.ShippingQuery;
import com.crm.shipping.domain.dto.ShippingUpdateDTO;
import com.crm.shipping.domain.entity.CrmShipping;
import com.crm.shipping.domain.vo.ShippingVO;

import java.util.List;

/**
 * 发货单业务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ShippingService extends IService<CrmShipping> {

    /**
     * 分页查询发货单列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 发货单分页数据
     */
    IPage<ShippingVO> pageShippings(ShippingQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询发货单详情
     *
     * @param id 发货单ID
     * @return 发货单详情
     */
    ShippingVO getShippingById(Long id);

    /**
     * 创建发货单
     *
     * @param dto 发货单创建信息
     * @return 发货单ID
     */
    Long createShipping(ShippingCreateDTO dto);

    /**
     * 更新发货单
     *
     * @param dto 发货单更新信息
     * @return 是否成功
     */
    boolean updateShipping(ShippingUpdateDTO dto);

    /**
     * 删除发货单
     *
     * @param ids 发货单ID列表
     * @return 是否成功
     */
    boolean deleteShippings(List<Long> ids);

    /**
     * 生成发货单号
     *
     * @return 发货单号(格式:SHP20260409001)
     */
    String generateShippingNo();

    /**
     * 更新物流状态
     *
     * @param shippingId 发货单ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long shippingId, String status);

    /**
     * 录入运单号
     *
     * @param shippingId 发货单ID
     * @param trackingNo 运单号
     * @return 是否成功
     */
    boolean updateTrackingNo(Long shippingId, String trackingNo);

    /**
     * 查询待报关的发货单
     *
     * @return 待报关发货单列表
     */
    List<ShippingVO> getPendingCustomsShippings();

    /**
     * 导出发货单数据
     *
     * @param query 查询条件
     * @return 发货单列表
     */
    List<ShippingVO> exportShippings(ShippingQuery query);
}
