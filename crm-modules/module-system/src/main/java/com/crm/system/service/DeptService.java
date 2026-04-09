package com.crm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.system.domain.entity.SysDept;
import com.crm.system.domain.vo.DeptTreeVO;

import java.util.List;

/**
 * 部门服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface DeptService extends IService<SysDept> {

    /**
     * 获取部门树形结构
     *
     * @return 部门树
     */
    List<DeptTreeVO> getDeptTree();

    /**
     * 根据ID查询部门详情
     *
     * @param id 部门ID
     * @return 部门详情
     */
    SysDept getDeptById(Long id);

    /**
     * 创建部门
     *
     * @param dept 部门信息
     * @return 是否成功
     */
    boolean createDept(SysDept dept);

    /**
     * 更新部门
     *
     * @param dept 部门信息
     * @return 是否成功
     */
    boolean updateDept(SysDept dept);

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 是否成功
     */
    boolean deleteDept(Long id);

    /**
     * 检查部门是否存在子部门
     *
     * @param deptId 部门ID
     * @return 是否存在子部门
     */
    boolean hasChildren(Long deptId);

    /**
     * 检查部门下是否有用户
     *
     * @param deptId 部门ID
     * @return 是否有用户
     */
    boolean hasUsers(Long deptId);

    /**
     * 查询所有正常状态的部门列表
     *
     * @return 部门列表
     */
    List<SysDept> listNormalDepts();
}
