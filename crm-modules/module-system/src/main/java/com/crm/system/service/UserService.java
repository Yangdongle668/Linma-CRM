package com.crm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.system.domain.dto.UserCreateDTO;
import com.crm.system.domain.dto.UserQuery;
import com.crm.system.domain.dto.UserUpdateDTO;
import com.crm.system.domain.entity.SysUser;
import com.crm.system.domain.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface UserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param query 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户分页数据
     */
    IPage<UserVO> pageUsers(UserQuery query, int pageNum, int pageSize);

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getUserById(Long id);

    /**
     * 创建用户
     *
     * @param dto 用户创建信息
     * @return 是否成功
     */
    boolean createUser(UserCreateDTO dto);

    /**
     * 更新用户
     *
     * @param dto 用户更新信息
     * @return 是否成功
     */
    boolean updateUser(UserUpdateDTO dto);

    /**
     * 删除用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    boolean deleteUsers(List<Long> ids);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态(0正常 1停用)
     * @return 是否成功
     */
    boolean changeStatus(Long userId, String status);

    /**
     * 分配用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignRoles(Long userId, List<Long> roleIds);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 导出用户数据
     *
     * @param query 查询条件
     * @return 用户列表
     */
    List<UserVO> exportUsers(UserQuery query);

    /**
     * 导入用户数据
     *
     * @param userList 用户列表
     * @return 是否成功
     */
    boolean importUsers(List<UserCreateDTO> userList);
}
