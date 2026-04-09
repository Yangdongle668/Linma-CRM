package com.crm.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品分类更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "产品分类更新DTO")
public class ProductCategoryUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    @NotNull(message = "分类ID不能为空")
    private Long id;

    /**
     * 父分类ID(0表示根节点)
     */
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    /**
     * 分类名称(中文)
     */
    @Schema(description = "分类名称(中文)", example = "电子产品")
    @NotBlank(message = "分类名称(中文)不能为空")
    private String categoryNameCn;

    /**
     * 分类名称(英文)
     */
    @Schema(description = "分类名称(英文)", example = "Electronics")
    @NotBlank(message = "分类名称(英文)不能为空")
    private String categoryNameEn;

    /**
     * 分类编码
     */
    @Schema(description = "分类编码", example = "ELECTRONICS")
    private String categoryCode;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序", example = "1")
    private Integer sortOrder;

    /**
     * 状态(1正常 0禁用)
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 图标
     */
    @Schema(description = "图标", example = "icon-electronics")
    private String icon;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "电子产品分类")
    private String description;
}
