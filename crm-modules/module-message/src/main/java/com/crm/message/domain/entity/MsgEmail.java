package com.crm.message.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮件实体类 - 存储收件箱和发件箱的邮件
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("msg_email")
public class MsgEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（邮件所属用户）
     */
    private Long userId;

    /**
     * 邮件方向：inbound-收件, outbound-发件
     */
    private String direction;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 发件人邮箱
     */
    private String fromEmail;

    /**
     * 发件人名称
     */
    private String fromName;

    /**
     * 收件人邮箱
     */
    private String toEmail;

    /**
     * 抄送邮箱
     */
    private String ccEmail;

    /**
     * 邮件内容(HTML)
     */
    private String content;

    /**
     * 纯文本内容预览
     */
    private String preview;

    /**
     * 是否有附件
     */
    private Boolean hasAttachment;

    /**
     * 附件信息(JSON格式)
     */
    private String attachments;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 是否星标
     */
    private Boolean isStarred;

    /**
     * 文件夹：inbox/starred/sent/drafts/archive/spam/trash
     */
    private String folder;

    /**
     * 标签(JSON数组)
     */
    private String labels;

    /**
     * 关联客户ID
     */
    private Long customerId;

    /**
     * 关联邮箱设置ID
     */
    private Long emailSettingsId;

    /**
     * 邮件接收/发送时间
     */
    private LocalDateTime messageTime;

    /**
     * 删除标记：0-未删除, 1-已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
