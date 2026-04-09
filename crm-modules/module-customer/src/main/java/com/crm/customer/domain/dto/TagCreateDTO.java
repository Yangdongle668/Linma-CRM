package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "标签创建信息")
public class TagCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标签名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标签名称不能为空")
    private String tagName;

    @Schema(description = "标签颜色")
    private String tagColor;

    @Schema(description = "标签类型(manual手动 auto自动)")
    private String tagType;

    @Schema(description = "自动打标规则(JSON格式)")
    private String autoRule;
}
public class TagCreateDTO {
    
}
