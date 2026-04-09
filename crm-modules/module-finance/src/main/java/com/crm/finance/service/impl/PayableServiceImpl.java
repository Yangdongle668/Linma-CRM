package com.crm.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.finance.domain.dto.PayableQuery;
import com.crm.finance.domain.dto.PaymentApprovalDTO;
import com.crm.finance.domain.entity.CrmPayable;
import com.crm.finance.domain.vo.PayableVO;
import com.crm.finance.mapper.CrmPayableMapper;
import com.crm.finance.service.PayableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 应付账款业务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class PayableServiceImpl extends ServiceImpl<CrmPayableMapper, CrmPayable> implements PayableService {

    private final CrmPayableMapper payableMapper;

    @Override
    public IPage<PayableVO> pagePayables(PayableQuery query, int pageNum, int pageSize) {
        // TODO: 实现分页查询
        return null;
    }

    @Override
    public PayableVO getPayableById(Long id) {
        // TODO: 实现详情查询
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitPaymentApplication(Long payableId) {
        CrmPayable payable = getById(payableId);
        if (payable == null) {
            return false;
        }

        payable.setApprovalStatus("pending");
        return updateById(payable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approvePayment(PaymentApprovalDTO dto) {
        CrmPayable payable = getById(dto.getPayableId());
        if (payable == null) {
            return false;
        }

        payable.setApprovalStatus(dto.getApprovalResult());
        // TODO: 设置审批人和审批时间
        payable.setApprovedTime(LocalDateTime.now());
        payable.setApprovalRemark(dto.getApprovalRemark());

        return updateById(payable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmPayment(Long payableId, BigDecimal amount) {
        CrmPayable payable = getById(payableId);
        if (payable == null) {
            return false;
        }

        BigDecimal newPaidAmount = payable.getPaidAmount().add(amount);
        payable.setPaidAmount(newPaidAmount);

        // 计算未付金额
        BigDecimal unpaidAmount = payable.getAmount().subtract(newPaidAmount);
        payable.setUnpaidAmount(unpaidAmount);

        // 计算付款进度
        BigDecimal progress = newPaidAmount.divide(payable.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
        payable.setPaymentProgress(progress);

        // 更新状态
        if (unpaidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            payable.setStatus("paid");
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            payable.setStatus("partial");
        }

        return updateById(payable);
    }

    @Override
    public String generatePayableNo() {
        // TODO: 实现单号生成逻辑
        return "PAY" + System.currentTimeMillis();
    }
}
