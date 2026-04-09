package com.crm.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户视图对象VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户信息")
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
    private Long id;

    @Schema(description = "客户编号")
    private String customerNo;

    @Schema(description = "公司名称(英文)")
    private String companyName;

    @Schema(description = "公司名称(中文)")
    private String companyNameCn;

    @Schema(description = "国家代码")
    private String country;

    @Schema(description = "国家名称")
    private String countryName;

    @Schema(description = "省份/州")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "公司网站")
    private String website;

    @Schema(description = "行业分类")
    private String industry;

    @Schema(description = "客户来源(展会/阿里/官网/转介绍)")
    private String source;

    @Schema(description = "客户等级(A/B/C/D)")
    private String level;

    @Schema(description = "状态(1正常 0禁用)")
    private Integer status;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "负责人姓名")
    private String ownerName;

    @Schema(description = "所属部门")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "询盘总数")
    private Integer totalInquiry;

    @Schema(description = "累计订单金额")
    private BigDecimal totalOrder;

    @Schema(description = "最后下单日期")
    private LocalDate lastOrderDate;

    @Schema(description = "创建者")
    private Long createdBy;

    @Schema(description = "创建者姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "标签列表")
    private List<TagVO> tags;

    @Schema(description = "联系人数量")
    private Integer contactCount;

    @Schema(description = "最近跟进时间")
    private LocalDateTime lastFollowTime;

    @Schema(description = "天数未跟进")
    private Integer daysSinceLastFollow;
}
