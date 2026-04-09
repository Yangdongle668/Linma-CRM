package com.crm.shipping.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发货单实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_shipping")
public class CrmShipping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发货单号(自动生成,格式:SHP20260409001)
     */
    private String shippingNo;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 订单号(冗余字段)
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称(冗余字段)
     */
    private String customerName;

    /**
     * 物流商(DHL/FedEx/UPS/海运/空运)
     */
    private String carrier;

    /**
     * 运单号
     */
    private String trackingNo;

    /**
     * 发货日期
     */
    private LocalDate shippingDate;

    /**
     * 预计到达日期
     */
    private LocalDate estimatedArrivalDate;

    /**
     * 实际到达日期
     */
    private LocalDate actualArrivalDate;

    /**
     * 箱数
     */
    private Integer cartonCount;

    /**
     * 总体积(CBM)
     */
    private BigDecimal totalVolume;

    /**
     * 总毛重(KG)
     */
    private BigDecimal totalGrossWeight;

    /**
     * 总净重(KG)
     */
    private BigDecimal totalNetWeight;

    /**
     * 报关状态(pending待报关/clearing报关中/cleared已报关)
     */
    private String customsStatus;

    /**
     * 报关单号
     */
    private String customsDeclarationNo;

    /**
     * 发货状态(pending待发货/shipped已发货/in_transit运输中/delivered已送达)
     */
    private String status;

    /**
     * 起运港
     */
    private String portOfLoading;

    /**
     * 目的港
     */
    private String portOfDestination;

    /**
     * 贸易术语(FOB/CIF/DDP等)
     */
    private String tradeTerm;

    /**
     * 备注
     */
    private String remark;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 所属部门ID
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
}
