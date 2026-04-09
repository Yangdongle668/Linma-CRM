package com.crm.quotation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.quotation.domain.dto.*;
import com.crm.quotation.domain.entity.CrmQuotation;
import com.crm.quotation.domain.entity.CrmQuotationItem;
import com.crm.quotation.domain.vo.QuotationItemVO;
import com.crm.quotation.domain.vo.QuotationVO;
import com.crm.quotation.mapper.CrmQuotationItemMapper;
import com.crm.quotation.mapper.CrmQuotationMapper;
import com.crm.quotation.service.QuotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报价单业务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationServiceImpl extends ServiceImpl<CrmQuotationMapper, CrmQuotation> implements QuotationService {

    private final CrmQuotationMapper quotationMapper;
    private final CrmQuotationItemMapper quotationItemMapper;

    @Override
    public IPage<QuotationVO> pageQuotations(QuotationQuery query, int pageNum, int pageSize) {
        Page<CrmQuotation> page = new Page<>(pageNum, pageSize);
        IPage<CrmQuotation> quotationPage = quotationMapper.selectQuotationPage(
                page,
                query.getCustomerId(),
                query.getStatus(),
                query.getQuotationNo(),
                query.getStartDate(),
                query.getEndDate(),
                query.getOwnerId()
        );

        // 转换为VO
        Page<QuotationVO> voPage = new Page<>(pageNum, pageSize, quotationPage.getTotal());
        List<QuotationVO> voList = quotationPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public QuotationVO getQuotationById(Long id) {
        CrmQuotation quotation = getById(id);
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }
        QuotationVO vo = convertToVO(quotation);

        // 查询明细
        List<CrmQuotationItem> items = quotationItemMapper.selectByQuotationId(id);
        List<QuotationItemVO> itemVOs = items.stream()
                .map(this::convertItemToVO)
                .collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createQuotation(QuotationCreateDTO dto) {
        CrmQuotation quotation = new CrmQuotation();
        BeanUtils.copyProperties(dto, quotation);

        // 生成报价单号
        quotation.setQuotationNo(generateQuotationNo());

        // 设置版本号
        quotation.setVersion(1);

        // 设置报价日期
        if (quotation.getQuotationDate() == null) {
            quotation.setQuotationDate(LocalDate.now());
        }

        // 计算有效期
        if (quotation.getValidityDays() != null) {
            quotation.setValidUntil(quotation.getQuotationDate().plusDays(quotation.getValidityDays()));
        }

        // 设置初始状态
        quotation.setStatus("draft");

        // 计算总金额和利润率
        calculateTotals(quotation, dto.getItems());

        // 检查是否需要审批(利润率<10%)
        if (quotation.getProfitMargin() != null && quotation.getProfitMargin().compareTo(new BigDecimal("10")) < 0) {
            quotation.setNeedApproval(true);
        } else {
            quotation.setNeedApproval(false);
        }

        // 保存主表
        save(quotation);

        // 保存明细
        saveItems(quotation.getId(), dto.getItems());

        log.info("创建报价单成功: {}", quotation.getQuotationNo());
        return quotation.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotation(QuotationUpdateDTO dto) {
        CrmQuotation oldQuotation = getById(dto.getId());
        if (oldQuotation == null) {
            throw new BusinessException("报价单不存在");
        }

        // 只有草稿状态可以修改
        if (!"draft".equals(oldQuotation.getStatus())) {
            throw new BusinessException("只有草稿状态的报价单可以修改");
        }

        CrmQuotation quotation = new CrmQuotation();
        BeanUtils.copyProperties(dto, quotation);

        // 增加版本号
        quotation.setVersion(oldQuotation.getVersion() + 1);

        // 重新计算总金额和利润率
        calculateTotals(quotation, dto.getItems());

        // 重新检查是否需要审批
        if (quotation.getProfitMargin() != null && quotation.getProfitMargin().compareTo(new BigDecimal("10")) < 0) {
            quotation.setNeedApproval(true);
        }

        // 更新主表
        updateById(quotation);

        // 删除旧明细，保存新明细
        quotationItemMapper.deleteByQuotationId(dto.getId());
        saveItems(dto.getId(), dto.getItems());

        log.info("更新报价单成功: {}, 版本: {}", oldQuotation.getQuotationNo(), quotation.getVersion());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuotations(List<Long> ids) {
        for (Long id : ids) {
            CrmQuotation quotation = getById(id);
            if (quotation != null) {
                // 只有草稿状态可以删除
                if (!"draft".equals(quotation.getStatus())) {
                    throw new BusinessException("报价单 " + quotation.getQuotationNo() + " 不是草稿状态，不能删除");
                }
                // 删除明细
                quotationItemMapper.deleteByQuotationId(id);
            }
        }
        // 逻辑删除主表
        removeByIds(ids);
        log.info("删除报价单成功: {}", ids);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitForApproval(Long quotationId) {
        CrmQuotation quotation = getById(quotationId);
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        if (!"draft".equals(quotation.getStatus())) {
            throw new BusinessException("只有草稿状态的报价单可以提交审批");
        }

        if (!quotation.getNeedApproval()) {
            throw new BusinessException("该报价单不需要审批");
        }

        quotation.setStatus("pending_approval");
        updateById(quotation);

        log.info("报价单提交审批成功: {}", quotation.getQuotationNo());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveQuotation(QuotationApprovalDTO dto) {
        CrmQuotation quotation = getById(dto.getQuotationId());
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        if (!"pending_approval".equals(quotation.getStatus())) {
            throw new BusinessException("报价单不在待审批状态");
        }

        quotation.setStatus(dto.getApproved() ? "approved" : "rejected");
        quotation.setApproverId(null); // TODO: 从当前登录用户获取
        quotation.setApprovalTime(LocalDateTime.now());
        quotation.setApprovalComment(dto.getComment());
        updateById(quotation);

        log.info("报价单审批{}: {}", dto.getApproved() ? "通过" : "拒绝", quotation.getQuotationNo());
        return true;
    }

    @Override
    public String generatePdf(Long quotationId, String language) {
        // TODO: 调用PDF生成服务
        log.info("生成PDF报价单: {}, 语言: {}", quotationId, language);
        return "/pdf/quotation_" + quotationId + ".pdf";
    }

    @Override
    public boolean sendEmail(QuotationEmailDTO dto) {
        // TODO: 调用邮件服务
        CrmQuotation quotation = getById(dto.getQuotationId());
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        // 更新发送次数
        quotation.setEmailSendCount(quotation.getEmailSendCount() == null ? 1 : quotation.getEmailSendCount() + 1);
        quotation.setLastSendTime(LocalDateTime.now());
        updateById(quotation);

        log.info("发送邮件成功: {}", quotation.getQuotationNo());
        return true;
    }

    @Override
    public Long createFromInquiry(Long inquiryId) {
        // TODO: 从询盘信息创建报价单
        log.info("从询盘创建报价单: {}", inquiryId);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long convertToOrder(Long quotationId) {
        CrmQuotation quotation = getById(quotationId);
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        if (!"approved".equals(quotation.getStatus()) && !"sent".equals(quotation.getStatus())) {
            throw new BusinessException("只有已批准或已发送的报价单可以转为订单");
        }

        // TODO: 调用订单服务创建订单
        // 更新报价单状态
        quotation.setStatus("converted");
        updateById(quotation);

        log.info("报价单转为订单成功: {}", quotation.getQuotationNo());
        return null; // TODO: 返回订单ID
    }

    @Override
    public String generateQuotationNo() {
        // 格式: QT + 年月日 + 三位序号
        String dateStr = LocalDate.now().toString().replace("-", "");
        String prefix = "QT" + dateStr;

        // 查询当天最大序号
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmQuotation::getQuotationNo, prefix)
                .orderByDesc(CrmQuotation::getQuotationNo)
                .last("LIMIT 1");

        CrmQuotation lastQuotation = getOne(wrapper);

        int sequence = 1;
        if (lastQuotation != null && StrUtil.isNotBlank(lastQuotation.getQuotationNo())) {
            String lastNo = lastQuotation.getQuotationNo();
            String lastSeq = lastNo.substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }

        return String.format("%s%03d", prefix, sequence);
    }

    @Override
    public List<QuotationVO> getVersionHistory(Long quotationId) {
        CrmQuotation quotation = getById(quotationId);
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        // 查询相同报价单号的所有版本
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmQuotation::getQuotationNo, quotation.getQuotationNo())
                .orderByDesc(CrmQuotation::getVersion);

        List<CrmQuotation> quotations = list(wrapper);
        return quotations.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyQuotation(Long quotationId) {
        CrmQuotation source = getById(quotationId);
        if (source == null) {
            throw new BusinessException("报价单不存在");
        }

        // 复制主表
        CrmQuotation newQuotation = new CrmQuotation();
        BeanUtils.copyProperties(source, newQuotation);
        newQuotation.setId(null);
        newQuotation.setQuotationNo(generateQuotationNo());
        newQuotation.setVersion(1);
        newQuotation.setStatus("draft");
        newQuotation.setCreatedTime(null);
        newQuotation.setUpdatedTime(null);

        save(newQuotation);

        // 复制明细
        List<CrmQuotationItem> sourceItems = quotationItemMapper.selectByQuotationId(quotationId);
        List<QuotationItemDTO> itemDTOs = sourceItems.stream().map(item -> {
            QuotationItemDTO dto = new QuotationItemDTO();
            BeanUtils.copyProperties(item, dto);
            dto.setId(null);
            return dto;
        }).collect(Collectors.toList());

        saveItems(newQuotation.getId(), itemDTOs);

        log.info("复制报价单成功: {} -> {}", source.getQuotationNo(), newQuotation.getQuotationNo());
        return newQuotation.getId();
    }

    @Override
    public List<QuotationVO> exportQuotations(QuotationQuery query) {
        LambdaQueryWrapper<CrmQuotation> wrapper = buildQueryWrapper(query);
        List<CrmQuotation> quotations = list(wrapper);
        return quotations.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExpired(Long quotationId) {
        CrmQuotation quotation = getById(quotationId);
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        if (quotation.getValidUntil() == null) {
            return false;
        }

        return LocalDate.now().isAfter(quotation.getValidUntil());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoMarkExpired() {
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmQuotation::getStatus, "approved", "sent")
                .lt(CrmQuotation::getValidUntil, LocalDate.now())
                .ne(CrmQuotation::getStatus, "expired");

        List<CrmQuotation> expiredQuotations = list(wrapper);
        for (CrmQuotation quotation : expiredQuotations) {
            quotation.setStatus("expired");
            updateById(quotation);
        }

        log.info("自动标记过期报价单数量: {}", expiredQuotations.size());
        return expiredQuotations.size();
    }

    /**
     * 计算总金额和利润率
     */
    private void calculateTotals(CrmQuotation quotation, List<QuotationItemDTO> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (QuotationItemDTO item : items) {
            if (item.getQuantity() != null && item.getUnitPrice() != null) {
                BigDecimal itemTotal = item.getQuantity().multiply(item.getUnitPrice());
                totalAmount = totalAmount.add(itemTotal);
            }
            if (item.getQuantity() != null && item.getCostPrice() != null) {
                BigDecimal itemCost = item.getQuantity().multiply(item.getCostPrice());
                totalCost = totalCost.add(itemCost);
            }
        }

        quotation.setTotalAmount(totalAmount);
        quotation.setTotalCost(totalCost);

        // 计算利润和利润率
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profit = totalAmount.subtract(totalCost);
            // 如果有汇率，转换为CNY计算
            if (quotation.getExchangeRate() != null) {
                profit = profit.multiply(quotation.getExchangeRate());
            }
            quotation.setProfitAmount(profit);
            quotation.setProfitMargin(profit.divide(totalCost, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
        }
    }

    /**
     * 保存明细
     */
    private void saveItems(Long quotationId, List<QuotationItemDTO> itemDTOs) {
        List<CrmQuotationItem> items = new ArrayList<>();
        int lineNo = 1;

        for (QuotationItemDTO dto : itemDTOs) {
            CrmQuotationItem item = new CrmQuotationItem();
            BeanUtils.copyProperties(dto, item);
            item.setId(null);
            item.setQuotationId(quotationId);
            item.setLineNo(lineNo++);

            // 计算行总金额
            if (dto.getQuantity() != null && dto.getUnitPrice() != null) {
                item.setTotalAmount(dto.getQuantity().multiply(dto.getUnitPrice()));
            }

            // 计算行总成本
            if (dto.getQuantity() != null && dto.getCostPrice() != null) {
                item.setTotalCost(dto.getQuantity().multiply(dto.getCostPrice()));
            }

            // 计算行利润率
            if (item.getTotalCost() != null && item.getTotalCost().compareTo(BigDecimal.ZERO) > 0
                    && item.getTotalAmount() != null) {
                BigDecimal profit = item.getTotalAmount().subtract(item.getTotalCost());
                item.setProfitMargin(profit.divide(item.getTotalCost(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")));
            }

            items.add(item);
        }

        if (!items.isEmpty()) {
            quotationItemMapper.batchInsert(items);
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<CrmQuotation> buildQueryWrapper(QuotationQuery query) {
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();

        if (query.getCustomerId() != null) {
            wrapper.eq(CrmQuotation::getCustomerId, query.getCustomerId());
        }
        if (StrUtil.isNotBlank(query.getStatus())) {
            wrapper.eq(CrmQuotation::getStatus, query.getStatus());
        }
        if (StrUtil.isNotBlank(query.getQuotationNo())) {
            wrapper.like(CrmQuotation::getQuotationNo, query.getQuotationNo());
        }
        if (query.getStartDate() != null) {
            wrapper.ge(CrmQuotation::getQuotationDate, query.getStartDate());
        }
        if (query.getEndDate() != null) {
            wrapper.le(CrmQuotation::getQuotationDate, query.getEndDate());
        }
        if (query.getOwnerId() != null) {
            wrapper.eq(CrmQuotation::getOwnerId, query.getOwnerId());
        }
        if (StrUtil.isNotBlank(query.getCurrency())) {
            wrapper.eq(CrmQuotation::getCurrency, query.getCurrency());
        }
        if (StrUtil.isNotBlank(query.getTradeTerm())) {
            wrapper.eq(CrmQuotation::getTradeTerm, query.getTradeTerm());
        }
        if (query.getNeedApproval() != null) {
            wrapper.eq(CrmQuotation::getNeedApproval, query.getNeedApproval());
        }
        if (query.getInquiryId() != null) {
            wrapper.eq(CrmQuotation::getInquiryId, query.getInquiryId());
        }
        if (query.getMinAmount() != null) {
            wrapper.ge(CrmQuotation::getTotalAmount, query.getMinAmount());
        }
        if (query.getMaxAmount() != null) {
            wrapper.le(CrmQuotation::getTotalAmount, query.getMaxAmount());
        }
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w.like(CrmQuotation::getCustomerName, query.getKeyword())
                    .or()
                    .like(CrmQuotation::getQuotationNo, query.getKeyword()));
        }

        wrapper.orderByDesc(CrmQuotation::getCreatedTime);
        return wrapper;
    }

    /**
     * 转换为主表VO
     */
    private QuotationVO convertToVO(CrmQuotation quotation) {
        QuotationVO vo = new QuotationVO();
        BeanUtils.copyProperties(quotation, vo);
        vo.setStatusDesc(vo.getStatusDesc()); // 触发getter计算状态描述
        return vo;
    }

    /**
     * 转换为明细VO
     */
    private QuotationItemVO convertItemToVO(CrmQuotationItem item) {
        QuotationItemVO vo = new QuotationItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
}
