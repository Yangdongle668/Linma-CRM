package com.crm.message.service;

import com.crm.message.domain.dto.EmailSettingsDTO;
import com.crm.message.domain.entity.SysEmailSettings;

import java.util.List;

/**
 * 邮件设置服务接口
 *
 * @author CRM System
 * @since 2026-04-12
 */
public interface EmailSettingsService {

    /**
     * 保存邮件设置
     */
    Long saveSettings(Long userId, EmailSettingsDTO dto);

    /**
     * 更新邮件设置
     */
    void updateSettings(Long userId, EmailSettingsDTO dto);

    /**
     * 删除邮件设置
     */
    void deleteSettings(Long id);

    /**
     * 获取用户的邮件设置列表
     */
    List<SysEmailSettings> getUserSettings(Long userId);

    /**
     * 获取默认邮件设置
     */
    SysEmailSettings getDefaultSettings(Long userId);

    /**
     * 根据ID获取设置
     */
    SysEmailSettings getById(Long id);

    /**
     * 设置为默认账户
     */
    void setAsDefault(Long userId, Long settingsId);

    /**
     * 测试SMTP连接
     */
    boolean testSmtpConnection(EmailSettingsDTO dto);

    /**
     * 测试IMAP连接
     */
    boolean testImapConnection(EmailSettingsDTO dto);

    /**
     * 同步邮件
     */
    void syncEmails(Long settingsId);
}
