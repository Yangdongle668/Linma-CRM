package com.crm.inquiry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.inquiry.domain.dto.InquiryQuery;
import com.crm.inquiry.domain.entity.CrmInquiry;
import com.crm.inquiry.domain.vo.InquiryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 询盘Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface InquiryMapper extends BaseMapper<CrmInquiry> {

    /**
     * 分页查询询盘(带关联信息)
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 询盘列表
     */
    Page<InquiryVO> selectInquiryPage(Page<InquiryVO> page, @Param("query") InquiryQuery query);

    /**
     * 根据询盘号查询
     *
     * @param inquiryNo 询盘号
     * @return 询盘信息
     */
    CrmInquiry selectByInquiryNo(@Param("inquiryNo") String inquiryNo);

    /**
     * 批量导出询盘
     *
     * @param query 查询条件
     * @return 询盘列表
     */
    List<InquiryVO> selectInquiryForExport(@Param("query") InquiryQuery query);

    /**
     * 统计待处理询盘数量
     *
     * @param ownerId 负责人ID
     * @return 数量
     */
    Integer countPendingInquiries(@Param("ownerId") Long ownerId);

    /**
     * 统计已转化询盘数量
     *
     * @param ownerId 负责人ID
     * @return 数量
     */
    Integer countConvertedInquiries(@Param("ownerId") Long ownerId);
}
