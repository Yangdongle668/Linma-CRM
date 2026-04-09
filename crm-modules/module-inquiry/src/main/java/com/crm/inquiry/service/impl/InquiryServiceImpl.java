package com.crm.inquiry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.inquiry.domain.dto.InquiryCreateDTO;
import com.crm.inquiry.domain.dto.InquiryQuery;
import com.crm.inquiry.domain.dto.InquiryReplyDTO;
import com.crm.inquiry.domain.dto.InquiryUpdateDTO;
import com.crm.inquiry.domain.entity.CrmInquiry;
import com.crm.inquiry.domain.vo.InquiryVO;
import com.crm.inquiry.mapper.InquiryMapper;
import com.crm.inquiry.service.InquiryAutoAssignService;
import com.crm.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 询盘Service实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl extends ServiceImpl<InquiryMapper, CrmInquiry> implements InquiryService {

    private final InquiryMapper inquiryMapper;
    private final InquiryAutoAssignService autoAssignService;

    @Override
    public IPage<InquiryVO> getInquiryPage(Page<InquiryVO> page, InquiryQuery query) {
        return inquiryMapper.selectInquiryPage(page, query);
    }

    @Override
    public InquiryVO getInquiryById(Long id) {
        CrmInquiry inquiry = this.getById(id);
        if (inquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        InquiryVO vo = BeanUtil.copyProperties(inquiry, InquiryVO.class);
        
        // 设置状态描述
        vo.setStatusDesc(getStatusDesc(inquiry.getStatus()));
        vo.setPriorityDesc(getPriorityDesc(inquiry.getPriority()));
        vo.setSourceDesc(getSourceDesc(inquiry.getSource()));
        
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createInquiry(InquiryCreateDTO dto) {
        CrmInquiry inquiry = BeanUtil.copyProperties(dto, CrmInquiry.class);
        
        // 生成询盘号
        String inquiryNo = generateInquiryNo();
        inquiry.setInquiryNo(inquiryNo);
        
        // 设置初始状态
        inquiry.setStatus("new");
        
        // 设置默认优先级
        if (StrUtil.isBlank(inquiry.getPriority())) {
            inquiry.setPriority("MEDIUM");
        }
        
        // 设置回复次数
        inquiry.setReplyCount(0);

        this.save(inquiry);
        log.info("创建询盘成功, ID: {}, 询盘号: {}", inquiry.getId(), inquiryNo);
        return inquiry.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInquiry(InquiryUpdateDTO dto) {
        CrmInquiry existInquiry = this.getById(dto.getId());
        if (existInquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        CrmInquiry inquiry = BeanUtil.copyProperties(dto, CrmInquiry.class);
        this.updateById(inquiry);
        log.info("更新询盘成功, ID: {}", inquiry.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInquiries(List<Long> ids) {
        this.removeByIds(ids);
        log.info("删除询盘成功, IDs: {}", ids);
    }

    @Override
    public String generateInquiryNo() {
        String dateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String prefix = "INQ" + dateStr;
        
        // 查询当天最大序号
        LambdaQueryWrapper<CrmInquiry> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CrmInquiry::getInquiryNo, prefix)
               .orderByDesc(CrmInquiry::getInquiryNo)
               .last("LIMIT 1");
        
        CrmInquiry lastInquiry = this.getOne(wrapper);
        
        int sequence = 1;
        if (lastInquiry != null && StrUtil.isNotBlank(lastInquiry.getInquiryNo())) {
            String lastNo = lastInquiry.getInquiryNo();
            String lastSeq = lastNo.substring(prefix.length());
            sequence = Integer.parseInt(lastSeq) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyInquiry(InquiryReplyDTO replyDTO) {
        CrmInquiry inquiry = this.getById(replyDTO.getInquiryId());
        if (inquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        // 更新最后回复时间和回复次数
        inquiry.setLastReplyTime(LocalDateTime.now());
        inquiry.setReplyCount(inquiry.getReplyCount() + 1);
        
        // 如果标记为已报价，更新状态
        if (Boolean.TRUE.equals(replyDTO.getMarkAsQuoted())) {
            inquiry.setStatus("quoted");
        } else if ("new".equals(inquiry.getStatus())) {
            // 如果当前状态是新建，改为处理中
            inquiry.setStatus("processing");
        }

        this.updateById(inquiry);
        log.info("回复询盘成功, 询盘ID: {}, 回复次数: {}", inquiry.getId(), inquiry.getReplyCount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long inquiryId, String newStatus, String reason) {
        CrmInquiry inquiry = this.getById(inquiryId);
        if (inquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        // 验证状态流转合法性
        validateStatusTransition(inquiry.getStatus(), newStatus);

        String oldStatus = inquiry.getStatus();
        inquiry.setStatus(newStatus);
        
        // 如果关闭，记录关闭原因
        if ("closed".equals(newStatus)) {
            inquiry.setCloseReason(reason);
        }

        this.updateById(inquiry);
        log.info("变更询盘状态成功, 询盘ID: {}, {} -> {}", inquiryId, oldStatus, newStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long convertToQuotation(Long inquiryId) {
        CrmInquiry inquiry = this.getById(inquiryId);
        if (inquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        if ("converted".equals(inquiry.getStatus())) {
            throw new BusinessException("询盘已转化为报价单");
        }

        // 这里应该调用报价单服务创建报价单
        // 由于模块解耦，实际项目中应通过Feign或事件驱动方式调用
        // Long quotationId = quotationService.createFromInquiry(inquiry);
        
        // 模拟创建报价单
        Long quotationId = inquiryId * 1000L;
        inquiry.setQuotationId(quotationId);
        inquiry.setStatus("converted");
        
        this.updateById(inquiry);
        log.info("询盘转化为报价单成功, 询盘ID: {}, 报价单ID: {}", inquiryId, quotationId);
        
        return quotationId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeInquiry(Long inquiryId, String reason) {
        changeStatus(inquiryId, "closed", reason);
        log.info("关闭询盘成功, 询盘ID: {}, 原因: {}", inquiryId, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignInquiry(Long inquiryId, Long ownerId) {
        CrmInquiry inquiry = this.getById(inquiryId);
        if (inquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        inquiry.setOwnerId(ownerId);
        
        // 如果状态是新建，改为处理中
        if ("new".equals(inquiry.getStatus())) {
            inquiry.setStatus("processing");
        }

        this.updateById(inquiry);
        log.info("分配询盘成功, 询盘ID: {}, 负责人ID: {}", inquiryId, ownerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoAssignInquiry(Long inquiryId, String strategy) {
        CrmInquiry inquiry = this.getById(inquiryId);
        if (inquiry == null) {
            throw new BusinessException("询盘不存在");
        }

        Long assignedUserId = null;

        switch (strategy.toLowerCase()) {
            case "round_robin":
                // 轮询分配 - 从配置或数据库获取销售人员列表
                Long[] salesUserIds = {1L, 2L, 3L, 4L, 5L};
                assignedUserId = autoAssignService.roundRobinAssign(salesUserIds);
                break;
                
            case "load_balance":
                // 负载均衡分配
                Long[] userIds = {1L, 2L, 3L};
                Integer[] pendingCounts = {5, 3, 8};
                assignedUserId = autoAssignService.loadBalanceAssign(userIds, pendingCounts);
                break;
                
            case "region_match":
                // 区域匹配分配
                // Map<String, Long> regionSalesMap = getRegionSalesMap();
                // assignedUserId = autoAssignService.regionMatchAssign(inquiry.getDestinationCountry(), regionSalesMap);
                log.warn("区域匹配策略需要配置区域映射关系");
                break;
                
            default:
                throw new BusinessException("不支持的分配策略: " + strategy);
        }

        if (assignedUserId != null) {
            assignInquiry(inquiryId, assignedUserId);
        } else {
            log.warn("自动分配失败，未找到合适的销售人员");
        }
    }

    @Override
    public List<InquiryVO> exportInquiries(InquiryQuery query) {
        return inquiryMapper.selectInquiryForExport(query);
    }

    @Override
    public Integer countPendingInquiries(Long ownerId) {
        return inquiryMapper.countPendingInquiries(ownerId);
    }

    @Override
    public Integer countConvertedInquiries(Long ownerId) {
        return inquiryMapper.countConvertedInquiries(ownerId);
    }

    /**
     * 验证状态流转合法性
     *
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     */
    private void validateStatusTransition(String currentStatus, String newStatus) {
        // 定义合法的状态流转
        // new -> processing -> quoted -> converted
        // new/processing/quoted -> closed
        
        if ("closed".equals(currentStatus)) {
            throw new BusinessException("已关闭的询盘不能变更状态");
        }
        
        if ("converted".equals(currentStatus)) {
            throw new BusinessException("已转化的询盘不能变更状态");
        }
    }

    /**
     * 获取状态描述
     *
     * @param status 状态
     * @return 状态描述
     */
    private String getStatusDesc(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "new":
                return "新建";
            case "processing":
                return "处理中";
            case "quoted":
                return "已报价";
            case "converted":
                return "已转化";
            case "closed":
                return "已关闭";
            default:
                return status;
        }
    }

    /**
     * 获取优先级描述
     *
     * @param priority 优先级
     * @return 优先级描述
     */
    private String getPriorityDesc(String priority) {
        if (priority == null) {
            return "";
        }
        switch (priority) {
            case "HIGH":
                return "高优先级";
            case "MEDIUM":
                return "中优先级";
            case "LOW":
                return "低优先级";
            default:
                return priority;
        }
    }

    /**
     * 获取来源描述
     *
     * @param source 来源
     * @return 来源描述
     */
    private String getSourceDesc(String source) {
        if (source == null) {
            return "";
        }
        switch (source) {
            case "alibaba":
                return "阿里巴巴国际站";
            case "website":
                return "官网";
            case "email":
                return "邮件";
            case "exhibition":
                return "展会";
            case "referral":
                return "转介绍";
            default:
                return source;
        }
    }
}
