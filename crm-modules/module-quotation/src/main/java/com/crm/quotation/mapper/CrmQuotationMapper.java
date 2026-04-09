package com.crm.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.quotation.domain.entity.CrmQuotation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 报价单Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface CrmQuotationMapper extends BaseMapper<CrmQuotation> {

    /**
     * 分页查询报价单列表(支持高级搜索)
     *
     * @param page 分页对象
     * @param customerId 客户ID
     * @param status 状态
     * @param quotationNo 报价单号
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param ownerId 负责人ID
     * @return 报价单分页数据
     */
    IPage<CrmQuotation> selectQuotationPage(
            Page<CrmQuotation> page,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("quotationNo") String quotationNo,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("ownerId") Long ownerId
    );

    /**
     * 根据客户ID查询报价单列表
     *
     * @param customerId 客户ID
     * @return 报价单列表
     */
    List<CrmQuotation> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 根据询盘ID查询报价单列表
     *
     * @param inquiryId 询盘ID
     * @return 报价单列表
     */
    List<CrmQuotation> selectByInquiryId(@Param("inquiryId") Long inquiryId);

    /**
     * 查询待审批的报价单列表
     *
     * @return 待审批报价单列表
     */
    List<CrmQuotation> selectPendingApproval();

    /**
     * 查询即将过期的报价单列表
     *
     * @param days 天数
     * @return 即将过期的报价单列表
     */
    List<CrmQuotation> selectExpiringSoon(@Param("days") Integer days);
}
