package com.crm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.system.domain.entity.SysConfig;
import com.crm.system.mapper.SysConfigMapper;
import com.crm.system.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 参数配置服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ConfigService {

    @Override
    public IPage<SysConfig> pageConfigs(String configName, String configKey, int pageNum, int pageSize) {
        Page<SysConfig> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<SysConfig>()
                .like(StrUtil.isNotBlank(configName), SysConfig::getConfigName, configName)
                .eq(StrUtil.isNotBlank(configKey), SysConfig::getConfigKey, configKey)
                .orderByAsc(SysConfig::getCreatedTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public List<SysConfig> listAllConfigs() {
        return this.list(new LambdaQueryWrapper<SysConfig>()
                .orderByAsc(SysConfig::getCreatedTime));
    }

    @Override
    public SysConfig getConfigById(Long id) {
        return this.getById(id);
    }

    @Override
    public String getConfigValueByKey(String configKey) {
        return this.getBaseMapper().selectConfigValueByKey(configKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createConfig(SysConfig config) {
        // 检查参数键名是否唯一
        if (!checkConfigKeyUnique(config.getConfigKey(), null)) {
            throw new RuntimeException("参数键名已存在");
        }

        // 设置默认值
        if (StrUtil.isBlank(config.getConfigType())) {
            config.setConfigType("N");
        }

        return this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(SysConfig config) {
        // 检查是否为系统内置参数
        SysConfig oldConfig = this.getById(config.getId());
        if (oldConfig != null && "Y".equals(oldConfig.getConfigType())) {
            throw new RuntimeException("系统内置参数不允许修改");
        }

        // 检查参数键名是否唯一
        if (!checkConfigKeyUnique(config.getConfigKey(), config.getId())) {
            throw new RuntimeException("参数键名已存在");
        }

        return this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigs(List<Long> ids) {
        // 检查是否包含系统内置参数
        for (Long id : ids) {
            SysConfig config = this.getById(id);
            if (config != null && "Y".equals(config.getConfigType())) {
                throw new RuntimeException("系统内置参数不允许删除");
            }
        }

        return this.removeByIds(ids);
    }

    @Override
    public boolean checkConfigKeyUnique(String configKey, Long id) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey);
        
        if (id != null) {
            wrapper.ne(SysConfig::getId, id);
        }

        return this.count(wrapper) == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refreshCache() {
        // TODO: 刷新参数配置缓存
        log.info("刷新参数配置缓存");
        return true;
    }
}
