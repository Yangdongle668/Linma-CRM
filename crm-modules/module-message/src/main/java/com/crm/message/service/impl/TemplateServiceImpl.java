package com.crm.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.message.domain.dto.TemplateCreateDTO;
import com.crm.message.domain.entity.MsgTemplate;
import com.crm.message.mapper.MsgTemplateMapper;
import com.crm.message.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息模板管理服务实现类
 *
 * @author CRM System
 * @since 2026-04-09
 */
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl extends ServiceImpl<MsgTemplateMapper, MsgTemplate> implements TemplateService {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    @Override
    public Long createTemplate(TemplateCreateDTO dto) {
        MsgTemplate template = new MsgTemplate();
        template.setTemplateCode(dto.getTemplateCode());
        template.setTemplateName(dto.getTemplateName());
        template.setType(dto.getType());
        template.setTitle(dto.getTitle());
        template.setContent(dto.getContent());
        template.setEmailSubject(dto.getEmailSubject());
        template.setRemark(dto.getRemark());
        template.setStatus(1); // 默认启用

        save(template);
        return template.getId();
    }

    @Override
    public MsgTemplate getByCode(String templateCode) {
        LambdaQueryWrapper<MsgTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgTemplate::getTemplateCode, templateCode)
                .eq(MsgTemplate::getStatus, 1);
        return getOne(wrapper);
    }

    @Override
    public String renderTemplate(String templateCode, Map<String, Object> variables) {
        MsgTemplate template = getByCode(templateCode);
        if (template == null) {
            throw new IllegalArgumentException("模板不存在或已禁用: " + templateCode);
        }

        String content = template.getContent();

        // 替换变量占位符 ${variableName}
        Matcher matcher = VARIABLE_PATTERN.matcher(content);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);
            String replacement = value != null ? value.toString() : "";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    @Override
    public List<MsgTemplate> getActiveTemplates(String type) {
        LambdaQueryWrapper<MsgTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsgTemplate::getStatus, 1);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(MsgTemplate::getType, type);
        }
        wrapper.orderByDesc(MsgTemplate::getCreatedTime);
        return list(wrapper);
    }
}
