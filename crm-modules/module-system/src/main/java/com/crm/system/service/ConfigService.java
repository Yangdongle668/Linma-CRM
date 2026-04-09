package com.crm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.system.domain.entity.SysConfig;

import java.util.List;

/**
 * 参数配置服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ConfigService extends IService<SysConfig> {

    /**
     * 分页查询参数配置列表
     *
     * @param configName 参数名称
     * @param configKey 参数键名
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 参数配置分页数据
     */
    IPage<SysConfig> pageConfigs(String configName, String configKey, int pageNum, int pageSize);

    /**
     * 查询所有参数配置列表
     *
     * @return 参数配置列表
     */
    List<SysConfig> listAllConfigs();

    /**
     * 根据ID查询参数配置详情
     *
     * @param id 参数配置ID
     * @return 参数配置详情
     */
    SysConfig getConfigById(Long id);

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 参数键名
     * @return 参数值
     */
    String getConfigValueByKey(String configKey);

    /**
     * 创建参数配置
     *
     * @param config 参数配置信息
     * @return 是否成功
     */
    boolean createConfig(SysConfig config);

    /**
     * 更新参数配置
     *
     * @param config 参数配置信息
     * @return 是否成功
     */
    boolean updateConfig(SysConfig config);

    /**
     * 删除参数配置
     *
     * @param ids 参数配置ID列表
     * @return 是否成功
     */
    boolean deleteConfigs(List<Long> ids);

    /**
     * 检查参数键名是否唯一
     *
     * @param configKey 参数键名
     * @param id 参数配置ID(更新时传入,新增时传null)
     * @return 是否唯一
     */
    boolean checkConfigKeyUnique(String configKey, Long id);

    /**
     * 刷新参数配置缓存
     *
     * @return 是否成功
     */
    boolean refreshCache();
}
