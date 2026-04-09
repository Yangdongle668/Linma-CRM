package com.crm.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.system.domain.entity.SysMenu;
import com.crm.system.domain.vo.MenuTreeVO;
import com.crm.system.mapper.SysMenuMapper;
import com.crm.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements MenuService {

    @Override
    public List<MenuTreeVO> getMenuTree() {
        // 查询所有菜单
        List<SysMenu> allMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getOrderNum));

        // 构建树形结构
        return buildMenuTree(allMenus, 0L);
    }

    @Override
    public List<MenuTreeVO> getMenuTreeByUserId(Long userId) {
        // TODO: 根据用户ID查询菜单列表
        List<SysMenu> menus = this.list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getOrderNum));

        return buildMenuTree(menus, 0L);
    }

    @Override
    public List<SysMenu> getMenusByRoleId(Long roleId) {
        // TODO: 调用Mapper查询角色的菜单列表
        log.info("查询角色 {} 的菜单列表", roleId);
        return List.of();
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        // TODO: 调用Mapper查询用户的菜单列表
        log.info("查询用户 {} 的菜单列表", userId);
        return List.of();
    }

    @Override
    public SysMenu getMenuById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMenu(SysMenu menu) {
        // 设置默认值
        if (menu.getOrderNum() == null) {
            menu.setOrderNum(0);
        }
        if (menu.getVisible() == null) {
            menu.setVisible("0");
        }
        if (menu.getStatus() == null) {
            menu.setStatus("0");
        }

        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(SysMenu menu) {
        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Long id) {
        // 检查是否存在子菜单
        if (hasChildren(id)) {
            throw new RuntimeException("存在子菜单,不允许删除");
        }

        // 检查是否已分配给角色
        if (isAssignedToRole(id)) {
            throw new RuntimeException("菜单已分配给角色,不允许删除");
        }

        return this.removeById(id);
    }

    @Override
    public boolean hasChildren(Long menuId) {
        long count = this.count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, menuId));
        return count > 0;
    }

    @Override
    public boolean isAssignedToRole(Long menuId) {
        // TODO: 检查菜单是否已分配给角色
        log.info("检查菜单 {} 是否已分配给角色", menuId);
        return false;
    }

    /**
     * 构建菜单树形结构
     *
     * @param menus 菜单列表
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    private List<MenuTreeVO> buildMenuTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .sorted(Comparator.comparingInt(SysMenu::getOrderNum))
                .map(menu -> {
                    MenuTreeVO vo = new MenuTreeVO();
                    BeanUtil.copyProperties(menu, vo);
                    
                    // 递归获取子菜单
                    List<MenuTreeVO> children = buildMenuTree(menus, menu.getId());
                    if (!children.isEmpty()) {
                        vo.setChildren(children);
                    }
                    
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
