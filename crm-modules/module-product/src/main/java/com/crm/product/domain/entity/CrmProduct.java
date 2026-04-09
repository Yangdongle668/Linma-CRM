package com.crm.product.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_product")
public class CrmProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品编号(自动生成,格式:PRD20260409001)
     */
    private String productNo;

    /**
     * 产品名称(英文)
     */
    private String productName;

    /**
     * 产品名称(中文)
     */
    private String productNameCn;

    /**
     * 产品分类ID
     */
    private Long categoryId;

    /**
     * SKU编码
     */
    private String sku;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 单位(PCS/SET/KG等)
     */
    private String unit;

    /**
     * 成本价(CNY)
     */
    private BigDecimal costPrice;

    /**
     * 出厂价(CNY)
     */
    private BigDecimal factoryPrice;

    /**
     * FOB价格(USD)
     */
    private BigDecimal fobPrice;

    /**
     * CIF价格(USD)
     */
    private BigDecimal cifPrice;

    /**
     * 币种
     */
    private String currency;

    /**
     * 库存数量
     */
    private Integer stockQuantity;

    /**
     * 最小起订量(MOQ)
     */
    private Integer minOrderQuantity;

    /**
     * HS编码
     */
    private String hsCode;

    /**
     * 退税率(%)
     */
    private BigDecimal taxRefundRate;

    /**
     * 毛重(KG)
     */
    private BigDecimal grossWeight;

    /**
     * 净重(KG)
     */
    private BigDecimal netWeight;

    /**
     * 包装尺寸(长*宽*高 cm)
     */
    private String packingSize;

    /**
     * 产品图片URL(多个用逗号分隔)
     */
    private String images;

    /**
     * 主图URL
     */
    private String mainImage;

    /**
     * 状态(1上架 0下架)
     */
    private Integer status;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 部门ID
     */
    private Long departmentId;

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
     * 备注
     */
    private String remark;
}
