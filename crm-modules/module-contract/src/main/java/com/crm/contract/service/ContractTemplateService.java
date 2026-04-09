package com.crm.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.contract.domain.dto.ContractTemplateCreateDTO;
import com.crm.contract.domain.dto.ContractTemplateQuery;
import com.crm.contract.domain.dto.ContractTemplateUpdateDTO;
import com.crm.contract.domain.entity.CrmContractTemplate;
import com.crm.contract.domain.vo.ContractTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * 合同模板Service接口
 *
 * @author CRM System
 * @since 2026-04-09
 */
public interface ContractTemplateService extends IService<CrmContractTemplate> {

    /**
     * 分页查询模板
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 模板分页列表
     */
    IPage<ContractTemplateVO> getTemplatePage(Page<ContractTemplateVO> page, ContractTemplateQuery query);

    /**
     * 根据ID查询模板详情
     *
     * @param id 模板ID
     * @return 模板详情
     */
    ContractTemplateVO getTemplateById(Long id);

    /**
     * 创建模板
     *
     * @param dto 创建DTO
     * @return 模板ID
     */
    Long createTemplate(ContractTemplateCreateDTO dto);

    /**
     * 更新模板
     *
     * @param dto 更新DTO
     */
    void updateTemplate(ContractTemplateUpdateDTO dto);

    /**
     * 删除模板
     *
     * @param ids 模板ID列表
     */
    void deleteTemplates(List<Long> ids);

    /**
     * 查询模板列表
     *
     * @param query 查询条件
     * @return 模板列表
     */
    List<ContractTemplateVO> listTemplates(ContractTemplateQuery query);

    /**
     * 获取默认模板
     *
     * @param contractType 合同类型
     * @param language 语言
     * @return 模板信息
     */
    CrmContractTemplate getDefaultTemplate(String contractType, String language);

    /**
     * 设置默认模板
     *
     * @param templateId 模板ID
     */
    void setDefaultTemplate(Long templateId);

    /**
     * 替换模板变量
     *
     * @param templateContent 模板内容
     * @param variables 变量映射
     * @return 替换后的内容
     */
    String replaceVariables(String templateContent, Map<String, Object> variables);

    /**
     * 检查模板编码唯一性
     *
     * @param templateCode 模板编码
     * @param excludeId 排除的ID
     * @return 是否唯一
     */
    boolean checkTemplateCodeUnique(String templateCode, Long excludeId);
}
