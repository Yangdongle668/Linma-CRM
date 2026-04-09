package com.crm.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 跟进记录实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_follow_up")
public class CrmFollowUp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跟进ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 跟进方式(email/phone/wechat/meeting/video)
     */
    private String followType;

    /**
     * 跟进内容
     */
    private String followContent;

    /**
     * 下一步计划
     */
    private String nextPlan;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 附件URL列表(JSON格式)
     */
    private String attachmentUrls;

    /**
     * 通话时长(秒)
     */
    private Integer duration;

    /**
     * 客户满意度(1-5星)
     */
    private Integer satisfaction;

    /**
     * 跟进人ID
     */
    private Long followUserId;

    /**
     * 跟进时间
     */
    private LocalDateTime followTime;

    /**
     * 拜访地点(GPS坐标)
     */
    private String location;

    /**
     * 拜访照片(JSON格式)
     */
    private String photos;

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
public class CrmFollowUp {
    
}
