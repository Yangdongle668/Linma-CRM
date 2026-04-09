package com.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.system.domain.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典数据Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> selectDictDataByType(String dictType);
}
