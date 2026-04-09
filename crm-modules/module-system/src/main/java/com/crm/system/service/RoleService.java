package com.crm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.system.domain.dto.RoleQuery;
import com.crm.system.domain.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface RoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 角色分页数据
     */
    IPage<SysRole> pageRoles(RoleQuery query, int pageNum, int pageSize);

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<SysRole> listAllRoles();

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    SysRole getRoleById(Long id);

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean createRole(SysRole role);

    /**
     * 更新角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(SysRole role);

    /**
     * 删除角色
     *
     * @param ids 角色ID列表
     * @return 是否成功
     */
    boolean deleteRoles(List<Long> ids);

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 状态(0正常 1停用)
     * @return 是否成功
     */
    boolean changeStatus(Long roleId, String status);

    /**
     * 为角色分配菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询角色的菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 设置角色数据权限
     *
     * @param roleId 角色ID
     * @param dataScope 数据范围
     * @param deptIds 部门ID列表(自定义数据权限时使用)
     * @return 是否成功
     */
    boolean setDataScope(Long roleId, String dataScope, List<Long> deptIds);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);

    /**
     * 检查角色权限字符串是否唯一
     *
     * @param roleKey 角色权限字符串
     * @param roleId 角色ID(更新时传入,新增时传null)
     * @return 是否唯一
     */
    boolean checkRoleKeyUnique(String roleKey, Long roleId);
}
