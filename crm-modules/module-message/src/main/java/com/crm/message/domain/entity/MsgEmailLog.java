package com.crm.message.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮件日志实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("msg_email_log")
public class MsgEmailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 收件人邮箱
     */
    private String toEmail;

    /**
     * 抄送邮箱
     */
    private String ccEmail;

    /**
     * 发件人邮箱
     */
    private String fromEmail;

    /**
     * 邮件内容(HTML)
     */
    private String content;

    /**
     * 附件路径
     */
    private String attachments;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 发送状态(pending待发送/sent已发送/failed发送失败)
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 打开时间(邮件追踪)
     */
    private LocalDateTime openTime;

    /**
     * 点击时间(链接追踪)
     */
    private LocalDateTime clickTime;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
