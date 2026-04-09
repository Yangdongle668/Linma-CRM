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
        Page<ReceivableVO> page = new Page<>(pageNum, pageSize);
        // TODO: 实现分页查询
        return null;
    }

    @Override
    public ReceivableVO getReceivableById(Long id) {
        // TODO: 实现详情查询
        return null;
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
        wrapper.eq(CrmReceivable::getStatus, "overdue")
                .or()
                .lt(CrmReceivable::getDueDate, LocalDate.now())
                .ne(CrmReceivable::getStatus, "paid")
                .orderByAsc(CrmReceivable::getDueDate);
        // TODO: 转换为VO
        return null;
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
