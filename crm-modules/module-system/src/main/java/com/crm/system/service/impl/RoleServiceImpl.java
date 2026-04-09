package com.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.system.domain.dto.RoleQuery;
import com.crm.system.domain.entity.SysRole;
import com.crm.system.mapper.SysRoleMapper;
import com.crm.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements RoleService {

    @Override
    public IPage<SysRole> pageRoles(RoleQuery query, int pageNum, int pageSize) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = buildQueryWrapper(query);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysRole> listAllRoles() {
        return this.list(new LambdaQueryWrapper<SysRole>()
                .orderByAsc(SysRole::getRoleSort));
    }

    @Override
    public SysRole getRoleById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(SysRole role) {
        // 检查角色权限字符串是否唯一
        if (!checkRoleKeyUnique(role.getRoleKey(), null)) {
            throw new RuntimeException("角色权限字符串已存在");
        }

        // 设置默认值
        if (StrUtil.isBlank(role.getStatus())) {
            role.setStatus("0");
        }
        if (role.getRoleSort() == null) {
            role.setRoleSort(0);
        }
        if (StrUtil.isBlank(role.getDataScope())) {
            role.setDataScope("1");
        }

        return this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysRole role) {
        // 检查角色权限字符串是否唯一
        if (!checkRoleKeyUnique(role.getRoleKey(), role.getId())) {
            throw new RuntimeException("角色权限字符串已存在");
        }

        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long roleId, String status) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        role.setStatus(status);
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        // TODO: 实现角色菜单分配逻辑
        // 1. 删除角色原有的菜单关联
        // 2. 插入新的菜单关联
        log.info("为角色 {} 分配菜单: {}", roleId, menuIds);
        return true;
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        // TODO: 查询角色的菜单ID列表
        log.info("查询角色 {} 的菜单ID列表", roleId);
        return List.of();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDataScope(Long roleId, String dataScope, List<Long> deptIds) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        role.setDataScope(dataScope);
        boolean result = this.updateById(role);

        if (result && "5".equals(dataScope) && deptIds != null) {
            // TODO: 保存自定义数据权限的部门关联
            log.info("为角色 {} 设置自定义数据权限,部门IDs: {}", roleId, deptIds);
        }

        return result;
    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        // TODO: 根据用户ID查询角色列表
        log.info("查询用户 {} 的角色列表", userId);
        return List.of();
    }

    @Override
    public boolean checkRoleKeyUnique(String roleKey, Long roleId) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, roleKey);
        
        if (roleId != null) {
            wrapper.ne(SysRole::getId, roleId);
        }

        return this.count(wrapper) == 0;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysRole> buildQueryWrapper(RoleQuery query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        if (query != null) {
            wrapper.like(StrUtil.isNotBlank(query.getRoleName()),
                            SysRole::getRoleName, query.getRoleName())
                   .eq(StrUtil.isNotBlank(query.getRoleKey()),
                            SysRole::getRoleKey, query.getRoleKey())
                   .eq(StrUtil.isNotBlank(query.getStatus()),
                            SysRole::getStatus, query.getStatus());
        }

        wrapper.orderByAsc(SysRole::getRoleSort);
        return wrapper;
    }
}
