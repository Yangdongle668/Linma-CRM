package com.crm.product.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品分类查询DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "产品分类查询DTO")
public class ProductCategoryQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称(模糊搜索)
     */
    @Schema(description = "分类名称", example = "电子")
    private String categoryName;

    /**
     * 分类编码
     */
    @Schema(description = "分类编码", example = "ELECTRONICS")
    private String categoryCode;

    /**
     * 状态(1正常 0禁用)
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 父分类ID
     */
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;
}
