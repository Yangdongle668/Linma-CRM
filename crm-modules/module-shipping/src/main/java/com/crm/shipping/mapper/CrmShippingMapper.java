package com.crm.shipping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.shipping.domain.dto.ShippingQuery;
import com.crm.shipping.domain.entity.CrmShipping;
import com.crm.shipping.domain.vo.ShippingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 发货单Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmShippingMapper extends BaseMapper<CrmShipping> {

    /**
     * 分页查询发货单列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 发货单分页数据
     */
    IPage<ShippingVO> selectShippingPage(Page<ShippingVO> page, @Param("query") ShippingQuery query);
}
