package com.crm.message.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息通知查询条件
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "消息通知查询条件")
public class NotificationQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息类型(system系统通知/order订单通知/customer客户通知/task任务通知)")
    private String type;

    @Schema(description = "是否已读(0未读 1已读)")
    private Integer isRead;

    @Schema(description = "优先级(low低/medium中/high高)")
    private String priority;

    @Schema(description = "业务类型")
    private String businessType;
}
