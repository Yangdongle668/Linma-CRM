package com.crm.contract.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合同模板VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "合同模板VO")
public class ContractTemplateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @Schema(description = "模板ID", example = "1")
    private Long id;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称", example = "标准销售合同模板")
    private String templateName;

    /**
     * 模板编码
     */
    @Schema(description = "模板编码", example = "SALES_STD_V1")
    private String templateCode;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", example = "sales")
    private String contractType;

    /**
     * 合同类型描述
     */
    @Schema(description = "合同类型描述", example = "销售合同")
    private String contractTypeDesc;

    /**
     * 模板内容(含变量占位符)
     */
    @Schema(description = "模板内容")
    private String templateContent;

    /**
     * 语言
     */
    @Schema(description = "语言", example = "zh")
    private String language;

    /**
     * 语言描述
     */
    @Schema(description = "语言描述", example = "中文")
    private String languageDesc;

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
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述", example = "启用")
    private String statusDesc;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 创建者
     */
    @Schema(description = "创建者", example = "1")
    private Long createdBy;

    /**
     * 创建者姓名
     */
    @Schema(description = "创建者姓名", example = "管理员")
    private String createdByName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
}
