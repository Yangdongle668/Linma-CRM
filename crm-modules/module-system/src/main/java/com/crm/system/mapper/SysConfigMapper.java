package com.crm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crm.system.domain.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数配置Mapper接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 参数键名
     * @return 参数值
     */
    String selectConfigValueByKey(String configKey);
}
