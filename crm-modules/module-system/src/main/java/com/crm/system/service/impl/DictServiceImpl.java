package com.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.system.domain.entity.SysDictData;
import com.crm.system.domain.entity.SysDictType;
import com.crm.system.mapper.SysDictDataMapper;
import com.crm.system.mapper.SysDictTypeMapper;
import com.crm.system.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements DictService {

    private final SysDictDataMapper dictDataMapper;

    @Override
    public IPage<SysDictType> pageDictTypes(String dictName, String dictType, int pageNum, int pageSize) {
        Page<SysDictType> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<SysDictType>()
                .like(StrUtil.isNotBlank(dictName), SysDictType::getDictName, dictName)
                .eq(StrUtil.isNotBlank(dictType), SysDictType::getDictType, dictType)
                .orderByAsc(SysDictType::getCreatedTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public List<SysDictType> listAllDictTypes() {
        return this.list(new LambdaQueryWrapper<SysDictType>()
                .orderByAsc(SysDictType::getCreatedTime));
    }

    @Override
    public SysDictType getDictTypeById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDictType(SysDictType dictType) {
        // 检查字典类型是否唯一
        if (!checkDictTypeUnique(dictType.getDictType(), null)) {
            throw new RuntimeException("字典类型已存在");
        }

        // 设置默认值
        if (StrUtil.isBlank(dictType.getStatus())) {
            dictType.setStatus("0");
        }

        return this.save(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictType(SysDictType dictType) {
        // 检查字典类型是否唯一
        if (!checkDictTypeUnique(dictType.getDictType(), dictType.getId())) {
            throw new RuntimeException("字典类型已存在");
        }

        return this.updateById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictTypes(List<Long> ids) {
        // TODO: 删除字典类型时,同时删除对应的字典数据
        return this.removeByIds(ids);
    }

    @Override
    public boolean checkDictTypeUnique(String dictType, Long id) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType);
        
        if (id != null) {
            wrapper.ne(SysDictType::getId, id);
        }

        return this.count(wrapper) == 0;
    }

    @Override
    public List<SysDictData> getDictDataByType(String dictType) {
        return dictDataMapper.selectDictDataByType(dictType);
    }

    @Override
    public IPage<SysDictData> pageDictData(String dictType, String dictLabel, int pageNum, int pageSize) {
        Page<SysDictData> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<SysDictData>()
                .eq(StrUtil.isNotBlank(dictType), SysDictData::getDictType, dictType)
                .like(StrUtil.isNotBlank(dictLabel), SysDictData::getDictLabel, dictLabel)
                .orderByAsc(SysDictData::getDictSort);
        
        return dictDataMapper.selectPage(page, wrapper);
    }

    @Override
    public SysDictData getDictDataById(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDictData(SysDictData dictData) {
        // 设置默认值
        if (dictData.getDictSort() == null) {
            dictData.setDictSort(0);
        }
        if (StrUtil.isBlank(dictData.getStatus())) {
            dictData.setStatus("0");
        }
        if (StrUtil.isBlank(dictData.getIsDefault())) {
            dictData.setIsDefault("N");
        }

        return dictDataMapper.insert(dictData) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictData(SysDictData dictData) {
        return dictDataMapper.updateById(dictData) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictData(List<Long> ids) {
        return dictDataMapper.deleteBatchIds(ids) > 0;
    }
}
