package com.crm.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.finance.domain.dto.PaymentRecordDTO;
import com.crm.finance.domain.dto.ReceivableQuery;
import com.crm.finance.domain.entity.CrmReceivable;
import com.crm.finance.domain.vo.ReceivableVO;
import com.crm.finance.mapper.CrmReceivableMapper;
import com.crm.finance.service.ReceivableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应收账款业务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class ReceivableServiceImpl extends ServiceImpl<CrmReceivableMapper, CrmReceivable> implements ReceivableService {

    private final CrmReceivableMapper receivableMapper;

    @Override
    public IPage<ReceivableVO> pageReceivables(ReceivableQuery query, int pageNum, int pageSize) {
        Page<CrmReceivable> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<CrmReceivable> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmReceivable::getDeleted, 0);
        
        if (query.getReceivableNo() != null && !query.getReceivableNo().isEmpty()) {
            wrapper.like(CrmReceivable::getReceivableNo, query.getReceivableNo());
        }
        if (query.getOrderId() != null) {
            wrapper.eq(CrmReceivable::getOrderId, query.getOrderId());
        }
        if (query.getCustomerId() != null) {
            wrapper.eq(CrmReceivable::getCustomerId, query.getCustomerId());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(CrmReceivable::getStatus, query.getStatus());
        }
        if (query.getStartDate() != null) {
            wrapper.ge(CrmReceivable::getCreatedTime, query.getStartDate().atStartOfDay());
        }
        if (query.getEndDate() != null) {
            wrapper.le(CrmReceivable::getCreatedTime, query.getEndDate().atTime(23, 59, 59));
        }
        
        wrapper.orderByDesc(CrmReceivable::getCreatedTime);
        
        Page<CrmReceivable> receivablePage = page(page, wrapper);
        
        // Convert to VO page
        Page<ReceivableVO> voPage = new Page<>(pageNum, pageSize, receivablePage.getTotal());
        List<ReceivableVO> voList = receivablePage.getRecords().stream().map(this::convertToVO).toList();
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    private ReceivableVO convertToVO(CrmReceivable receivable) {
        ReceivableVO vo = new ReceivableVO();
        vo.setId(receivable.getId());
        vo.setReceivableNo(receivable.getReceivableNo());
        vo.setOrderId(receivable.getOrderId());
        vo.setCustomerId(receivable.getCustomerId());
        vo.setAmount(receivable.getAmount());
        vo.setCurrency(receivable.getCurrency());
        vo.setReceivedAmount(receivable.getReceivedAmount());
        
        // Calculate unpaid amount
        BigDecimal unpaidAmount = receivable.getAmount().subtract(receivable.getReceivedAmount());
        vo.setUnpaidAmount(unpaidAmount);
        
        // Calculate payment progress
        if (receivable.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = receivable.getReceivedAmount()
                    .divide(receivable.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            vo.setPaymentProgress(progress);
        } else {
            vo.setPaymentProgress(BigDecimal.ZERO);
        }
        
        vo.setDueDate(receivable.getDueDate());
        vo.setStatus(receivable.getStatus());
        vo.setRemark(receivable.getRemark());
        vo.setCreatedTime(receivable.getCreatedTime());
        
        // Calculate aging days
        if (receivable.getDueDate() != null) {
            long agingDays = ChronoUnit.DAYS.between(receivable.getDueDate(), LocalDate.now());
            vo.setAgingDays((int) agingDays);
            
            // Determine aging range
            if (agingDays <= 30) {
                vo.setAgingRange("0-30");
            } else if (agingDays <= 60) {
                vo.setAgingRange("31-60");
            } else if (agingDays <= 90) {
                vo.setAgingRange("61-90");
            } else {
                vo.setAgingRange("90+");
            }
        }
        
        return vo;
    }

    @Override
    public ReceivableVO getReceivableById(Long id) {
        CrmReceivable receivable = getById(id);
        if (receivable == null) {
            return null;
        }
        return convertToVO(receivable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordPayment(PaymentRecordDTO dto) {
        CrmReceivable receivable = getById(dto.getReceivableId());
        if (receivable == null) {
            return false;
        }

        BigDecimal newReceivedAmount = receivable.getReceivedAmount().add(dto.getAmount());
        receivable.setReceivedAmount(newReceivedAmount);

        // 计算未收金额
        BigDecimal unpaidAmount = receivable.getAmount().subtract(newReceivedAmount);
        receivable.setUnpaidAmount(unpaidAmount);

        // 计算收款进度
        BigDecimal progress = newReceivedAmount.divide(receivable.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
        receivable.setPaymentProgress(progress);

        // 更新状态
        if (unpaidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            receivable.setStatus("paid");
        } else if (newReceivedAmount.compareTo(BigDecimal.ZERO) > 0) {
            receivable.setStatus("partial");
        }

        return updateById(receivable);
    }

    @Override
    public List<ReceivableVO> getOverdueReceivables() {
        LambdaQueryWrapper<CrmReceivable> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmReceivable::getDeleted, 0)
                .and(w -> w.eq(CrmReceivable::getStatus, "overdue")
                        .or()
                        .lt(CrmReceivable::getDueDate, LocalDate.now()))
                .ne(CrmReceivable::getStatus, "paid")
                .orderByAsc(CrmReceivable::getDueDate);
        
        List<CrmReceivable> receivables = list(wrapper);
        return receivables.stream().map(this::convertToVO).toList();
    }

    @Override
    public Map<String, Object> getAgingAnalysis() {
        Map<String, Object> result = new HashMap<>();

        // TODO: 实现账龄分析SQL查询
        // 0-30天、31-60天、61-90天、90天以上
        result.put("0-30", BigDecimal.ZERO);
        result.put("31-60", BigDecimal.ZERO);
        result.put("61-90", BigDecimal.ZERO);
        result.put("90+", BigDecimal.ZERO);

        return result;
    }

    @Override
    public String generateReceivableNo() {
        // TODO: 实现单号生成逻辑
        return "REC" + System.currentTimeMillis();
    }
}
