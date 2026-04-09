package com.crm.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 客户实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_customer")
public class CrmCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户编号（自动生成，格式：CUS20260409001）
     */
    private String customerNo;

    /**
     * 公司名称(英文)
     */
    private String companyName;

    /**
     * 公司名称(中文)
     */
    private String companyNameCn;

    /**
     * 国家代码
     */
    private String country;

    /**
     * 省份/州
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 公司网站
     */
    private String website;

    /**
     * 行业分类
     */
    private String industry;

    /**
     * 客户来源(展会/阿里/官网/转介绍)
     */
    private String source;

    /**
     * 客户等级(A/B/C/D)
     */
    private String level;

    /**
     * 状态(1正常 0禁用)
     */
    private Integer status;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 所属部门
     */
    private Long departmentId;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 询盘总数
     */
    private Integer totalInquiry;

    /**
     * 累计订单金额
     */
    private BigDecimal totalOrder;

    /**
     * 最后下单日期
     */
    private LocalDate lastOrderDate;

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
