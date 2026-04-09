package com.crm.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.common.core.exception.BusinessException;
import com.crm.contract.domain.dto.ContractTemplateCreateDTO;
import com.crm.contract.domain.dto.ContractTemplateQuery;
import com.crm.contract.domain.dto.ContractTemplateUpdateDTO;
import com.crm.contract.domain.entity.CrmContractTemplate;
import com.crm.contract.domain.vo.ContractTemplateVO;
import com.crm.contract.mapper.ContractTemplateMapper;
import com.crm.contract.service.ContractTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 合同模板Service实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractTemplateServiceImpl extends ServiceImpl<ContractTemplateMapper, CrmContractTemplate> 
        implements ContractTemplateService {

    private final ContractTemplateMapper templateMapper;

    // 变量占位符正则表达式 {{variableName}}
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    @Override
    public IPage<ContractTemplateVO> getTemplatePage(Page<ContractTemplateVO> page, ContractTemplateQuery query) {
        return templateMapper.selectTemplatePage(page, query);
    }

    @Override
    public ContractTemplateVO getTemplateById(Long id) {
        CrmContractTemplate template = this.getById(id);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        ContractTemplateVO vo = BeanUtil.copyProperties(template, ContractTemplateVO.class);
        vo.setContractTypeDesc(getContractTypeDesc(template.getContractType()));
        vo.setLanguageDesc(getLanguageDesc(template.getLanguage()));
        vo.setStatusDesc(template.getStatus() == 1 ? "启用" : "禁用");
        
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTemplate(ContractTemplateCreateDTO dto) {
        // 检查模板编码唯一性
        if (StrUtil.isNotBlank(dto.getTemplateCode())) {
            if (!checkTemplateCodeUnique(dto.getTemplateCode(), null)) {
                throw new BusinessException("模板编码已存在");
            }
        }

        CrmContractTemplate template = BeanUtil.copyProperties(dto, CrmContractTemplate.class);
        
        // 如果设置为默认模板，先取消同类型的其他默认模板
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            cancelDefaultTemplate(template.getContractType(), template.getLanguage());
        }

        this.save(template);
        log.info("创建合同模板成功, ID: {}, 名称: {}", template.getId(), template.getTemplateName());
        return template.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(ContractTemplateUpdateDTO dto) {
        CrmContractTemplate existTemplate = this.getById(dto.getId());
        if (existTemplate == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查模板编码唯一性
        if (StrUtil.isNotBlank(dto.getTemplateCode())) {
            if (!checkTemplateCodeUnique(dto.getTemplateCode(), dto.getId())) {
                throw new BusinessException("模板编码已存在");
            }
        }

        CrmContractTemplate template = BeanUtil.copyProperties(dto, CrmContractTemplate.class);
        
        // 如果设置为默认模板，先取消同类型的其他默认模板
        if (Boolean.TRUE.equals(dto.getIsDefault()) && !Boolean.TRUE.equals(existTemplate.getIsDefault())) {
            cancelDefaultTemplate(template.getContractType(), template.getLanguage());
        }

        this.updateById(template);
        log.info("更新合同模板成功, ID: {}", template.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplates(List<Long> ids) {
        this.removeByIds(ids);
        log.info("删除合同模板成功, IDs: {}", ids);
    }

    @Override
    public List<ContractTemplateVO> listTemplates(ContractTemplateQuery query) {
        return templateMapper.selectTemplateList(query);
    }

    @Override
    public CrmContractTemplate getDefaultTemplate(String contractType, String language) {
        return templateMapper.selectDefaultTemplate(contractType, language);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultTemplate(Long templateId) {
        CrmContractTemplate template = this.getById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 取消同类型的其他默认模板
        cancelDefaultTemplate(template.getContractType(), template.getLanguage());

        // 设置当前模板为默认
        template.setIsDefault(true);
        this.updateById(template);
        log.info("设置默认模板成功, 模板ID: {}", templateId);
    }

    @Override
    public String replaceVariables(String templateContent, Map<String, Object> variables) {
        if (StrUtil.isBlank(templateContent)) {
            throw new BusinessException("模板内容不能为空");
        }

        if (variables == null || variables.isEmpty()) {
            return templateContent;
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);
            
            if (value != null) {
                matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
            } else {
                // 如果变量未找到，保留原占位符
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    @Override
    public boolean checkTemplateCodeUnique(String templateCode, Long excludeId) {
        LambdaQueryWrapper<CrmContractTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmContractTemplate::getTemplateCode, templateCode);
        if (excludeId != null) {
            wrapper.ne(CrmContractTemplate::getId, excludeId);
        }
        return this.count(wrapper) == 0;
    }

    /**
     * 取消同类型的其他默认模板
     *
     * @param contractType 合同类型
     * @param language 语言
     */
    private void cancelDefaultTemplate(String contractType, String language) {
        LambdaUpdateWrapper<CrmContractTemplate> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CrmContractTemplate::getContractType, contractType)
               .eq(CrmContractTemplate::getLanguage, language)
               .eq(CrmContractTemplate::getIsDefault, true)
               .set(CrmContractTemplate::getIsDefault, false);
        
        this.update(wrapper);
    }

    /**
     * 获取合同类型描述
     *
     * @param contractType 合同类型
     * @return 描述
     */
    private String getContractTypeDesc(String contractType) {
        if (contractType == null) {
            return "";
        }
        switch (contractType) {
            case "sales":
                return "销售合同";
            case "purchase":
                return "采购合同";
            case "agency":
                return "代理合同";
            case "nda":
                return "保密协议";
            default:
                return contractType;
        }
    }

    /**
     * 获取语言描述
     *
     * @param language 语言
     * @return 描述
     */
    private String getLanguageDesc(String language) {
        if (language == null) {
            return "";
        }
        switch (language) {
            case "zh":
                return "中文";
            case "en":
                return "英文";
            default:
                return language;
        }
    }
}
