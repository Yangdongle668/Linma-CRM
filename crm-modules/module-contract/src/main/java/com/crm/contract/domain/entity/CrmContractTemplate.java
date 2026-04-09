package com.crm.contract.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合同模板实体类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crm_contract_template")
public class CrmContractTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 合同类型(sales/purchase/agency/nda)
     */
    private String contractType;

    /**
     * 模板内容(含变量占位符)
     */
    private String templateContent;

    /**
     * 语言(zh/en)
     */
    private String language;

    /**
     * 版本号
     */
    private String version;

    /**
     * 是否默认模板
     */
    private Boolean isDefault;

    /**
     * 状态(1启用 0禁用)
     */
    private Integer status;

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
}
