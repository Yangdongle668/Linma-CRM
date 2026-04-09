package com.crm.inquiry.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.inquiry.domain.dto.InquiryCreateDTO;
import com.crm.inquiry.domain.dto.InquiryQuery;
import com.crm.inquiry.domain.dto.InquiryReplyDTO;
import com.crm.inquiry.domain.dto.InquiryUpdateDTO;
import com.crm.inquiry.domain.entity.CrmInquiry;
import com.crm.inquiry.domain.vo.InquiryVO;

import java.util.List;

/**
 * 询盘Service接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface InquiryService extends IService<CrmInquiry> {

    /**
     * 分页查询询盘
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 询盘分页列表
     */
    IPage<InquiryVO> getInquiryPage(Page<InquiryVO> page, InquiryQuery query);

    /**
     * 根据ID查询询盘详情
     *
     * @param id 询盘ID
     * @return 询盘详情
     */
    InquiryVO getInquiryById(Long id);

    /**
     * 创建询盘
     *
     * @param dto 创建DTO
     * @return 询盘ID
     */
    Long createInquiry(InquiryCreateDTO dto);

    /**
     * 更新询盘
     *
     * @param dto 更新DTO
     */
    void updateInquiry(InquiryUpdateDTO dto);

    /**
     * 删除询盘
     *
     * @param ids 询盘ID列表
     */
    void deleteInquiries(List<Long> ids);

    /**
     * 生成询盘号
     *
     * @return 询盘号
     */
    String generateInquiryNo();

    /**
     * 回复询盘
     *
     * @param replyDTO 回复DTO
     */
    void replyInquiry(InquiryReplyDTO replyDTO);

    /**
     * 变更询盘状态
     *
     * @param inquiryId 询盘ID
     * @param newStatus 新状态
     * @param reason 原因
     */
    void changeStatus(Long inquiryId, String newStatus, String reason);

    /**
     * 转为报价单
     *
     * @param inquiryId 询盘ID
     * @return 报价单ID
     */
    Long convertToQuotation(Long inquiryId);

    /**
     * 关闭询盘
     *
     * @param inquiryId 询盘ID
     * @param reason 关闭原因
     */
    void closeInquiry(Long inquiryId, String reason);

    /**
     * 分配询盘给负责人
     *
     * @param inquiryId 询盘ID
     * @param ownerId 负责人ID
     */
    void assignInquiry(Long inquiryId, Long ownerId);

    /**
     * 自动分配询盘(根据策略)
     *
     * @param inquiryId 询盘ID
     * @param strategy 分配策略(round_robin/load_balance/region_match)
     */
    void autoAssignInquiry(Long inquiryId, String strategy);

    /**
     * 导出询盘
     *
     * @param query 查询条件
     * @return 询盘列表
     */
    List<InquiryVO> exportInquiries(InquiryQuery query);

    /**
     * 统计待处理询盘数量
     *
     * @param ownerId 负责人ID
     * @return 数量
     */
    Integer countPendingInquiries(Long ownerId);

    /**
     * 统计已转化询盘数量
     *
     * @param ownerId 负责人ID
     * @return 数量
     */
    Integer countConvertedInquiries(Long ownerId);
}
