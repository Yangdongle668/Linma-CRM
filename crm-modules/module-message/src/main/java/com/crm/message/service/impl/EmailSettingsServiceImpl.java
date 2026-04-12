package com.crm.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.common.core.exception.BusinessException;
import com.crm.message.domain.dto.EmailSettingsDTO;
import com.crm.message.domain.entity.SysEmailSettings;
import com.crm.message.mapper.SysEmailSettingsMapper;
import com.crm.message.service.EmailSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.Properties;

/**
 * 邮件设置服务实现类
 *
 * @author CRM System
 * @since 2026-04-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSettingsServiceImpl implements EmailSettingsService {

    private final SysEmailSettingsMapper emailSettingsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveSettings(Long userId, EmailSettingsDTO dto) {
        log.info("保存邮件设置 - 用户ID:{}, 邮箱:{}", userId, dto.getEmailAddress());

        // 检查邮箱是否已存在
        SysEmailSettings existing = emailSettingsMapper.selectByEmailAddress(dto.getEmailAddress());
        if (existing != null) {
            throw new BusinessException("该邮箱地址已配置");
        }

        SysEmailSettings settings = new SysEmailSettings();
        BeanUtils.copyProperties(dto, settings);
        settings.setUserId(userId);

        // 如果是第一个账户，自动设为默认
        List<SysEmailSettings> userSettings = emailSettingsMapper.selectByUserId(userId);
        if (userSettings.isEmpty()) {
            settings.setIsDefault(true);
        }

        // 设置默认值
        if (settings.getSmtpPort() == null) {
            settings.setSmtpPort("ssl".equals(settings.getSmtpEncryption()) ? 465 : 587);
        }
        if (settings.getImapPort() == null) {
            settings.setImapPort("ssl".equals(settings.getImapEncryption()) ? 993 : 143);
        }
        if (settings.getEnabled() == null) {
            settings.setEnabled(true);
        }
        if (settings.getSyncInterval() == null) {
            settings.setSyncInterval(5);
        }
        if (settings.getKeepServerCopy() == null) {
            settings.setKeepServerCopy(true);
        }
        if (settings.getPageSize() == null) {
            settings.setPageSize(20);
        }

        emailSettingsMapper.insert(settings);
        log.info("邮件设置保存成功 - ID:{}", settings.getId());

        return settings.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSettings(Long userId, EmailSettingsDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("设置ID不能为空");
        }

        SysEmailSettings settings = emailSettingsMapper.selectById(dto.getId());
        if (settings == null || !settings.getUserId().equals(userId)) {
            throw new BusinessException("设置不存在或无权限");
        }

        BeanUtils.copyProperties(dto, settings);
        emailSettingsMapper.updateById(settings);
        log.info("邮件设置更新成功 - ID:{}", settings.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSettings(Long id) {
        emailSettingsMapper.deleteById(id);
        log.info("邮件设置删除成功 - ID:{}", id);
    }

    @Override
    public List<SysEmailSettings> getUserSettings(Long userId) {
        return emailSettingsMapper.selectByUserId(userId);
    }

    @Override
    public SysEmailSettings getDefaultSettings(Long userId) {
        return emailSettingsMapper.selectDefaultByUserId(userId);
    }

    @Override
    public SysEmailSettings getById(Long id) {
        return emailSettingsMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAsDefault(Long userId, Long settingsId) {
        // 取消所有默认
        List<SysEmailSettings> userSettings = emailSettingsMapper.selectByUserId(userId);
        for (SysEmailSettings setting : userSettings) {
            if (setting.getIsDefault()) {
                setting.setIsDefault(false);
                emailSettingsMapper.updateById(setting);
            }
        }

        // 设置新的默认
        SysEmailSettings settings = emailSettingsMapper.selectById(settingsId);
        if (settings == null || !settings.getUserId().equals(userId)) {
            throw new BusinessException("设置不存在或无权限");
        }
        settings.setIsDefault(true);
        emailSettingsMapper.updateById(settings);

        log.info("已设置默认邮箱 - ID:{}", settingsId);
    }

    @Override
    public boolean testSmtpConnection(EmailSettingsDTO dto) {
        log.info("测试SMTP连接 - 服务器:{}, 端口:{}", dto.getSmtpHost(), dto.getSmtpPort());

        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(dto.getSmtpHost());
            mailSender.setPort(dto.getSmtpPort());
            mailSender.setUsername(dto.getSmtpUsername());
            mailSender.setPassword(dto.getSmtpPassword());

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", dto.getSmtpAuth() != null ? dto.getSmtpAuth() : true);

            if ("ssl".equals(dto.getSmtpEncryption())) {
                props.put("mail.smtp.ssl.enable", true);
            } else if ("tls".equals(dto.getSmtpEncryption())) {
                props.put("mail.smtp.starttls.enable", true);
            }

            // 测试连接
            Transport transport = mailSender.getSession().getTransport();
            transport.connect(dto.getSmtpHost(), dto.getSmtpPort(),
                    dto.getSmtpUsername(), dto.getSmtpPassword());
            transport.close();

            log.info("SMTP连接测试成功");
            return true;

        } catch (Exception e) {
            log.error("SMTP连接测试失败: {}", e.getMessage());
            throw new BusinessException("SMTP连接失败: " + e.getMessage());
        }
    }

    @Override
    public boolean testImapConnection(EmailSettingsDTO dto) {
        log.info("测试IMAP连接 - 服务器:{}, 端口:{}", dto.getImapHost(), dto.getImapPort());

        try {
            Properties props = new Properties();

            if ("ssl".equals(dto.getImapEncryption())) {
                props.put("mail.imap.ssl.enable", "true");
            } else if ("tls".equals(dto.getImapEncryption())) {
                props.put("mail.imap.starttls.enable", "true");
            }

            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(dto.getImapHost(), dto.getImapPort(),
                    dto.getImapUsername(), dto.getImapPassword());

            // 尝试打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();
            inbox.close(false);
            store.close();

            log.info("IMAP连接测试成功 - 邮件数:{}", messageCount);
            return true;

        } catch (Exception e) {
            log.error("IMAP连接测试失败: {}", e.getMessage());
            throw new BusinessException("IMAP连接失败: " + e.getMessage());
        }
    }

    @Override
    public void syncEmails(Long settingsId) {
        log.info("开始同步邮件 - 设置ID:{}", settingsId);

        SysEmailSettings settings = emailSettingsMapper.selectById(settingsId);
        if (settings == null || !settings.getEnabled()) {
            throw new BusinessException("邮件设置不存在或未启用");
        }

        // TODO: 实现IMAP邮件同步逻辑
        // 这里应该：
        // 1. 连接IMAP服务器
        // 2. 获取新邮件
        // 3. 保存到数据库
        // 4. 更新最后同步时间

        log.info("邮件同步完成");
    }
}
