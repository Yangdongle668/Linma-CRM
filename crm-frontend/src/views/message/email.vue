<template>
  <div class="email-client">
    <!-- Left Sidebar - Folders & Accounts -->
    <aside class="email-sidebar">
      <!-- Compose Button -->
      <div class="compose-section">
        <el-button type="primary" size="large" icon="Edit" @click="handleCompose" class="compose-btn">
          写信
        </el-button>
      </div>

      <!-- Email Accounts -->
      <div class="accounts-section">
        <div class="section-header">
          <span>邮箱账户</span>
          <el-icon class="add-account" @click="showAddAccountDialog"><Plus /></el-icon>
        </div>
        <div class="account-list" v-if="emailAccounts.length > 0">
          <div
            v-for="account in emailAccounts"
            :key="account.id"
            class="account-item"
            :class="{ active: selectedAccount?.id === account.id }"
            @click="selectAccount(account)"
          >
            <el-avatar :size="32" class="account-avatar">
              {{ account.emailAddress?.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="account-info">
              <div class="account-name">{{ account.displayName || account.emailAddress }}</div>
              <div class="account-email">{{ account.emailAddress }}</div>
            </div>
            <div class="account-actions">
              <el-icon v-if="account.isDefault" color="#409EFF" title="默认账户"><StarFilled /></el-icon>
              <el-icon class="edit-icon" @click.stop="editAccount(account)" title="编辑"><Edit /></el-icon>
            </div>
          </div>
        </div>
        <div v-else class="no-accounts">
          <el-empty description="暂无邮箱账户" :image-size="60">
            <el-button type="primary" size="small" @click="showAddAccountDialog">添加账户</el-button>
          </el-empty>
        </div>
      </div>

      <!-- Folders -->
      <div class="folders-section">
        <div class="section-header">
          <span>文件夹</span>
        </div>
        <el-menu
          :default-active="currentFolder"
          class="folder-menu"
          @select="handleFolderSelect"
        >
          <el-menu-item index="inbox">
            <el-icon><Message /></el-icon>
            <span>收件箱</span>
            <el-badge v-if="unreadCount > 0" :value="unreadCount" class="folder-badge" />
          </el-menu-item>
          <el-menu-item index="starred">
            <el-icon><Star /></el-icon>
            <span>星标邮件</span>
          </el-menu-item>
          <el-menu-item index="sent">
            <el-icon><Promotion /></el-icon>
            <span>已发送</span>
          </el-menu-item>
          <el-menu-item index="drafts">
            <el-icon><Document /></el-icon>
            <span>草稿箱</span>
            <el-badge v-if="draftCount > 0" :value="draftCount" class="folder-badge" />
          </el-menu-item>
          <el-menu-item index="archive">
            <el-icon><Box /></el-icon>
            <span>归档</span>
          </el-menu-item>
          <el-menu-item index="spam">
            <el-icon><WarnTriangleFilled /></el-icon>
            <span>垃圾邮件</span>
          </el-menu-item>
          <el-menu-item index="trash">
            <el-icon><Delete /></el-icon>
            <span>已删除</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- Tags/Labels -->
      <div class="tags-section">
        <div class="section-header">
          <span>标签</span>
          <el-icon @click="handleCreateTag"><Plus /></el-icon>
        </div>
        <div class="tag-list">
          <div v-for="tag in tags" :key="tag.id" class="tag-item">
            <div class="tag-color" :style="{ backgroundColor: tag.color }"></div>
            <span>{{ tag.name }}</span>
          </div>
        </div>
      </div>
    </aside>

    <!-- Main Content Area -->
    <main class="email-main">
      <!-- Toolbar -->
      <div class="email-toolbar">
        <div class="toolbar-left">
          <el-checkbox v-model="selectAll" @change="handleSelectAll" />
          <el-button icon="Refresh" circle @click="refreshEmails" :loading="refreshing" />
          <el-dropdown trigger="click" @command="handleMoreAction">
            <el-button icon="More" circle />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="markRead">标记为已读</el-dropdown-item>
                <el-dropdown-item command="markUnread">标记为未读</el-dropdown-item>
                <el-dropdown-item command="move">移动到</el-dropdown-item>
                <el-dropdown-item command="label">添加标签</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="toolbar-right">
          <el-input
            v-model="searchQuery"
            placeholder="搜索邮件..."
            prefix-icon="Search"
            clearable
            style="width: 300px"
            @input="handleSearch"
          />
          <el-button icon="ArrowDown" @click="handleSort">排序</el-button>
        </div>
      </div>

      <!-- Email List -->
      <div class="email-list-container" v-loading="loading">
        <el-table
          :data="emailList"
          stripe
          @selection-change="handleSelectionChange"
          @row-click="handleEmailClick"
          class="email-table"
        >
          <el-table-column type="selection" width="40" />
          <el-table-column label="" width="30">
            <template #default="{ row }">
              <el-icon v-if="row.starred" color="#F5A623"><StarFilled /></el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="from" label="发件人" width="180" show-overflow-tooltip>
            <template #default="{ row }">
              <div class="sender-cell">
                <el-avatar :size="28" class="sender-avatar">
                  {{ row.fromName?.charAt(0) || row.from?.charAt(0) }}
                </el-avatar>
                <span class="sender-name">{{ row.fromName || row.from }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="subject" label="主题" min-width="300" show-overflow-tooltip>
            <template #default="{ row }">
              <div class="subject-cell">
                <el-tag v-if="!row.read" type="danger" size="small" class="unread-tag">新</el-tag>
                <span :class="{ 'unread-text': !row.read }">{{ row.subject }}</span>
                <span class="email-preview">- {{ row.preview }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="附件" width="50">
            <template #default="{ row }">
              <el-icon v-if="row.hasAttachment" color="#909399"><Paperclip /></el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="date" label="时间" width="120" sortable>
            <template #default="{ row }">
              <span class="email-date">{{ formatDate(row.date) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </main>

    <!-- Reading Pane (Right Side) -->
    <aside class="reading-pane" v-if="selectedEmail">
      <div class="reading-header">
        <div class="reading-actions">
          <el-button icon="ArrowLeft" circle @click="selectedEmail = null" />
          <el-button icon="ArrowDown" circle title="归档" />
          <el-button icon="Delete" circle title="删除" @click="handleDeleteEmail(selectedEmail)" />
          <el-button icon="Star" circle :type="selectedEmail.starred ? 'warning' : ''" @click="toggleStar(selectedEmail)" />
          <el-button icon="Back" circle title="标记为未读" />
          <el-dropdown trigger="click">
            <el-button icon="More" circle />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>打印</el-dropdown-item>
                <el-dropdown-item>显示原始邮件</el-dropdown-item>
                <el-dropdown-item>过滤此发件人</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <div class="reading-content">
        <h2 class="email-subject">{{ selectedEmail.subject }}</h2>

        <div class="email-meta">
          <el-avatar :size="40" class="meta-avatar">
            {{ selectedEmail.fromName?.charAt(0) || selectedEmail.from?.charAt(0) }}
          </el-avatar>
          <div class="meta-info">
            <div class="meta-from">
              <strong>{{ selectedEmail.fromName || selectedEmail.from }}</strong>
              <span class="meta-email">&lt;{{ selectedEmail.from }}&gt;</span>
            </div>
            <div class="meta-to">
              收件人：<span>{{ selectedEmail.to }}</span>
            </div>
          </div>
          <div class="meta-time">{{ formatFullDate(selectedEmail.date) }}</div>
        </div>

        <el-divider />

        <div class="email-body" v-html="selectedEmail.content"></div>

        <!-- Attachments -->
        <div class="attachments-section" v-if="selectedEmail.attachments?.length > 0">
          <el-divider />
          <div class="attachments-header">
            <el-icon><Paperclip /></el-icon>
            <span>{{ selectedEmail.attachments.length }} 个附件</span>
          </div>
          <div class="attachments-list">
            <div
              v-for="attachment in selectedEmail.attachments"
              :key="attachment.id"
              class="attachment-item"
            >
              <el-icon :size="32" color="#409EFF"><Document /></el-icon>
              <div class="attachment-info">
                <div class="attachment-name">{{ attachment.name }}</div>
                <div class="attachment-size">{{ formatFileSize(attachment.size) }}</div>
              </div>
              <el-button size="small" icon="Download">下载</el-button>
            </div>
          </div>
        </div>

        <!-- Reply Actions -->
        <div class="reply-actions">
          <el-button type="primary" icon="ChatDotRound" @click="handleReply(selectedEmail)">
            回复
          </el-button>
          <el-button icon="ChatLineRound" @click="handleReplyAll(selectedEmail)">
            回复全部
          </el-button>
          <el-button icon="Forward" @click="handleForward(selectedEmail)">
            转发
          </el-button>
        </div>
      </div>
    </aside>

    <!-- Compose Dialog -->
    <el-dialog
      v-model="composeVisible"
      title="写信"
      width="900px"
      :close-on-click-modal="false"
      class="compose-dialog"
    >
      <el-form :model="composeForm" label-width="80px" class="compose-form">
        <el-form-item label="收件人">
          <el-input
            v-model="composeForm.to"
            placeholder="输入收件人邮箱，多个用逗号分隔"
            clearable
          />
        </el-form-item>
        <el-form-item label="抄送">
          <el-input
            v-model="composeForm.cc"
            placeholder="输入抄送邮箱"
            clearable
          />
        </el-form-item>
        <el-form-item label="主题">
          <el-input
            v-model="composeForm.subject"
            placeholder="输入邮件主题"
            clearable
          />
        </el-form-item>
        <el-form-item label="正文">
          <div class="editor-toolbar">
            <el-button-group>
              <el-button size="small" icon="Bold" @click="formatText('bold')" />
              <el-button size="small" icon="Italic" @click="formatText('italic')" />
              <el-button size="small" icon="Underline" @click="formatText('underline')" />
            </el-button-group>
            <el-button-group>
              <el-button size="small" icon="JustifyLeft" @click="formatText('justifyLeft')" />
              <el-button size="small" icon="JustifyCenter" @click="formatText('justifyCenter')" />
              <el-button size="small" icon="JustifyRight" @click="formatText('justifyRight')" />
            </el-button-group>
            <el-button-group>
              <el-button size="small" icon="Link" @click="insertLink" />
              <el-button size="small" icon="Picture" @click="insertImage" />
            </el-button-group>
          </div>
          <el-input
            v-model="composeForm.content"
            type="textarea"
            :rows="12"
            placeholder="输入邮件内容..."
            class="compose-textarea"
          />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            action="#"
            multiple
            :auto-upload="false"
            :on-change="handleAttachmentChange"
            :file-list="composeForm.attachments"
          >
            <el-button size="small" icon="Upload">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="saveDraft">存草稿</el-button>
          <el-button @click="composeVisible = false">取消</el-button>
          <el-button type="primary" @click="sendEmail" :loading="sending">
            发送
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Add Account Dialog -->
    <el-dialog
      v-model="addAccountVisible"
      title="添加邮箱账户"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="accountFormRef"
        :model="accountForm"
        :rules="accountRules"
        label-width="120px"
      >
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="邮箱地址" prop="emailAddress">
          <el-input v-model="accountForm.emailAddress" placeholder="example@qq.com" />
        </el-form-item>
        <el-form-item label="显示名称" prop="displayName">
          <el-input v-model="accountForm.displayName" placeholder="您的姓名" />
        </el-form-item>

        <el-divider content-position="left">SMTP设置（发件）</el-divider>
        <el-form-item label="SMTP服务器" prop="smtpHost">
          <el-input v-model="accountForm.smtpHost" placeholder="smtp.qq.com" />
        </el-form-item>
        <el-form-item label="SMTP端口" prop="smtpPort">
          <el-input-number v-model="accountForm.smtpPort" :min="1" :max="65535" />
        </el-form-item>
        <el-form-item label="加密方式">
          <el-radio-group v-model="accountForm.smtpEncryption">
            <el-radio label="ssl">SSL</el-radio>
            <el-radio label="tls">TLS</el-radio>
            <el-radio label="none">无</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户名" prop="smtpUsername">
          <el-input v-model="accountForm.smtpUsername" placeholder="邮箱地址或用户名" />
        </el-form-item>
        <el-form-item label="密码/授权码" prop="smtpPassword">
          <el-input
            v-model="accountForm.smtpPassword"
            type="password"
            show-password
            placeholder="输入密码或授权码"
          />
        </el-form-item>

        <el-divider content-position="left">IMAP设置（收件）</el-divider>
        <el-form-item label="使用与SMTP相同的配置">
          <el-switch v-model="useSameAsSmtp" @change="handleUseSameAsSmtp" />
          <span class="form-tip">开启后将自动使用SMTP的配置作为IMAP配置</span>
        </el-form-item>
        <el-form-item label="IMAP服务器" prop="imapHost">
          <el-input v-model="accountForm.imapHost" placeholder="imap.qq.com" :disabled="useSameAsSmtp" />
        </el-form-item>
        <el-form-item label="IMAP端口" prop="imapPort">
          <el-input-number v-model="accountForm.imapPort" :min="1" :max="65535" :disabled="useSameAsSmtp" />
        </el-form-item>
        <el-form-item label="加密方式">
          <el-radio-group v-model="accountForm.imapEncryption" :disabled="useSameAsSmtp">
            <el-radio label="ssl">SSL</el-radio>
            <el-radio label="tls">TLS</el-radio>
            <el-radio label="none">无</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户名" prop="imapUsername">
          <el-input v-model="accountForm.imapUsername" placeholder="邮箱地址或用户名" :disabled="useSameAsSmtp" />
        </el-form-item>
        <el-form-item label="密码/授权码" prop="imapPassword">
          <el-input
            v-model="accountForm.imapPassword"
            type="password"
            show-password
            placeholder="输入密码或授权码"
            :disabled="useSameAsSmtp"
          />
        </el-form-item>

        <el-divider content-position="left">其他设置</el-divider>
        <el-form-item label="同步间隔">
          <el-select v-model="accountForm.syncInterval">
            <el-option label="手动" :value="0" />
            <el-option label="每1分钟" :value="1" />
            <el-option label="每5分钟" :value="5" />
            <el-option label="每15分钟" :value="15" />
            <el-option label="每30分钟" :value="30" />
          </el-select>
        </el-form-item>
        <el-form-item label="保留服务器副本">
          <el-switch v-model="accountForm.keepServerCopy" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="testConnection">测试连接</el-button>
          <el-button @click="addAccountVisible = false">取消</el-button>
          <el-button type="primary" @click="saveAccount" :loading="savingAccount">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { sendEmail, getEmailSettingsList, saveEmailSettings, updateEmailSettings, deleteEmailSettings, setEmailAsDefault, testSmtpConnection, testImapConnection, getInboxEmails, getSentEmails, markEmailAsRead, toggleEmailStar, deleteEmail, type EmailSettings, type EmailSettingsDTO } from '@/api/message'
import dayjs from 'dayjs'

// State
const loading = ref(false)
const refreshing = ref(false)
const sending = ref(false)
const savingAccount = ref(false)
const selectAll = ref(false)
const currentFolder = ref('inbox')
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const unreadCount = ref(5)
const draftCount = ref(2)
const selectedEmail = ref<any>(null)
const selectedEmails = ref<any[]>([])
const composeVisible = ref(false)
const addAccountVisible = ref(false)

// Email accounts
const emailAccounts = ref<EmailSettings[]>([])
const selectedAccount = ref<EmailSettings | null>(null)
const currentUserId = ref(1) // TODO: 从登录上下文获取用户ID

// Tags
const tags = ref([
  { id: 1, name: '重要', color: '#F56C6C' },
  { id: 2, name: '工作', color: '#409EFF' },
  { id: 3, name: '个人', color: '#67C23A' },
])

// Email list - loaded from backend
const emailList = ref<any[]>([])

// Compose form
const composeForm = reactive({
  to: '',
  cc: '',
  subject: '',
  content: '',
  attachments: [] as any[],
})

// Account form
const accountFormRef = ref<FormInstance>()
const useSameAsSmtp = ref(false)
const accountForm = reactive({
  id: undefined as number | undefined,
  emailAddress: '',
  displayName: '',
  smtpHost: '',
  smtpPort: 465,
  smtpUsername: '',
  smtpPassword: '',
  smtpEncryption: 'ssl',
  imapHost: '',
  imapPort: 993,
  imapUsername: '',
  imapPassword: '',
  imapEncryption: 'ssl',
  syncInterval: 5,
  keepServerCopy: true,
})

function handleUseSameAsSmtp(value: boolean) {
  if (value) {
    // Copy SMTP settings to IMAP
    accountForm.imapHost = accountForm.smtpHost.replace('smtp', 'imap')
    accountForm.imapPort = 993
    accountForm.imapUsername = accountForm.smtpUsername
    accountForm.imapPassword = accountForm.smtpPassword
    accountForm.imapEncryption = accountForm.smtpEncryption
  }
}

const accountRules: FormRules = {
  emailAddress: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' },
  ],
  smtpHost: [{ required: true, message: '请输入SMTP服务器', trigger: 'blur' }],
  smtpUsername: [{ required: true, message: '请输入SMTP用户名', trigger: 'blur' }],
  smtpPassword: [{ required: true, message: '请输入SMTP密码', trigger: 'blur' }],
}

// Methods
function handleCompose() {
  composeVisible.value = true
  Object.assign(composeForm, {
    to: '',
    cc: '',
    subject: '',
    content: '',
    attachments: [],
  })
}

function selectAccount(account: any) {
  selectedAccount.value = account
  loadEmails()
}

function handleFolderSelect(folder: string) {
  currentFolder.value = folder
  loadEmails()
}

function handleSelectionChange(selection: any[]) {
  selectedEmails.value = selection
}

function handleSelectAll(val: boolean) {
  // Handle select all logic
}

async function handleEmailClick(row: any) {
  selectedEmail.value = row
  if (!row.read) {
    try {
      await markEmailAsRead(row.id, currentUserId.value)
      row.read = true
      unreadCount.value--
    } catch (error) {
      console.error('Failed to mark email as read:', error)
    }
  }
}

function refreshEmails() {
  refreshing.value = true
  setTimeout(() => {
    loadEmails()
    refreshing.value = false
    ElMessage.success('刷新成功')
  }, 1000)
}

async function loadEmails() {
  if (!selectedAccount.value) {
    console.log('No email account selected')
    return
  }

  loading.value = true
  try {
    const userId = currentUserId.value
    
    if (currentFolder.value === 'sent') {
      // Load sent emails
      const emails = await getSentEmails(userId, currentPage.value, pageSize.value)
      emailList.value = emails || []
      total.value = emails?.length || 0
    } else {
      // Load inbox emails (inbox, starred, archive, spam, trash)
      const folder = currentFolder.value === 'starred' ? 'inbox' : 
                     currentFolder.value === 'drafts' ? 'drafts' :
                     currentFolder.value
      const emails = await getInboxEmails(userId, folder, currentPage.value, pageSize.value)
      emailList.value = emails || []
      total.value = emails?.length || 0
      
      // If starred folder, filter only starred emails
      if (currentFolder.value === 'starred') {
        emailList.value = emailList.value.filter((email: any) => email.starred)
        total.value = emailList.value.length
      }
    }
    
    console.log(`Loaded ${emailList.value.length} emails from ${currentFolder.value}`)
  } catch (error) {
    console.error('Failed to load emails:', error)
    ElMessage.error('加载邮件失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  // Implement search logic
}

function handleSort() {
  // Implement sort logic
}

function handleMoreAction(command: string) {
  switch (command) {
    case 'markRead':
      ElMessage.success('已标记为已读')
      break
    case 'markUnread':
      ElMessage.success('已标记为未读')
      break
    case 'delete':
      handleDeleteSelected()
      break
  }
}

async function handleDeleteEmail(email: any) {
  ElMessageBox.confirm('确定要删除这封邮件吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteEmail(email.id, currentUserId.value)
      ElMessage.success('已删除')
      selectedEmail.value = null
      loadEmails() // Reload the list
    } catch (error) {
      console.error('Failed to delete email:', error)
      ElMessage.error('删除失败')
    }
  })
}

function handleDeleteSelected() {
  if (selectedEmails.value.length === 0) {
    ElMessage.warning('请先选择邮件')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedEmails.value.length} 封邮件吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    ElMessage.success('已删除')
    selectedEmails.value = []
  })
}

async function toggleStar(email: any) {
  try {
    await toggleEmailStar(email.id, currentUserId.value)
    email.starred = !email.starred
    ElMessage.success(email.starred ? '已添加星标' : '已取消星标')
  } catch (error) {
    console.error('Failed to toggle star:', error)
    ElMessage.error('操作失败')
  }
}

function handleReply(email: any) {
  composeVisible.value = true
  composeForm.to = email.from
  composeForm.subject = `Re: ${email.subject}`
  composeForm.content = `\n\n--- Original Message ---\nFrom: ${email.from}\nDate: ${email.date}\n\n${email.content}`
}

function handleReplyAll(email: any) {
  handleReply(email)
}

function handleForward(email: any) {
  composeVisible.value = true
  composeForm.subject = `Fwd: ${email.subject}`
  composeForm.content = `\n\n--- Forwarded Message ---\nFrom: ${email.from}\nDate: ${email.date}\n\n${email.content}`
}

function formatText(command: string) {
  // Implement rich text formatting
  ElMessage.info(`格式化: ${command}`)
}

function insertLink() {
  ElMessage.info('插入链接')
}

function insertImage() {
  ElMessage.info('插入图片')
}

function handleAttachmentChange(file: any, fileList: any[]) {
  composeForm.attachments = fileList
}

async function sendEmail() {
  if (!composeForm.to || !composeForm.subject || !composeForm.content) {
    ElMessage.warning('请填写完整的邮件信息')
    return
  }

  sending.value = true
  try {
    await sendEmail({
      to: composeForm.to,
      cc: composeForm.cc,
      subject: composeForm.subject,
      content: composeForm.content,
    })
    ElMessage.success('邮件发送成功')
    composeVisible.value = false
  } catch (error) {
    ElMessage.error('邮件发送失败')
  } finally {
    sending.value = false
  }
}

function saveDraft() {
  ElMessage.success('已保存到草稿箱')
  composeVisible.value = false
}

// Load email accounts from backend
async function loadEmailAccounts() {
  try {
    console.log('Loading email accounts for user:', currentUserId.value)
    const accounts = await getEmailSettingsList(currentUserId.value)
    console.log('Email accounts response:', accounts)
    
    // Axios interceptor already returns res.data, so accounts is the array directly
    if (accounts && Array.isArray(accounts)) {
      emailAccounts.value = accounts
      console.log('Loaded email accounts:', emailAccounts.value)
      
      // Select default account
      const defaultAccount = emailAccounts.value.find(acc => acc.isDefault || acc.default)
      if (defaultAccount) {
        selectedAccount.value = defaultAccount
        console.log('Selected default account:', defaultAccount.emailAddress)
      } else if (emailAccounts.value.length > 0) {
        selectedAccount.value = emailAccounts.value[0]
        console.log('Selected first account:', emailAccounts.value[0].emailAddress)
      }
    } else {
      console.warn('No email accounts found')
      emailAccounts.value = []
    }
  } catch (error) {
    console.error('Failed to load email accounts:', error)
    ElMessage.warning('加载邮箱账户失败')
    emailAccounts.value = []
  }
}

function editAccount(account: EmailSettings) {
  // Populate form with existing account data
  Object.assign(accountForm, {
    id: account.id,
    emailAddress: account.emailAddress,
    displayName: account.displayName || '',
    smtpHost: account.smtpHost,
    smtpPort: account.smtpPort,
    smtpUsername: account.smtpUsername,
    smtpPassword: account.smtpPassword,
    smtpEncryption: account.smtpEncryption || 'ssl',
    imapHost: account.imapHost,
    imapPort: account.imapPort,
    imapUsername: account.imapUsername,
    imapPassword: account.imapPassword,
    imapEncryption: account.imapEncryption || 'ssl',
    syncInterval: account.syncInterval || 5,
    keepServerCopy: account.keepServerCopy !== false,
  })
  addAccountVisible.value = true
}

function showAddAccountDialog() {
  addAccountVisible.value = true
  // Reset form
  Object.assign(accountForm, {
    id: undefined,
    emailAddress: '',
    displayName: '',
    smtpHost: '',
    smtpPort: 465,
    smtpUsername: '',
    smtpPassword: '',
    smtpEncryption: 'ssl',
    imapHost: '',
    imapPort: 993,
    imapUsername: '',
    imapPassword: '',
    imapEncryption: 'ssl',
    syncInterval: 5,
    keepServerCopy: true,
  })
}

async function testConnection() {
  if (!accountForm.smtpHost || !accountForm.imapHost) {
    ElMessage.warning('请先填写SMTP和IMAP服务器信息')
    return
  }

  try {
    const settings: EmailSettingsDTO = {
      emailAddress: accountForm.emailAddress,
      smtpHost: accountForm.smtpHost,
      smtpPort: accountForm.smtpPort,
      smtpUsername: accountForm.smtpUsername,
      smtpPassword: accountForm.smtpPassword,
      smtpEncryption: accountForm.smtpEncryption,
      imapHost: accountForm.imapHost,
      imapPort: accountForm.imapPort,
      imapUsername: accountForm.imapUsername,
      imapPassword: accountForm.imapPassword,
      imapEncryption: accountForm.imapEncryption,
    }

    // Axios interceptor returns res.data directly, so result is boolean
    const smtpResult = await testSmtpConnection(settings)
    if (smtpResult === true) {
      ElMessage.success('SMTP连接测试成功')
      
      const imapResult = await testImapConnection(settings)
      if (imapResult === true) {
        ElMessage.success('IMAP连接测试成功')
      } else {
        ElMessage.warning('SMTP连接成功，但IMAP连接失败')
      }
    } else {
      ElMessage.error('SMTP连接测试失败')
    }
  } catch (error) {
    console.error('Connection test error:', error)
    ElMessage.error('连接测试失败')
  }
}

async function saveAccount() {
  if (!accountFormRef.value) return

  await accountFormRef.value.validate(async (valid) => {
    if (valid) {
      savingAccount.value = true
      try {
        const settings: EmailSettingsDTO = {
          id: accountForm.id,
          emailAddress: accountForm.emailAddress,
          displayName: accountForm.displayName,
          smtpHost: accountForm.smtpHost,
          smtpPort: accountForm.smtpPort,
          smtpUsername: accountForm.smtpUsername,
          smtpPassword: accountForm.smtpPassword,
          smtpEncryption: accountForm.smtpEncryption,
          imapHost: accountForm.imapHost,
          imapPort: accountForm.imapPort,
          imapUsername: accountForm.imapUsername,
          imapPassword: accountForm.imapPassword,
          imapEncryption: accountForm.imapEncryption,
          syncInterval: accountForm.syncInterval,
          keepServerCopy: accountForm.keepServerCopy,
        }

        if (accountForm.id) {
          // Update existing account
          await updateEmailSettings(currentUserId.value, settings)
          ElMessage.success('账户更新成功')
        } else {
          // Create new account
          await saveEmailSettings(currentUserId.value, settings)
          ElMessage.success('账户添加成功')
        }

        addAccountVisible.value = false
        await loadEmailAccounts()
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        savingAccount.value = false
      }
    }
  })
}

function handleCreateTag() {
  ElMessage.info('创建标签功能')
}

function formatDate(date: string) {
  const now = dayjs()
  const d = dayjs(date)

  if (now.isSame(d, 'day')) {
    return d.format('HH:mm')
  } else if (now.diff(d, 'day') < 7) {
    return d.format('ddd')
  } else {
    return d.format('YYYY-MM-DD')
  }
}

function formatFullDate(date: string) {
  return dayjs(date).format('YYYY年MM月DD日 HH:mm')
}

function formatFileSize(bytes: number) {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

function handleSizeChange(val: number) {
  pageSize.value = val
  loadEmails()
}

function handleCurrentChange(val: number) {
  currentPage.value = val
  loadEmails()
}

onMounted(() => {
  loadEmailAccounts()
  loadEmails()
})
</script>

<style scoped lang="scss">
.email-client {
  display: flex;
  height: calc(100vh - 120px);
  background: #f5f7fa;
}

// Sidebar
.email-sidebar {
  width: 260px;
  background: #fff;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  overflow-y: auto;

  .compose-section {
    padding: 16px;
    border-bottom: 1px solid #e4e7ed;

    .compose-btn {
      width: 100%;
      height: 48px;
      font-size: 16px;
      box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
    }
  }

  .accounts-section,
  .folders-section,
  .tags-section {
    padding: 16px;
    border-bottom: 1px solid #e4e7ed;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      font-size: 14px;
      font-weight: 600;
      color: #303133;

      .add-account {
        cursor: pointer;
        color: #409eff;
        &:hover {
          color: #66b1ff;
        }
      }
    }
  }

  .account-list {
    .account-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s;
      margin-bottom: 4px;

      &:hover {
        background: #f5f7fa;

        .account-actions {
          opacity: 1;
        }
      }

      &.active {
        background: #ecf5ff;
      }

      .account-avatar {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
      }

      .account-info {
        flex: 1;
        min-width: 0;

        .account-name {
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .account-email {
          font-size: 12px;
          color: #909399;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .account-actions {
        display: flex;
        align-items: center;
        gap: 4px;
        opacity: 0;
        transition: opacity 0.3s;

        .edit-icon {
          cursor: pointer;
          color: #409eff;
          font-size: 16px;

          &:hover {
            color: #66b1ff;
          }
        }
      }
    }
  }

  .no-accounts {
    padding: 20px 0;
  }

  .folder-menu {
    border: none;

    :deep(.el-menu-item) {
      height: 40px;
      line-height: 40px;
      padding: 0 12px;

      .el-icon {
        margin-right: 8px;
      }

      &.is-active {
        background: #ecf5ff;
        color: #409eff;
      }
    }

    .folder-badge {
      margin-left: auto;
    }
  }

  .tag-list {
    .tag-item {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 6px 8px;
      font-size: 13px;
      color: #606266;
      cursor: pointer;
      border-radius: 4px;

      &:hover {
        background: #f5f7fa;
      }

      .tag-color {
        width: 12px;
        height: 12px;
        border-radius: 50%;
      }
    }
  }
}

// Main Content
.email-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
  min-width: 0;

  .email-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    border-bottom: 1px solid #e4e7ed;
    background: #fafafa;

    .toolbar-left,
    .toolbar-right {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .email-list-container {
    flex: 1;
    overflow: auto;

    .email-table {
      :deep(.el-table__row) {
        cursor: pointer;

        &:hover {
          background: #f5f7fa;
        }
      }

      .sender-cell {
        display: flex;
        align-items: center;
        gap: 8px;

        .sender-avatar {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          font-size: 12px;
        }

        .sender-name {
          font-size: 14px;
          color: #303133;
        }
      }

      .subject-cell {
        display: flex;
        align-items: center;
        gap: 6px;

        .unread-tag {
          flex-shrink: 0;
        }

        .unread-text {
          font-weight: 600;
          color: #303133;
        }

        .email-preview {
          color: #909399;
          font-size: 13px;
        }
      }

      .email-date {
        font-size: 13px;
        color: #909399;
      }
    }

    .pagination-container {
      padding: 16px;
      display: flex;
      justify-content: flex-end;
      border-top: 1px solid #e4e7ed;
    }
  }
}

// Reading Pane
.reading-pane {
  width: 500px;
  background: #fff;
  border-left: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  overflow-y: auto;

  .reading-header {
    padding: 12px 16px;
    border-bottom: 1px solid #e4e7ed;
    background: #fafafa;

    .reading-actions {
      display: flex;
      gap: 8px;
    }
  }

  .reading-content {
    flex: 1;
    padding: 20px;

    .email-subject {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 16px;
    }

    .email-meta {
      display: flex;
      gap: 12px;
      margin-bottom: 16px;

      .meta-avatar {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
      }

      .meta-info {
        flex: 1;

        .meta-from {
          font-size: 14px;
          color: #303133;
          margin-bottom: 4px;

          strong {
            margin-right: 8px;
          }

          .meta-email {
            color: #909399;
          }
        }

        .meta-to {
          font-size: 13px;
          color: #606266;

          span {
            color: #909399;
          }
        }
      }

      .meta-time {
        font-size: 13px;
        color: #909399;
        white-space: nowrap;
      }
    }

    .email-body {
      font-size: 14px;
      line-height: 1.8;
      color: #606266;
      min-height: 200px;
    }

    .attachments-section {
      .attachments-header {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 12px;
      }

      .attachments-list {
        display: flex;
        flex-direction: column;
        gap: 8px;

        .attachment-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 10px;
          background: #f5f7fa;
          border-radius: 6px;

          .attachment-info {
            flex: 1;

            .attachment-name {
              font-size: 13px;
              color: #303133;
              margin-bottom: 2px;
            }

            .attachment-size {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }
    }

    .reply-actions {
      margin-top: 20px;
      padding-top: 16px;
      border-top: 1px solid #e4e7ed;
      display: flex;
      gap: 12px;
    }
  }
}

// Compose Dialog
.compose-dialog {
  :deep(.el-dialog__body) {
    padding: 20px;
  }

  .compose-form {
    .editor-toolbar {
      margin-bottom: 8px;
      display: flex;
      gap: 8px;
    }

    .compose-textarea {
      :deep(textarea) {
        font-family: 'Microsoft YaHei', Arial, sans-serif;
        font-size: 14px;
        line-height: 1.6;
      }
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .form-tip {
    margin-left: 8px;
    font-size: 12px;
    color: #909399;
  }
}
</style>
