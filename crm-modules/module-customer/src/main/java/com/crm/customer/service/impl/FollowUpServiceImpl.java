package com.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.customer.domain.dto.FollowUpCreateDTO;
import com.crm.customer.domain.entity.CrmFollowUp;
import com.crm.customer.domain.vo.FollowUpVO;
import com.crm.customer.mapper.CrmFollowUpMapper;
import com.crm.customer.service.FollowUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 跟进记录服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class FollowUpServiceImpl extends ServiceImpl<CrmFollowUpMapper, CrmFollowUp> implements FollowUpService {

    private final CrmFollowUpMapper followUpMapper;

    @Override
    public IPage<FollowUpVO> pageFollowUps(Long customerId, Long followUserId, int pageNum, int pageSize) {
        Page<CrmFollowUp> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CrmFollowUp> wrapper = new LambdaQueryWrapper<>();
        
        // 客户ID过滤
        if (customerId != null) {
            wrapper.eq(CrmFollowUp::getCustomerId, customerId);
        }
        
        // 跟进人ID过滤
        if (followUserId != null) {
            wrapper.eq(CrmFollowUp::getFollowUserId, followUserId);
        }
        
        wrapper.orderByDesc(CrmFollowUp::getFollowTime);
        
        IPage<CrmFollowUp> followUpPage = this.page(page, wrapper);
        return followUpPage.convert(this::convertToVO);
    }

    @Override
    public FollowUpVO getFollowUpById(Long id) {
        CrmFollowUp followUp = this.getById(id);
        if (followUp == null) {
            return null;
        }
        return convertToVO(followUp);
    }

    @Override
    public List<FollowUpVO> getFollowUpTimeline(Long customerId) {
        List<CrmFollowUp> followUps = followUpMapper.selectByCustomerId(customerId);
        return followUps.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createFollowUp(FollowUpCreateDTO dto) {
        CrmFollowUp followUp = new CrmFollowUp();
        BeanUtil.copyProperties(dto, followUp);
        
        // 设置跟进时间
        followUp.setFollowTime(LocalDateTime.now());
        
        // TODO: 从SecurityContext获取当前用户ID
        followUp.setFollowUserId(1L);
        
        // 解析下次跟进时间
        if (StrUtil.isNotBlank(dto.getNextFollowTime())) {
            followUp.setNextFollowTime(LocalDateTime.parse(dto.getNextFollowTime()));
        }
        
        // 转换附件URL列表为JSON字符串
        if (dto.getAttachmentUrls() != null && !dto.getAttachmentUrls().isEmpty()) {
            followUp.setAttachmentUrls(String.join(",", dto.getAttachmentUrls()));
        }
        
        // 转换照片列表为JSON字符串
        if (dto.getPhotos() != null && !dto.getPhotos().isEmpty()) {
            followUp.setPhotos(String.join(",", dto.getPhotos()));
        }
        
        return this.save(followUp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFollowUp(Long id, String followContent, String nextPlan, String nextFollowTime, Integer satisfaction) {
        CrmFollowUp followUp = this.getById(id);
        if (followUp == null) {
            throw new RuntimeException("跟进记录不存在");
        }
        
        if (StrUtil.isNotBlank(followContent)) {
            followUp.setFollowContent(followContent);
        }
        
        if (StrUtil.isNotBlank(nextPlan)) {
            followUp.setNextPlan(nextPlan);
        }
        
        if (StrUtil.isNotBlank(nextFollowTime)) {
            followUp.setNextFollowTime(LocalDateTime.parse(nextFollowTime));
        }
        
        if (satisfaction != null) {
            followUp.setSatisfaction(satisfaction);
        }
        
        return this.updateById(followUp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFollowUps(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setReminder(Long followUpId, String nextFollowTime) {
        CrmFollowUp followUp = this.getById(followUpId);
        if (followUp == null) {
            throw new RuntimeException("跟进记录不存在");
        }
        
        followUp.setNextFollowTime(LocalDateTime.parse(nextFollowTime));
        
        return this.updateById(followUp);
    }

    @Override
    public List<FollowUpVO> getTodayFollowUps() {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        List<CrmFollowUp> followUps = followUpMapper.selectTodayFollowUps(todayStart, todayEnd);
        return followUps.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowUpVO> getUpcomingReminders(Integer hours) {
        if (hours == null || hours <= 0) {
            hours = 24; // 默认24小时
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusHours(hours);
        
        List<CrmFollowUp> followUps = followUpMapper.selectUpcomingReminders(now, endTime);
        return followUps.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 实体转VO
     */
    private FollowUpVO convertToVO(CrmFollowUp followUp) {
        FollowUpVO vo = new FollowUpVO();
        BeanUtil.copyProperties(followUp, vo);
        
        // 格式化通话时长
        if (followUp.getDuration() != null) {
            int minutes = followUp.getDuration() / 60;
            int seconds = followUp.getDuration() % 60;
            vo.setDurationFormatted(String.format("%d分%d秒", minutes, seconds));
        }
        
        // 解析附件URL列表
        if (StrUtil.isNotBlank(followUp.getAttachmentUrls())) {
            vo.setAttachmentUrls(List.of(followUp.getAttachmentUrls().split(",")));
        }
        
        // 解析照片列表
        if (StrUtil.isNotBlank(followUp.getPhotos())) {
            vo.setPhotos(List.of(followUp.getPhotos().split(",")));
        }
        
        // 设置跟进方式名称
        vo.setFollowTypeName(getFollowTypeName(followUp.getFollowType()));
        
        // TODO: 补充关联数据
        // - 公司名称
        // - 联系人姓名
        // - 跟进人姓名
        
        return vo;
    }

    /**
     * 获取跟进方式名称
     */
    private String getFollowTypeName(String followType) {
        if (StrUtil.isBlank(followType)) {
            return "";
        }
        switch (followType.toLowerCase()) {
            case "email":
                return "邮件";
            case "phone":
                return "电话";
            case "wechat":
                return "微信";
            case "meeting":
                return "面谈";
            case "video":
                return "视频会议";
            default:
                return followType;
        }
    }
}
