package com.crm.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单树视图对象VO
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Data
@Schema(description = "菜单树视图对象")
public class MenuTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private Long id;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String menuName;

    /**
     * 菜单类型(M目录 C菜单 F按钮)
     */
    @Schema(description = "菜单类型")
    private String menuType;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    private String component;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String perms;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /**
     * 是否显示(0显示 1隐藏)
     */
    @Schema(description = "是否显示")
    private String visible;

    /**
     * 状态(0正常 1停用)
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 子菜单列表
     */
    @Schema(description = "子菜单列表")
    private List<MenuTreeVO> children;
}
