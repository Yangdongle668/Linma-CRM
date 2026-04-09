package com.crm.contract.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 合同模板查询DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同模板查询DTO")
public class ContractTemplateQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称(模糊搜索)
     */
    @Schema(description = "模板名称", example = "销售")
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
    private String contractType;

    /**
     * 语言
     */
    @Schema(description = "语言(zh/en)", example = "zh")
    private String language;

    /**
     * 状态
     */
    @Schema(description = "状态(1启用 0禁用)", example = "1")
    private Integer status;

    /**
     * 是否默认模板
     */
    @Schema(description = "是否默认模板", example = "true")
    private Boolean isDefault;
}
