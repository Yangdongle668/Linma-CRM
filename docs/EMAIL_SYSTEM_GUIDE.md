# Email System Documentation / 邮件系统文档

## Overview / 概述

The Linma-CRM email system provides a professional email client interface similar to NetEase Mail Master, with full SMTP/IMAP integration for sending and receiving emails.

Linma-CRM 邮件系统提供类似网易邮箱大师的专业邮件客户端界面，支持完整的 SMTP/IMAP 集成，用于发送和接收邮件。

---

## Features / 功能特性

### 1. Multi-Account Support / 多账户支持
- Add multiple email accounts (Gmail, Outlook, QQ Mail, etc.)
- Switch between accounts seamlessly
- Set default account for composing emails
- 支持添加多个邮箱账户（Gmail、Outlook、QQ邮箱等）
- 无缝切换账户
- 设置默认写信账户

### 2. Professional UI / 专业界面
- Three-pane layout (Folders | Email List | Reading Pane)
- Modern, clean design inspired by NetEase Mail Master
- Responsive and intuitive user experience
- 三栏布局（文件夹 | 邮件列表 | 阅读窗格）
- 受网易邮箱大师启发的现代简洁设计
- 响应式直观用户体验

### 3. Folder Management / 文件夹管理
- System folders: Inbox, Sent, Drafts, Trash, Spam, Archive
- Custom folders and labels/tags
- Unread count badges
- 系统文件夹：收件箱、已发送、草稿箱、垃圾箱、归档
- 自定义文件夹和标签
- 未读数量徽章

### 4. Email Operations / 邮件操作
- Compose with rich text editor
- Reply, Reply All, Forward
- Star/Unstar emails
- Mark as read/unread
- Delete and archive
- Attachment support
- 富文本编辑器写信
- 回复、回复全部、转发
- 星标/取消星标
- 标记已读/未读
- 删除和归档
- 附件支持

### 5. SMTP/IMAP Configuration / SMTP/IMAP 配置
- User-specific SMTP settings for sending
- IMAP integration for receiving emails
- SSL/TLS encryption support
- Connection testing before saving
- Auto-sync at configurable intervals
- 用户专属SMTP发件设置
- IMAP集成收件
- SSL/TLS加密支持
- 保存前连接测试
- 可配置间隔自动同步

---

## Configuration / 配置

### 1. Database Setup / 数据库设置

Run the migration script to create email-related tables:

```bash
mysql -u root -p crm_db < docs/database/email_settings_migration.sql
```

This creates:
- `sys_email_settings` - User email account configurations
- `msg_email_folder` - Email folders
- `msg_email_label` - Email labels/tags
- `msg_email_label_relation` - Email-label relationships
- Extended `msg_email_log` with additional fields

### 2. Application Properties / 应用配置

Default SMTP settings are in `crm-admin/src/main/resources/application.yml`:

```yaml
spring:
  mail:
    host: ${MAIL_HOST:smtp.exmail.qq.com}
    port: ${MAIL_PORT:465}
    username: ${MAIL_USERNAME:noreply@yourcompany.com}
    password: ${MAIL_PASSWORD:your-password}
    protocol: ${MAIL_PROTOCOL:smtp}
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true
```

**Note:** These are fallback defaults. Users configure their own SMTP/IMAP settings through the UI.

### 3. Environment Variables / 环境变量

For production deployment, set these environment variables:

```bash
export MAIL_HOST=smtp.yourcompany.com
export MAIL_PORT=465
export MAIL_USERNAME=noreply@yourcompany.com
export MAIL_PASSWORD=your-secure-password
```

---

## API Endpoints / API 接口

### Email Settings Management / 邮件设置管理

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/message/email-settings` | Save email settings |
| PUT | `/message/email-settings` | Update email settings |
| DELETE | `/message/email-settings/{id}` | Delete email settings |
| GET | `/message/email-settings/list` | Get user's email settings |
| GET | `/message/email-settings/default` | Get default email settings |
| GET | `/message/email-settings/{id}` | Get settings by ID |
| PUT | `/message/email-settings/set-default` | Set as default account |
| POST | `/message/email-settings/test-smtp` | Test SMTP connection |
| POST | `/message/email-settings/test-imap` | Test IMAP connection |
| POST | `/message/email-settings/sync/{settingsId}` | Sync emails manually |

### Email Operations / 邮件操作

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/message/email/send` | Send email |
| POST | `/message/email/send-async` | Send email asynchronously |
| POST | `/message/email/send-with-template` | Send with template |
| POST | `/message/email/batch-send` | Batch send emails |

---

## Usage Guide / 使用指南

### Adding an Email Account / 添加邮箱账户

1. Click the "+" icon next to "邮箱账户" in the sidebar
2. Fill in your email details:
   - **Email Address**: your@email.com
   - **Display Name**: Your Name
   - **SMTP Server**: e.g., smtp.qq.com
   - **SMTP Port**: 465 (SSL) or 587 (TLS)
   - **SMTP Username**: Usually your email address
   - **SMTP Password**: Use authorization code for QQ/Gmail
   - **IMAP Server**: e.g., imap.qq.com
   - **IMAP Port**: 993 (SSL) or 143 (TLS)
   - **IMAP Username**: Usually your email address
   - **IMAP Password**: Use authorization code
3. Click "测试连接" to verify settings
4. Click "保存" to save the account

### Common SMTP/IMAP Settings / 常用SMTP/IMAP设置

#### QQ Mail / QQ邮箱
```
SMTP: smtp.qq.com:465 (SSL)
IMAP: imap.qq.com:993 (SSL)
Note: Use authorization code from QQ Mail settings
```

#### Gmail
```
SMTP: smtp.gmail.com:465 (SSL) or 587 (TLS)
IMAP: imap.gmail.com:993 (SSL)
Note: Enable "Less secure app access" or use App Password
```

#### Outlook / Hotmail
```
SMTP: smtp-mail.outlook.com:587 (TLS)
IMAP: outlook.office365.com:993 (SSL)
```

#### 163 Mail / 163邮箱
```
SMTP: smtp.163.com:465 (SSL)
IMAP: imap.163.com:993 (SSL)
Note: Use authorization code
```

### Composing an Email / 写信

1. Click the "写信" button in the sidebar
2. Enter recipient(s) in "收件人" (comma-separated for multiple)
3. Optionally add CC recipients in "抄送"
4. Enter subject line
5. Compose your message in the rich text editor
6. Attach files if needed
7. Click "发送" to send or "存草稿" to save as draft

### Managing Emails / 管理邮件

- **Star**: Click the star icon to mark important emails
- **Read/Unread**: Double-click to read, right-click to mark as unread
- **Delete**: Select emails and click delete icon
- **Archive**: Move emails to archive folder
- **Reply**: Click reply button in reading pane
- **Forward**: Click forward button in reading pane

---

## Architecture / 架构

### Backend Structure / 后端结构

```
module-message/
├── controller/
│   ├── EmailController.java           # Email operations
│   ├── EmailSettingsController.java   # Settings management
│   └── TemplateController.java        # Template management
├── service/
│   ├── EmailService.java              # Email sending interface
│   ├── EmailSettingsService.java      # Settings interface
│   └── impl/
│       ├── EmailServiceImpl.java      # SMTP implementation
│       └── EmailSettingsServiceImpl.java  # Settings logic
├── mapper/
│   ├── MsgEmailLogMapper.java         # Email log mapper
│   └── SysEmailSettingsMapper.java    # Settings mapper
├── domain/
│   ├── entity/
│   │   ├── MsgEmailLog.java           # Email log entity
│   │   └── SysEmailSettings.java      # Settings entity
│   └── dto/
│       ├── EmailSendDTO.java          # Send email DTO
│       └── EmailSettingsDTO.java      # Settings DTO
└── resources/
    └── mapper/
        ├── MsgEmailLogMapper.xml
        └── SysEmailSettingsMapper.xml
```

### Frontend Structure / 前端结构

```
crm-frontend/src/
├── views/message/
│   ├── email-new.vue                  # New professional email client
│   ├── email.vue                      # Legacy email view
│   └── template.vue                   # Template management
└── api/
    └── message.ts                     # Email API functions
```

---

## Security Considerations / 安全考虑

1. **Password Storage**: SMTP/IMAP passwords should be encrypted before storing in database
2. **Authorization Codes**: For QQ/Gmail, use authorization codes instead of actual passwords
3. **SSL/TLS**: Always use encrypted connections (SSL/TLS)
4. **Rate Limiting**: Implement rate limiting to prevent email spam
5. **Validation**: Validate all email addresses before sending

### Encryption Implementation / 加密实现

Add password encryption in `EmailSettingsServiceImpl`:

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

// When saving
settings.setSmtpPassword(passwordEncoder.encode(dto.getSmtpPassword()));
settings.setImapPassword(passwordEncoder.encode(dto.getImapPassword()));

// When using
String decryptedPassword = passwordEncoder.matches(inputPassword, storedPassword);
```

---

## Future Enhancements / 未来增强

- [ ] Implement actual IMAP email synchronization
- [ ] Add email search across all folders
- [ ] Implement email filters/rules
- [ ] Add email templates library
- [ ] Support for HTML email templates
- [ ] Email scheduling (send later)
- [ ] Email tracking (read receipts)
- [ ] Bulk email operations
- [ ] Email export (PDF/EML)
- [ ] Integration with CRM customer records

---

## Troubleshooting / 故障排除

### SMTP Connection Failed / SMTP连接失败

1. Verify SMTP server address and port
2. Check if SSL/TLS setting is correct
3. Ensure username/password are correct
4. For QQ/Gmail, use authorization code, not login password
5. Check firewall settings

### IMAP Sync Issues / IMAP同步问题

1. Verify IMAP is enabled in your email provider settings
2. Check IMAP server address and port
3. Ensure proper encryption method (SSL/TLS)
4. Verify authorization code is valid

### Email Not Sending / 邮件无法发送

1. Check SMTP settings are configured
2. Verify sender email is authenticated
3. Check recipient email format
4. Review application logs for errors

---

## Support / 支持

For issues or questions:
- Check the application logs in `logs/` directory
- Review Swagger documentation at `http://localhost:8080/api/doc.html`
- Contact system administrator

---

**Last Updated**: 2026-04-12
**Version**: 1.0.0
