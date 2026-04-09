package com.crm.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签视图对象VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "标签信息")
public class TagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标签ID")
    private Long id;

    @Schema(description = "标签名称")
    private String tagName;

    @Schema(description = "标签颜色")
    private String tagColor;

    @Schema(description = "标签类型(manual手动 auto自动)")
    private String tagType;

    @Schema(description = "使用次数")
    private Integer usageCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
