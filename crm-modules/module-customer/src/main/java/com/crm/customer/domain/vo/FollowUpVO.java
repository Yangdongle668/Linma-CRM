package com.crm.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 跟进记录视图对象VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "跟进记录信息")
public class FollowUpVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "跟进ID")
    private Long id;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "跟进方式(email/phone/wechat/meeting/video)")
    private String followType;

    @Schema(description = "跟进方式名称")
    private String followTypeName;

    @Schema(description = "跟进内容")
    private String followContent;

    @Schema(description = "下一步计划")
    private String nextPlan;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "附件URL列表")
    private List<String> attachmentUrls;

    @Schema(description = "通话时长(秒)")
    private Integer duration;

    @Schema(description = "通话时长格式化")
    private String durationFormatted;

    @Schema(description = "客户满意度(1-5星)")
    private Integer satisfaction;

    @Schema(description = "跟进人ID")
    private Long followUserId;

    @Schema(description = "跟进人姓名")
    private String followUserName;

    @Schema(description = "跟进时间")
    private LocalDateTime followTime;

    @Schema(description = "拜访地点(GPS坐标)")
    private String location;

    @Schema(description = "拜访照片URL列表")
    private List<String> photos;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
