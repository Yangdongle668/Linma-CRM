package com.crm.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.finance.domain.dto.PayableQuery;
import com.crm.finance.domain.dto.PaymentApprovalDTO;
import com.crm.finance.domain.entity.CrmPayable;
import com.crm.finance.domain.vo.PayableVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 应付账款业务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface PayableService extends IService<CrmPayable> {

    /**
     * 分页查询应付账款列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 应付账款分页数据
     */
    IPage<PayableVO> pagePayables(PayableQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询应付账款详情
     *
     * @param id 应付ID
     * @return 应付账款详情
     */
    PayableVO getPayableById(Long id);

    /**
     * 提交付款申请
     *
     * @param payableId 应付ID
     * @return 是否成功
     */
    boolean submitPaymentApplication(Long payableId);

    /**
     * 审批付款申请
     *
     * @param dto 审批信息
     * @return 是否成功
     */
    boolean approvePayment(PaymentApprovalDTO dto);

    /**
     * 确认付款
     *
     * @param payableId 应付ID
     * @param amount 付款金额
     * @return 是否成功
     */
    boolean confirmPayment(Long payableId, BigDecimal amount);

    /**
     * 生成应付单号
     *
     * @return 应付单号
     */
    String generatePayableNo();
}
