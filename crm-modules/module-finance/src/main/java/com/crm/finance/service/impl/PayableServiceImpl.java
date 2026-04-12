package com.crm.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        Page<CrmPayable> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<CrmPayable> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmPayable::getDeleted, 0);
        
        if (query.getPayableNo() != null && !query.getPayableNo().isEmpty()) {
            wrapper.like(CrmPayable::getPayableNo, query.getPayableNo());
        }
        if (query.getOrderId() != null) {
            wrapper.eq(CrmPayable::getOrderId, query.getOrderId());
        }
        if (query.getSupplierId() != null) {
            wrapper.eq(CrmPayable::getSupplierId, query.getSupplierId());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(CrmPayable::getStatus, query.getStatus());
        }
        if (query.getApprovalStatus() != null && !query.getApprovalStatus().isEmpty()) {
            wrapper.eq(CrmPayable::getApprovalStatus, query.getApprovalStatus());
        }
        if (query.getStartDate() != null) {
            wrapper.ge(CrmPayable::getCreatedTime, query.getStartDate().atStartOfDay());
        }
        if (query.getEndDate() != null) {
            wrapper.le(CrmPayable::getCreatedTime, query.getEndDate().atTime(23, 59, 59));
        }
        
        wrapper.orderByDesc(CrmPayable::getCreatedTime);
        
        Page<CrmPayable> payablePage = page(page, wrapper);
        
        // Convert to VO page
        Page<PayableVO> voPage = new Page<>(pageNum, pageSize, payablePage.getTotal());
        List<PayableVO> voList = payablePage.getRecords().stream().map(this::convertToVO).toList();
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    private PayableVO convertToVO(CrmPayable payable) {
        PayableVO vo = new PayableVO();
        vo.setId(payable.getId());
        vo.setPayableNo(payable.getPayableNo());
        vo.setOrderId(payable.getOrderId());
        vo.setSupplierId(payable.getSupplierId());
        vo.setAmount(payable.getAmount());
        vo.setCurrency(payable.getCurrency());
        vo.setPaidAmount(payable.getPaidAmount());
        
        // Calculate unpaid amount
        BigDecimal unpaidAmount = payable.getAmount().subtract(payable.getPaidAmount());
        vo.setUnpaidAmount(unpaidAmount);
        
        // Calculate payment progress
        if (payable.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = payable.getPaidAmount()
                    .divide(payable.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            vo.setPaymentProgress(progress);
        } else {
            vo.setPaymentProgress(BigDecimal.ZERO);
        }
        
        vo.setDueDate(payable.getDueDate());
        vo.setStatus(payable.getStatus());
        vo.setApprovalStatus(payable.getApprovalStatus());
        vo.setApprovedBy(payable.getApprovedBy());
        vo.setApprovedTime(payable.getApprovedTime());
        vo.setApprovalRemark(payable.getApprovalRemark());
        vo.setRemark(payable.getRemark());
        vo.setCreatedTime(payable.getCreatedTime());
        
        return vo;
    }

    @Override
    public PayableVO getPayableById(Long id) {
        CrmPayable payable = getById(id);
        if (payable == null) {
            return null;
        }
        return convertToVO(payable);
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
