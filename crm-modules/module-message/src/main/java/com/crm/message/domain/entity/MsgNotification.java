package com.crm.message.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 站内消息实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("msg_notification")
public class MsgNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型(system系统通知/order订单通知/customer客户通知/task任务通知)
     */
    private String type;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 发送人ID(0表示系统消息)
     */
    private Long senderId;

    /**
     * 是否已读(0未读 1已读)
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 业务类型(关联的业务类型)
     */
    private String businessType;

    /**
     * 业务ID(关联的业务ID)
     */
    private Long businessId;

    /**
     * 跳转链接
     */
    private String jumpUrl;

    /**
     * 优先级(low低/medium中/high高)
     */
    private String priority;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
