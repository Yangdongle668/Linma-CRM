package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户查询条件DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户查询条件")
public class CustomerQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户编号")
    private String customerNo;

    @Schema(description = "公司名称(支持模糊搜索)")
    private String companyName;

    @Schema(description = "国家代码")
    private String country;

    @Schema(description = "省份/州")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "行业分类")
    private String industry;

    @Schema(description = "客户来源")
    private String source;

    @Schema(description = "客户等级(A/B/C/D)")
    private String level;

    @Schema(description = "状态(1正常 0禁用)")
    private Integer status;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "所属部门")
    private Long departmentId;

    @Schema(description = "标签ID列表")
    private java.util.List<Long> tagIds;

    @Schema(description = "是否有询盘")
    private Boolean hasInquiry;

    @Schema(description = "是否有订单")
    private Boolean hasOrder;

    @Schema(description = "开始创建时间")
    private String startTime;

    @Schema(description = "结束创建时间")
    private String endTime;

    @Schema(description = "关键字(搜索公司名、网站、备注)")
    private String keyword;
}
