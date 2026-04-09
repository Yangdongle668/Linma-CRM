package com.crm.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.quotation.domain.dto.*;
import com.crm.quotation.domain.entity.CrmQuotation;
import com.crm.quotation.domain.vo.QuotationVO;

import java.util.List;

/**
 * 报价单业务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface QuotationService extends IService<CrmQuotation> {

    /**
     * 分页查询报价单列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 报价单分页数据
     */
    IPage<QuotationVO> pageQuotations(QuotationQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询报价单详情
     *
     * @param id 报价单ID
     * @return 报价单详情
     */
    QuotationVO getQuotationById(Long id);

    /**
     * 创建报价单
     *
     * @param dto 报价单创建信息
     * @return 报价单ID
     */
    Long createQuotation(QuotationCreateDTO dto);

    /**
     * 更新报价单(生成新版本)
     *
     * @param dto 报价单更新信息
     * @return 是否成功
     */
    boolean updateQuotation(QuotationUpdateDTO dto);

    /**
     * 删除报价单
     *
     * @param ids 报价单ID列表
     * @return 是否成功
     */
    boolean deleteQuotations(List<Long> ids);

    /**
     * 提交审批
     *
     * @param quotationId 报价单ID
     * @return 是否成功
     */
    boolean submitForApproval(Long quotationId);

    /**
     * 审批报价单
     *
     * @param dto 审批信息
     * @return 是否成功
     */
    boolean approveQuotation(QuotationApprovalDTO dto);

    /**
     * 生成PDF报价单
     *
     * @param quotationId 报价单ID
     * @param language 语言(en/cn)
     * @return PDF文件路径
     */
    String generatePdf(Long quotationId, String language);

    /**
     * 发送邮件
     *
     * @param dto 邮件发送信息
     * @return 是否成功
     */
    boolean sendEmail(QuotationEmailDTO dto);

    /**
     * 从询盘转化创建报价单
     *
     * @param inquiryId 询盘ID
     * @return 报价单ID
     */
    Long createFromInquiry(Long inquiryId);

    /**
     * 转为订单
     *
     * @param quotationId 报价单ID
     * @return 订单ID
     */
    Long convertToOrder(Long quotationId);

    /**
     * 生成报价单号
     *
     * @return 报价单号(格式: QT20260409001)
     */
    String generateQuotationNo();

    /**
     * 获取报价单历史版本列表
     *
     * @param quotationId 报价单ID
     * @return 历史版本列表
     */
    List<QuotationVO> getVersionHistory(Long quotationId);

    /**
     * 复制报价单
     *
     * @param quotationId 原报价单ID
     * @return 新报价单ID
     */
    Long copyQuotation(Long quotationId);

    /**
     * 导出报价单数据(Excel)
     *
     * @param query 查询条件
     * @return 报价单列表
     */
    List<QuotationVO> exportQuotations(QuotationQuery query);

    /**
     * 检查报价单是否过期
     *
     * @param quotationId 报价单ID
     * @return 是否已过期
     */
    boolean isExpired(Long quotationId);

    /**
     * 自动标记过期报价单(定时任务调用)
     *
     * @return 标记为过期的数量
     */
    int autoMarkExpired();
}
