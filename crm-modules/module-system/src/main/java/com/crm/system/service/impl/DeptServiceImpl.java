package com.crm.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.system.domain.entity.SysDept;
import com.crm.system.domain.vo.DeptTreeVO;
import com.crm.system.mapper.SysDeptMapper;
import com.crm.system.service.DeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements DeptService {

    @Override
    public List<DeptTreeVO> getDeptTree() {
        // 查询所有部门
        List<SysDept> allDepts = this.list(new LambdaQueryWrapper<SysDept>()
                .orderByAsc(SysDept::getOrderNum));

        // 构建树形结构
        return buildDeptTree(allDepts, 0L);
    }

    @Override
    public SysDept getDeptById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDept(SysDept dept) {
        // 设置默认值
        if (dept.getOrderNum() == null) {
            dept.setOrderNum(0);
        }
        if (dept.getStatus() == null) {
            dept.setStatus("0");
        }

        // 设置祖级列表
        if (dept.getParentId() != null && dept.getParentId() > 0) {
            SysDept parent = this.getById(dept.getParentId());
            if (parent != null) {
                dept.setAncestors(parent.getAncestors() + "," + dept.getParentId());
            }
        } else {
            dept.setAncestors("0");
            dept.setParentId(0L);
        }

        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        SysDept oldDept = this.getById(dept.getId());
        if (oldDept == null) {
            throw new RuntimeException("部门不存在");
        }

        // 如果父部门发生变化,需要更新祖级列表
        if (!oldDept.getParentId().equals(dept.getParentId())) {
            // 检查是否将自己设置为子部门
            if (dept.getId().equals(dept.getParentId())) {
                throw new RuntimeException("不能将自己设置为父部门");
            }
            
            // 更新祖级列表
            if (dept.getParentId() != null && dept.getParentId() > 0) {
                SysDept parent = this.getById(dept.getParentId());
                if (parent != null) {
                    dept.setAncestors(parent.getAncestors() + "," + dept.getParentId());
                }
            } else {
                dept.setAncestors("0");
                dept.setParentId(0L);
            }

            // TODO: 递归更新所有子部门的祖级列表
        }

        return this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(Long id) {
        // 检查是否存在子部门
        if (hasChildren(id)) {
            throw new RuntimeException("存在子部门,不允许删除");
        }

        // 检查部门下是否有用户
        if (hasUsers(id)) {
            throw new RuntimeException("部门下存在用户,不允许删除");
        }

        return this.removeById(id);
    }

    @Override
    public boolean hasChildren(Long deptId) {
        long count = this.count(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, deptId));
        return count > 0;
    }

    @Override
    public boolean hasUsers(Long deptId) {
        // TODO: 检查部门下是否有用户
        log.info("检查部门 {} 下是否有用户", deptId);
        return false;
    }

    @Override
    public List<SysDept> listNormalDepts() {
        return this.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getStatus, "0")
                .orderByAsc(SysDept::getOrderNum));
    }

    /**
     * 构建部门树形结构
     *
     * @param depts 部门列表
     * @param parentId 父部门ID
     * @return 部门树
     */
    private List<DeptTreeVO> buildDeptTree(List<SysDept> depts, Long parentId) {
        return depts.stream()
                .filter(dept -> dept.getParentId().equals(parentId))
                .sorted(Comparator.comparingInt(SysDept::getOrderNum))
                .map(dept -> {
                    DeptTreeVO vo = new DeptTreeVO();
                    BeanUtil.copyProperties(dept, vo);
                    
                    // 递归获取子部门
                    List<DeptTreeVO> children = buildDeptTree(depts, dept.getId());
                    if (!children.isEmpty()) {
                        vo.setChildren(children);
                    }
                    
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
