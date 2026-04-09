package com.crm.customer.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户更新DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "客户更新信息")
public class CustomerUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户ID不能为空")
    private Long id;

    @Schema(description = "公司名称(英文)")
    private String companyName;

    @Schema(description = "公司名称(中文)")
    private String companyNameCn;

    @Schema(description = "国家代码")
    private String country;

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

    @Schema(description = "所属部门")
    private Long departmentId;

    @Schema(description = "下次跟进时间")
    private String nextFollowTime;

    @Schema(description = "备注")
    private String remark;
}
