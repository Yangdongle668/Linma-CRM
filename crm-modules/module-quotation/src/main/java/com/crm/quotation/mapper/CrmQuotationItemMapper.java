package com.crm.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.quotation.domain.entity.CrmQuotationItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报价单明细Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmQuotationItemMapper extends BaseMapper<CrmQuotationItem> {

    /**
     * 根据报价单ID查询明细列表
     *
     * @param quotationId 报价单ID
     * @return 明细列表
     */
    List<CrmQuotationItem> selectByQuotationId(@Param("quotationId") Long quotationId);

    /**
     * 批量插入报价单明细
     *
     * @param items 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("items") List<CrmQuotationItem> items);

    /**
     * 根据报价单ID删除明细
     *
     * @param quotationId 报价单ID
     * @return 影响行数
     */
    int deleteByQuotationId(@Param("quotationId") Long quotationId);
}
