package com.crm.message.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息通知VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "消息通知视图对象")
public class NotificationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息ID")
    private Long id;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "消息类型")
    private String type;

    @Schema(description = "接收人ID")
    private Long receiverId;

    @Schema(description = "发送人ID")
    private Long senderId;

    @Schema(description = "发送人姓名")
    private String senderName;

    @Schema(description = "是否已读(0未读 1已读)")
    private Integer isRead;

    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "业务ID")
    private Long businessId;

    @Schema(description = "跳转链接")
    private String jumpUrl;

    @Schema(description = "优先级")
    private String priority;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
