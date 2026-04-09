package com.crm.customer.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户导入DTO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
public class CustomerImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "公司名称(英文)", index = 0)
    private String companyName;

    @ExcelProperty(value = "公司名称(中文)", index = 1)
    private String companyNameCn;

    @ExcelProperty(value = "国家", index = 2)
    private String country;

    @ExcelProperty(value = "省份/州", index = 3)
    private String province;

    @ExcelProperty(value = "城市", index = 4)
    private String city;

    @ExcelProperty(value = "详细地址", index = 5)
    private String address;

    @ExcelProperty(value = "公司网站", index = 6)
    private String website;

    @ExcelProperty(value = "行业分类", index = 7)
    private String industry;

    @ExcelProperty(value = "客户来源", index = 8)
    private String source;

    @ExcelProperty(value = "客户等级", index = 9)
    private String level;

    @ExcelProperty(value = "联系人姓名", index = 10)
    private String contactName;

    @ExcelProperty(value = "联系人职位", index = 11)
    private String position;

    @ExcelProperty(value = "联系人手机", index = 12)
    private String phone;

    @ExcelProperty(value = "联系人邮箱", index = 13)
    private String email;

    @ExcelProperty(value = "备注", index = 14)
    private String remark;
}
