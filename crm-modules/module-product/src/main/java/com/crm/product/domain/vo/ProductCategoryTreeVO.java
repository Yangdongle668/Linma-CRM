package com.crm.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品分类树VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "产品分类树VO")
public class ProductCategoryTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    private Long id;

    /**
     * 父分类ID
     */
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    /**
     * 分类名称(中文)
     */
    @Schema(description = "分类名称(中文)", example = "电子产品")
    private String categoryNameCn;

    /**
     * 分类名称(英文)
     */
    @Schema(description = "分类名称(英文)", example = "Electronics")
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
     * 分类层级
     */
    @Schema(description = "分类层级", example = "1")
    private Integer level;

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

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 子分类列表
     */
    @Schema(description = "子分类列表")
    private List<ProductCategoryTreeVO> children;
}
