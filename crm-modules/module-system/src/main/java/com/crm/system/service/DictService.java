package com.crm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.system.domain.entity.SysDictData;
import com.crm.system.domain.entity.SysDictType;

import java.util.List;

/**
 * 字典服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface DictService extends IService<SysDictType> {

    // ==================== 字典类型相关方法 ====================

    /**
     * 分页查询字典类型列表
     *
     * @param dictName 字典名称
     * @param dictType 字典类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 字典类型分页数据
     */
    IPage<SysDictType> pageDictTypes(String dictName, String dictType, int pageNum, int pageSize);

    /**
     * 查询所有字典类型列表
     *
     * @return 字典类型列表
     */
    List<SysDictType> listAllDictTypes();

    /**
     * 根据ID查询字典类型详情
     *
     * @param id 字典类型ID
     * @return 字典类型详情
     */
    SysDictType getDictTypeById(Long id);

    /**
     * 创建字典类型
     *
     * @param dictType 字典类型信息
     * @return 是否成功
     */
    boolean createDictType(SysDictType dictType);

    /**
     * 更新字典类型
     *
     * @param dictType 字典类型信息
     * @return 是否成功
     */
    boolean updateDictType(SysDictType dictType);

    /**
     * 删除字典类型
     *
     * @param ids 字典类型ID列表
     * @return 是否成功
     */
    boolean deleteDictTypes(List<Long> ids);

    /**
     * 检查字典类型是否唯一
     *
     * @param dictType 字典类型
     * @param id 字典类型ID(更新时传入,新增时传null)
     * @return 是否唯一
     */
    boolean checkDictTypeUnique(String dictType, Long id);

    // ==================== 字典数据相关方法 ====================

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> getDictDataByType(String dictType);

    /**
     * 分页查询字典数据列表
     *
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 字典数据分页数据
     */
    IPage<SysDictData> pageDictData(String dictType, String dictLabel, int pageNum, int pageSize);

    /**
     * 根据ID查询字典数据详情
     *
     * @param id 字典数据ID
     * @return 字典数据详情
     */
    SysDictData getDictDataById(Long id);

    /**
     * 创建字典数据
     *
     * @param dictData 字典数据信息
     * @return 是否成功
     */
    boolean createDictData(SysDictData dictData);

    /**
     * 更新字典数据
     *
     * @param dictData 字典数据信息
     * @return 是否成功
     */
    boolean updateDictData(SysDictData dictData);

    /**
     * 删除字典数据
     *
     * @param ids 字典数据ID列表
     * @return 是否成功
     */
    boolean deleteDictData(List<Long> ids);
}
