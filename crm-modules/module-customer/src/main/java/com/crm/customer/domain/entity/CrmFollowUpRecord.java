package com.crm.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户跟进记录实体类
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Data
@TableName("crm_follow_up_record")
public class CrmFollowUpRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 联系人ID
     */
    private Long contactId;

    /**
     * 跟进类型(email/call/meeting/visit/message/other)
     */
    private String followType;

    /**
     * 主题
     */
    private String subject;

    /**
     * 跟进内容
     */
    private String content;

    /**
     * 方向(inbound/outbound)
     */
    private String direction;

    /**
     * 状态(planned/completed/cancelled)
     */
    private String status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 持续时间(分钟)
     */
    private Integer durationMinutes;

    /**
     * 地点
     */
    private String location;

    /**
     * 参与人员(JSON数组)
     */
    private String participantsJson;

    /**
     * 跟进结果
     */
    private String outcome;

    /**
     * 下一步行动
     */
    private String nextStep;

    /**
     * 下次跟进日期
     */
    private LocalDateTime nextFollowDate;

    /**
     * 情感倾向(positive/neutral/negative)
     */
    private String sentiment;

    /**
     * 重要性(low/normal/high/urgent)
     */
    private String importance;

    /**
     * 关联询盘ID
     */
    private Long relatedInquiryId;

    /**
     * 关联订单ID
     */
    private Long relatedOrderId;

    /**
     * 关联合同ID
     */
    private Long relatedContractId;

    /**
     * 关联邮件Message-ID
     */
    private String emailMessageId;

    /**
     * 附件列表(JSON)
     */
    private String attachmentsJson;

    /**
     * 创建人ID
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

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
