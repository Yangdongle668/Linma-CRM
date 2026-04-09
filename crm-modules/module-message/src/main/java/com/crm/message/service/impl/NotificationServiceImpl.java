package com.crm.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.message.domain.dto.NotificationQuery;
import com.crm.message.domain.entity.MsgNotification;
import com.crm.message.domain.vo.NotificationVO;
import com.crm.message.mapper.MsgNotificationMapper;
import com.crm.message.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 站内消息服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<MsgNotificationMapper, MsgNotification> implements NotificationService {

    private final MsgNotificationMapper notificationMapper;

    @Override
    public IPage<NotificationVO> pageNotifications(Long userId, NotificationQuery query, int pageNum, int pageSize) {
        Page<NotificationVO> page = new Page<>(pageNum, pageSize);
        // TODO: 实现分页查询
        return null;
    }

    @Override
    public Long getUnreadCount(Long userId) {
        LambdaQueryWrapper<MsgNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgNotification::getReceiverId, userId)
                .eq(MsgNotification::getIsRead, 0);
        return count(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long messageId, Long userId) {
        MsgNotification notification = getById(messageId);
        if (notification == null || !notification.getReceiverId().equals(userId)) {
            return false;
        }

        notification.setIsRead(1);
        notification.setReadTime(LocalDateTime.now());
        return updateById(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchMarkAsRead(List<Long> messageIds, Long userId) {
        LambdaQueryWrapper<MsgNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MsgNotification::getId, messageIds)
                .eq(MsgNotification::getReceiverId, userId);

        List<MsgNotification> notifications = list(wrapper);
        for (MsgNotification notification : notifications) {
            notification.setIsRead(1);
            notification.setReadTime(LocalDateTime.now());
        }

        return updateBatchById(notifications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendNotification(MsgNotification notification) {
        notification.setIsRead(0);
        save(notification);
        return notification.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendSystemNotification(Long receiverId, String title, String content,
                                        String businessType, Long businessId, String jumpUrl) {
        MsgNotification notification = new MsgNotification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType("system");
        notification.setReceiverId(receiverId);
        notification.setSenderId(0L);
        notification.setBusinessType(businessType);
        notification.setBusinessId(businessId);
        notification.setJumpUrl(jumpUrl);
        notification.setPriority("medium");

        return sendNotification(notification);
    }
}
