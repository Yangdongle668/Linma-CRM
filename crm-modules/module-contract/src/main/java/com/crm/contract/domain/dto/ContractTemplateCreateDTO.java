package com.crm.contract.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 合同模板创建DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同模板创建DTO")
public class ContractTemplateCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称", example = "标准销售合同模板")
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    /**
     * 模板编码
     */
    @Schema(description = "模板编码", example = "SALES_STD_V1")
    private String templateCode;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型(sales/purchase/agency/nda)", example = "sales")
    @NotBlank(message = "合同类型不能为空")
    private String contractType;

    /**
     * 模板内容(含变量占位符)
     */
    @Schema(description = "模板内容", example = "本合同由{{customerName}}与本公司签订...")
    @NotBlank(message = "模板内容不能为空")
    private String templateContent;

    /**
     * 语言
     */
    @Schema(description = "语言(zh/en)", example = "zh")
    private String language;

    /**
     * 版本号
     */
    @Schema(description = "版本号", example = "1.0")
    private String version;

    /**
     * 是否默认模板
     */
    @Schema(description = "是否默认模板", example = "false")
    private Boolean isDefault;

    /**
     * 状态
     */
    @Schema(description = "状态(1启用 0禁用)", example = "1")
    private Integer status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;
}
