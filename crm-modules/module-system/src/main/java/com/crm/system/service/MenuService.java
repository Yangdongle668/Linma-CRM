package com.crm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.system.domain.entity.SysMenu;
import com.crm.system.domain.vo.MenuTreeVO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface MenuService extends IService<SysMenu> {

    /**
     * 获取菜单树形结构
     *
     * @return 菜单树
     */
    List<MenuTreeVO> getMenuTree();

    /**
     * 根据用户ID获取菜单树
     *
     * @param userId 用户ID
     * @return 菜单树
     */
    List<MenuTreeVO> getMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID获取菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> getMenusByRoleId(Long roleId);

    /**
     * 根据用户ID获取菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getMenusByUserId(Long userId);

    /**
     * 根据ID查询菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    SysMenu getMenuById(Long id);

    /**
     * 创建菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    boolean createMenu(SysMenu menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    boolean updateMenu(SysMenu menu);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    boolean deleteMenu(Long id);

    /**
     * 检查菜单是否存在子菜单
     *
     * @param menuId 菜单ID
     * @return 是否存在子菜单
     */
    boolean hasChildren(Long menuId);

    /**
     * 检查菜单是否已分配给角色
     *
     * @param menuId 菜单ID
     * @return 是否已分配
     */
    boolean isAssignedToRole(Long menuId);
}
