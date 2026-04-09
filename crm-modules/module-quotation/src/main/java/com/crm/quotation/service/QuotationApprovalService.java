package com.crm.quotation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.common.core.exception.BusinessException;
import com.crm.quotation.domain.entity.CrmQuotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报价单审批服务
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationApprovalService {

    private final QuotationService quotationService;

    /**
     * 检查是否需要审批
     * 规则: 利润率 < 10% 需要主管审批
     *
     * @param quotation 报价单
     * @return 是否需要审批
     */
    public boolean checkNeedApproval(CrmQuotation quotation) {
        if (quotation.getProfitMargin() == null) {
            return false;
        }

        // 利润率小于10%需要审批
        BigDecimal threshold = new BigDecimal("10");
        boolean needApproval = quotation.getProfitMargin().compareTo(threshold) < 0;

        log.info("报价单 {} 利润率: {}%, 需要审批: {}",
                quotation.getQuotationNo(),
                quotation.getProfitMargin(),
                needApproval);

        return needApproval;
    }

    /**
     * 获取待审批的报价单列表
     *
     * @return 待审批报价单列表
     */
    public List<CrmQuotation> getPendingApprovals() {
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmQuotation::getStatus, "pending_approval")
                .orderByAsc(CrmQuotation::getCreatedTime);

        List<CrmQuotation> pendingList = quotationService.list(wrapper);
        log.info("查询到待审批报价单数量: {}", pendingList.size());

        return pendingList;
    }

    /**
     * 获取指定用户的待审批报价单
     *
     * @param approverId 审批人ID
     * @return 待审批报价单列表
     */
    public List<CrmQuotation> getPendingApprovalsByApprover(Long approverId) {
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmQuotation::getStatus, "pending_approval")
                .eq(CrmQuotation::getApproverId, approverId)
                .orderByAsc(CrmQuotation::getCreatedTime);

        List<CrmQuotation> pendingList = quotationService.list(wrapper);
        log.info("用户 {} 的待审批报价单数量: {}", approverId, pendingList.size());

        return pendingList;
    }

    /**
     * 自动提交低利润报价单审批
     * 定时任务调用,检查所有草稿状态的报价单,如果利润率<10%则自动提交审批
     *
     * @return 提交的报价单数量
     */
    public int autoSubmitLowProfitQuotations() {
        LambdaQueryWrapper<CrmQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmQuotation::getStatus, "draft")
                .lt(CrmQuotation::getProfitMargin, new BigDecimal("10"));

        List<CrmQuotation> lowProfitQuotations = quotationService.list(wrapper);

        int count = 0;
        for (CrmQuotation quotation : lowProfitQuotations) {
            try {
                quotation.setNeedApproval(true);
                quotation.setStatus("pending_approval");
                quotationService.updateById(quotation);
                count++;
                log.info("自动提交低利润报价单审批: {}", quotation.getQuotationNo());
            } catch (Exception e) {
                log.error("自动提交审批失败: {}", quotation.getQuotationNo(), e);
            }
        }

        log.info("自动提交低利润报价单审批完成, 数量: {}", count);
        return count;
    }

    /**
     * 验证审批权限
     *
     * @param quotationId 报价单ID
     * @param approverId 审批人ID
     * @return 是否有审批权限
     */
    public boolean validateApprovalPermission(Long quotationId, Long approverId) {
        CrmQuotation quotation = quotationService.getById(quotationId);
        if (quotation == null) {
            throw new BusinessException("报价单不存在");
        }

        if (!"pending_approval".equals(quotation.getStatus())) {
            throw new BusinessException("报价单不在待审批状态");
        }

        // TODO: 根据实际业务规则验证审批权限
        // 例如: 部门主管可以审批本部门的所有报价单
        // 或者: 只有指定的审批人可以审批

        return true;
    }
}
