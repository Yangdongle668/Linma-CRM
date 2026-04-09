package com.crm.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.message.domain.dto.TemplateCreateDTO;
import com.crm.message.domain.entity.MsgTemplate;

import java.util.List;

/**
 * 消息模板管理服务接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface TemplateService extends IService<MsgTemplate> {

    /**
     * 创建模板
     *
     * @param dto 模板创建信息
     * @return 模板ID
     */
    Long createTemplate(TemplateCreateDTO dto);

    /**
     * 根据编码获取模板
     *
     * @param templateCode 模板编码
     * @return 模板对象
     */
    MsgTemplate getByCode(String templateCode);

    /**
     * 渲染模板内容
     * 将模板中的变量占位符替换为实际值
     *
     * @param templateCode 模板编码
     * @param variables 变量Map
     * @return 渲染后的内容
     */
    String renderTemplate(String templateCode, java.util.Map<String, Object> variables);

    /**
     * 获取启用的模板列表
     *
     * @param type 模板类型
     * @return 模板列表
     */
    List<MsgTemplate> getActiveTemplates(String type);
}
