package com.crm.product.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品分类实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_product_category")
public class CrmProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父分类ID(0表示根节点)
     */
    private Long parentId;

    /**
     * 分类名称(中文)
     */
    private String categoryNameCn;

    /**
     * 分类名称(英文)
     */
    private String categoryNameEn;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

    /**
     * 分类层级
     */
    private Integer level;

    /**
     * 状态(1正常 0禁用)
     */
    private Integer status;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新者
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 子分类列表(非数据库字段)
     */
    @TableField(exist = false)
    private List<CrmProductCategory> children;
}
