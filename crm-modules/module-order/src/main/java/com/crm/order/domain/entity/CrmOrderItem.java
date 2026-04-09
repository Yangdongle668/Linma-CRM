package com.crm.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_order_item")
public class CrmOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 行号
     */
    private Integer lineNo;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品编号
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
     * 产品规格/型号
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
     * 订单数量
     */
    private BigDecimal quantity;

    /**
     * 已生产数量
     */
    private BigDecimal producedQuantity;

    /**
     * 已发货数量
     */
    private BigDecimal shippedQuantity;

    /**
     * 单价(原币)
     */
    private BigDecimal unitPrice;

    /**
     * 总金额(原币)=数量*单价
     */
    private BigDecimal totalAmount;

    /**
     * 成本价(CNY)
     */
    private BigDecimal costPrice;

    /**
     * 总成本(CNY)=数量*成本价
     */
    private BigDecimal totalCost;

    /**
     * 利润率(%)
     */
    private BigDecimal profitMargin;

    /**
     * 包装方式
     */
    private String packingMethod;

    /**
     * 每箱数量
     */
    private Integer quantityPerCarton;

    /**
     * 箱数
     */
    private Integer cartonCount;

    /**
     * 单箱毛重(KG)
     */
    private BigDecimal grossWeightPerCarton;

    /**
     * 总毛重(KG)
     */
    private BigDecimal totalGrossWeight;

    /**
     * 单箱净重(KG)
     */
    private BigDecimal netWeightPerCarton;

    /**
     * 总净重(KG)
     */
    private BigDecimal totalNetWeight;

    /**
     * 单箱体积(CBM)
     */
    private BigDecimal volumePerCarton;

    /**
     * 总体积(CBM)
     */
    private BigDecimal totalVolume;

    /**
     * HS编码
     */
    private String hsCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
