package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 跟进记录创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "跟进记录创建信息")
public class FollowUpCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "联系人ID")
    private Long contactId;

    @Schema(description = "跟进方式(email/phone/wechat/meeting/video)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "跟进方式不能为空")
    private String followType;

    @Schema(description = "跟进内容")
    private String followContent;

    @Schema(description = "下一步计划")
    private String nextPlan;

    @Schema(description = "下次跟进时间")
    private String nextFollowTime;

    @Schema(description = "附件URL列表")
    private List<String> attachmentUrls;

    @Schema(description = "通话时长(秒)")
    private Integer duration;

    @Schema(description = "客户满意度(1-5星)")
    private Integer satisfaction;

    @Schema(description = "拜访地点(GPS坐标)")
    private String location;

    @Schema(description = "拜访照片URL列表")
    private List<String> photos;
}
