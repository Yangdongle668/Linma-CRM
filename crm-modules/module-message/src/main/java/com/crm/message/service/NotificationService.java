package com.crm.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.message.domain.dto.NotificationQuery;
import com.crm.message.domain.entity.MsgNotification;
import com.crm.message.domain.vo.NotificationVO;

import java.util.List;

/**
 * 站内消息服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface NotificationService extends IService<MsgNotification> {

    /**
     * 分页查询消息列表
     *
     * @param userId 用户ID
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 消息分页数据
     */
    IPage<NotificationVO> pageNotifications(Long userId, NotificationQuery query, int pageNum, int pageSize);

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 标记消息为已读
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long messageId, Long userId);

    /**
     * 批量标记已读
     *
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean batchMarkAsRead(List<Long> messageIds, Long userId);

    /**
     * 发送站内消息
     *
     * @param notification 消息对象
     * @return 消息ID
     */
    Long sendNotification(MsgNotification notification);

    /**
     * 发送系统通知
     *
     * @param receiverId 接收人ID
     * @param title 标题
     * @param content 内容
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @param jumpUrl 跳转链接
     * @return 消息ID
     */
    Long sendSystemNotification(Long receiverId, String title, String content,
                                 String businessType, Long businessId, String jumpUrl);
}
