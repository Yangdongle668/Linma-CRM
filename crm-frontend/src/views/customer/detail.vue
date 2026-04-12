<template>
  <div class="customer-detail-page">
    <!-- 顶部操作栏 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="ArrowLeft" @click="goBack">返回</el-button>
        <h2 class="page-title">{{ isEdit ? '编辑客户' : '客户详情' }}</h2>
      </div>
      <div class="header-right">
        <el-button v-if="isEdit" @click="handleCancel">取消</el-button>
        <el-button v-if="!isEdit" type="primary" icon="Edit" @click="handleEdit">编辑</el-button>
        <el-button v-if="isEdit" type="success" icon="Check" @click="handleSave" :loading="saving">保存</el-button>
        <el-dropdown trigger="click" style="margin-left: 12px">
          <el-button icon="More">更多</el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item icon="Delete" @click="handleDelete">删除客户</el-dropdown-item>
              <el-dropdown-item icon="DocumentCopy" @click="handleDuplicate">复制客户</el-dropdown-item>
              <el-dropdown-item icon="Download" @click="handleExport">导出信息</el-dropdown-item>
              <el-dropdown-item divided icon="ChatDotRound" @click="handleSendEmail">
                <el-icon><Message /></el-icon>
                发送邮件
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <el-row :gutter="20" class="main-content">
      <!-- 左侧：客户基本信息卡片 -->
      <el-col :span="8">
        <el-card class="info-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
              <el-tag v-if="formData.priority" :type="getPriorityType(formData.priority)" size="small">
                {{ getPriorityLabel(formData.priority) }}
              </el-tag>
            </div>
          </template>

          <div class="customer-profile">
            <!-- 头像和公司名称 -->
            <div class="profile-header">
              <el-avatar :size="80" :src="formData.customerAvatar" class="company-avatar">
                {{ (formData.companyName || '?').charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="profile-info">
                <h3 class="company-name">{{ formData.companyName || '未命名客户' }}</h3>
                <p class="company-name-cn">{{ formData.companyNameCn }}</p>
                <div class="profile-tags">
                  <el-tag size="small" :type="getLevelType(formData.level)">
                    {{ formData.level || '未评级' }}
                  </el-tag>
                  <el-tag v-if="formData.customerLifecycle" size="small">
                    {{ getLifecycleLabel(formData.customerLifecycle) }}
                  </el-tag>
                </div>
              </div>
            </div>

            <el-divider />

            <!-- 快速联系 -->
            <div class="quick-contact">
              <div class="contact-item" v-if="primaryContact?.email">
                <el-icon><Message /></el-icon>
                <a :href="generateMailtoLink(primaryContact.email)" class="contact-link" target="_blank">
                  {{ primaryContact.email }}
                </a>
                <el-button link type="primary" size="small" @click="openEmailClient(primaryContact.email)">
                  发邮件
                </el-button>
              </div>
              <div class="contact-item" v-if="primaryContact?.phone">
                <el-icon><Phone /></el-icon>
                <span>{{ primaryContact.phone }}</span>
              </div>
              <div class="contact-item" v-if="formData.website">
                <el-icon><Link /></el-icon>
                <a :href="formData.website" target="_blank" class="contact-link">
                  {{ formData.website }}
                </a>
              </div>
            </div>

            <el-divider />

            <!-- 统计信息 -->
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-value">{{ customerStats.emailCount || 0 }}</div>
                <div class="stat-label">邮件往来</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ customerStats.callCount || 0 }}</div>
                <div class="stat-label">电话沟通</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ customerStats.meetingCount || 0 }}</div>
                <div class="stat-label">会议次数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ formatMoney(formData.totalOrder) }}</div>
                <div class="stat-label">累计订单</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 社交媒体 -->
        <el-card class="info-card" shadow="hover" v-if="hasSocialMedia">
          <template #header>
            <span>社交媒体</span>
          </template>
          <div class="social-links">
            <a v-if="formData.socialLinkedin" :href="formData.socialLinkedin" target="_blank" class="social-link linkedin">
              <el-icon :size="20"><Share /></el-icon>
              <span>LinkedIn</span>
            </a>
            <a v-if="formData.socialFacebook" :href="formData.socialFacebook" target="_blank" class="social-link facebook">
              <el-icon :size="20"><Share /></el-icon>
              <span>Facebook</span>
            </a>
            <a v-if="formData.socialTwitter" :href="formData.socialTwitter" target="_blank" class="social-link twitter">
              <el-icon :size="20"><Share /></el-icon>
              <span>Twitter</span>
            </a>
          </div>
        </el-card>

        <!-- 风险与机会 -->
        <el-card class="info-card" shadow="hover" v-if="formData.riskLevel || formData.opportunityValue">
          <template #header>
            <span>风险与机会</span>
          </template>
          <div class="risk-opportunity">
            <div v-if="formData.riskLevel" class="risk-item">
              <div class="label">风险等级</div>
              <el-tag :type="getRiskType(formData.riskLevel)" size="small">
                {{ getRiskLabel(formData.riskLevel) }}
              </el-tag>
            </div>
            <div v-if="formData.opportunityValue" class="opportunity-item">
              <div class="label">商机价值</div>
              <div class="value">{{ formatMoney(formData.opportunityValue) }}</div>
            </div>
            <div v-if="formData.conversionProbability" class="probability-item">
              <div class="label">转化概率</div>
              <el-progress :percentage="formData.conversionProbability" :stroke-width="8" />
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：详细信息Tabs -->
      <el-col :span="16">
        <el-tabs v-model="activeTab" class="detail-tabs">
          <!-- 基本信息 Tab -->
          <el-tab-pane label="基本信息" name="basic">
            <el-form
              ref="formRef"
              :model="formData"
              :rules="formRules"
              label-width="140px"
              :disabled="!isEdit"
              class="detail-form"
            >
              <el-divider content-position="left">公司信息</el-divider>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="公司名称(英文)" prop="companyName">
                    <el-input v-model="formData.companyName" placeholder="请输入公司英文名称" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="公司名称(中文)">
                    <el-input v-model="formData.companyNameCn" placeholder="请输入公司中文名称" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="公司网站" prop="website">
                    <el-input v-model="formData.website" placeholder="https://www.example.com">
                      <template #prefix>
                        <el-icon><Link /></el-icon>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="邮箱域名">
                    <el-input v-model="formData.emailDomain" disabled placeholder="自动从邮箱提取">
                      <template #prefix>
                        <el-icon><At /></el-icon>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="行业分类" prop="industry">
                    <el-select v-model="formData.industry" placeholder="选择行业" clearable>
                      <el-option label="电子产品" value="electronics" />
                      <el-option label="机械制造" value="machinery" />
                      <el-option label="纺织服装" value="textile" />
                      <el-option label="化工材料" value="chemical" />
                      <el-option label="食品饮料" value="food_beverage" />
                      <el-option label="医疗器械" value="medical" />
                      <el-option label="汽车配件" value="automotive" />
                      <el-option label="建筑材料" value="construction" />
                      <el-option label="家居用品" value="home_goods" />
                      <el-option label="其他" value="other" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="业务类型">
                    <el-select v-model="formData.businessType" placeholder="选择类型" clearable>
                      <el-option label="制造商" value="manufacturer" />
                      <el-option label="贸易商" value="trader" />
                      <el-option label="分销商" value="distributor" />
                      <el-option label="零售商" value="retailer" />
                      <el-option label="批发商" value="wholesaler" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="公司规模">
                    <el-select v-model="formData.companySize" placeholder="选择规模" clearable>
                      <el-option label="1-10人" value="1-10" />
                      <el-option label="11-50人" value="11-50" />
                      <el-option label="51-200人" value="51-200" />
                      <el-option label="201-500人" value="201-500" />
                      <el-option label="500+人" value="500+" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="主营产品">
                <el-input
                  v-model="formData.mainProducts"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入主营产品或服务"
                />
              </el-form-item>

              <el-form-item label="主要市场">
                <el-input
                  v-model="formData.mainMarkets"
                  placeholder="多个国家用逗号分隔，如：USA, Germany, UK"
                />
              </el-form-item>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="成立年份">
                    <el-date-picker
                      v-model="formData.establishedYear"
                      type="year"
                      placeholder="选择年份"
                      format="YYYY"
                      value-format="YYYY"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="年营业额(USD)">
                    <el-input-number
                      v-model="formData.annualRevenue"
                      :min="0"
                      :precision="2"
                      controls-position="right"
                      style="width: 100%"
                      placeholder="请输入年营业额"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="税号/VAT号">
                    <el-input v-model="formData.taxId" placeholder="请输入税号" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-divider content-position="left">联系信息</el-divider>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="国家/地区">
                    <el-select v-model="formData.country" placeholder="选择国家" filterable clearable>
                      <el-option label="美国" value="US" />
                      <el-option label="德国" value="DE" />
                      <el-option label="英国" value="GB" />
                      <el-option label="澳大利亚" value="AU" />
                      <el-option label="加拿大" value="CA" />
                      <el-option label="日本" value="JP" />
                      <el-option label="韩国" value="KR" />
                      <el-option label="中国" value="CN" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="省份/州">
                    <el-input v-model="formData.province" placeholder="请输入省份" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="城市">
                    <el-input v-model="formData.city" placeholder="请输入城市" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="详细地址">
                <el-input
                  v-model="formData.address"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入详细地址"
                />
              </el-form-item>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="时区">
                    <el-select v-model="formData.timezone" placeholder="选择时区" clearable>
                      <el-option label="UTC-8 (洛杉矶)" value="America/Los_Angeles" />
                      <el-option label="UTC-5 (纽约)" value="America/New_York" />
                      <el-option label="UTC+0 (伦敦)" value="Europe/London" />
                      <el-option label="UTC+1 (柏林)" value="Europe/Berlin" />
                      <el-option label="UTC+8 (北京)" value="Asia/Shanghai" />
                      <el-option label="UTC+9 (东京)" value="Asia/Tokyo" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="语言偏好">
                    <el-select v-model="formData.languagePreference" placeholder="选择语言" clearable>
                      <el-option label="英语" value="en" />
                      <el-option label="中文" value="zh" />
                      <el-option label="西班牙语" value="es" />
                      <el-option label="法语" value="fr" />
                      <el-option label="德语" value="de" />
                      <el-option label="日语" value="ja" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-divider content-position="left">客户分级</el-divider>

              <el-row :gutter="20">
                <el-col :span="6">
                  <el-form-item label="客户等级">
                    <el-select v-model="formData.level" placeholder="选择等级">
                      <el-option label="A级 - 重要客户" value="A" />
                      <el-option label="B级 - 普通客户" value="B" />
                      <el-option label="C级 - 小客户" value="C" />
                      <el-option label="D级 - 潜在客户" value="D" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="优先级">
                    <el-select v-model="formData.priority" placeholder="选择优先级">
                      <el-option label="高优先级" value="high">
                        <el-tag type="danger" size="small">高</el-tag>
                      </el-option>
                      <el-option label="中优先级" value="medium">
                        <el-tag type="warning" size="small">中</el-tag>
                      </el-option>
                      <el-option label="低优先级" value="low">
                        <el-tag type="info" size="small">低</el-tag>
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="信用评级">
                    <el-select v-model="formData.creditRating" placeholder="选择评级" clearable>
                      <el-option label="AAA" value="AAA" />
                      <el-option label="AA" value="AA" />
                      <el-option label="A" value="A" />
                      <el-option label="B" value="B" />
                      <el-option label="C" value="C" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="付款条件">
                    <el-select v-model="formData.paymentTerms" placeholder="选择条件" clearable>
                      <el-option label="T/T" value="TT" />
                      <el-option label="L/C" value="LC" />
                      <el-option label="PayPal" value="PayPal" />
                      <el-option label="Western Union" value="WU" />
                      <el-option label="OA 30天" value="OA30" />
                      <el-option label="OA 60天" value="OA60" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="客户来源">
                    <el-select v-model="formData.source" placeholder="选择来源" clearable>
                      <el-option label="展会" value="exhibition" />
                      <el-option label="阿里巴巴" value="alibaba" />
                      <el-option label="官网" value="website" />
                      <el-option label="转介绍" value="referral" />
                      <el-option label="Google搜索" value="google" />
                      <el-option label="社交媒体" value="social" />
                      <el-option label="冷开发" value="cold_call" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="获客渠道">
                    <el-select v-model="formData.acquisitionChannel" placeholder="选择渠道" clearable>
                      <el-option label="SEO" value="seo" />
                      <el-option label="SEM" value="sem" />
                      <el-option label="社交媒体" value="social_media" />
                      <el-option label="内容营销" value="content" />
                      <el-option label="邮件营销" value="email" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="首次接触日期">
                    <el-date-picker
                      v-model="formData.firstContactDate"
                      type="date"
                      placeholder="选择日期"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="客户生命周期">
                <el-radio-group v-model="formData.customerLifecycle">
                  <el-radio label="prospect">潜在客户</el-radio>
                  <el-radio label="lead">销售线索</el-radio>
                  <el-radio label="opportunity">商机</el-radio>
                  <el-radio label="customer">正式客户</el-radio>
                  <el-radio label="churned">流失客户</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-divider content-position="left">认证信息</el-divider>

              <el-form-item label="认证资质">
                <el-checkbox-group v-model="certifications">
                  <el-checkbox label="iso">ISO认证</el-checkbox>
                  <el-checkbox label="ce">CE认证</el-checkbox>
                  <el-checkbox label="fda">FDA认证</el-checkbox>
                </el-checkbox-group>
              </el-form-item>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="注册号">
                    <el-input v-model="formData.registrationNumber" placeholder="请输入公司注册号" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="进口许可证">
                    <el-input v-model="formData.importLicense" placeholder="请输入进口许可证号" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-divider content-position="left">社交媒体</el-divider>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="LinkedIn">
                    <el-input v-model="formData.socialLinkedin" placeholder="LinkedIn主页链接">
                      <template #prefix>
                        <el-icon><Share /></el-icon>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="Facebook">
                    <el-input v-model="formData.socialFacebook" placeholder="Facebook主页链接">
                      <template #prefix>
                        <el-icon><Share /></el-icon>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="Twitter">
                    <el-input v-model="formData.socialTwitter" placeholder="Twitter主页链接">
                      <template #prefix>
                        <el-icon><Share /></el-icon>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-divider content-position="left">分析信息</el-divider>

              <el-form-item label="竞争对手信息">
                <el-input
                  v-model="formData.competitorInfo"
                  type="textarea"
                  :rows="3"
                  placeholder="记录客户正在合作的竞争对手信息"
                />
              </el-form-item>

              <el-form-item label="SWOT分析">
                <el-input
                  v-model="formData.swotAnalysis"
                  type="textarea"
                  :rows="4"
                  placeholder="Strengths, Weaknesses, Opportunities, Threats"
                />
              </el-form-item>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="风险等级">
                    <el-select v-model="formData.riskLevel" placeholder="选择风险等级">
                      <el-option label="低风险" value="low">
                        <el-tag type="success" size="small">低</el-tag>
                      </el-option>
                      <el-option label="中风险" value="medium">
                        <el-tag type="warning" size="small">中</el-tag>
                      </el-option>
                      <el-option label="高风险" value="high">
                        <el-tag type="danger" size="small">高</el-tag>
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="16">
                  <el-form-item label="风险因素">
                    <el-input v-model="formData.riskFactors" placeholder="描述潜在风险因素" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="商机价值(USD)">
                    <el-input-number
                      v-model="formData.opportunityValue"
                      :min="0"
                      :precision="2"
                      controls-position="right"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="转化概率(%)">
                    <el-slider v-model="formData.conversionProbability" :max="100" show-input />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="预计成交日期">
                    <el-date-picker
                      v-model="formData.expectedCloseDate"
                      type="date"
                      placeholder="选择日期"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="满意度评分">
                    <el-rate v-model="formData.satisfactionScore" :max="5" show-text />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="净推荐值(NPS)">
                    <el-input-number
                      v-model="formData.npsScore"
                      :min="-100"
                      :max="100"
                      controls-position="right"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="备注">
                <el-input
                  v-model="formData.remark"
                  type="textarea"
                  :rows="3"
                  placeholder="其他备注信息"
                />
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <!-- 联系人 Tab -->
          <el-tab-pane label="联系人" name="contacts">
            <div class="tab-content">
              <el-button type="primary" icon="Plus" @click="handleAddContact" style="margin-bottom: 16px">
                添加联系人
              </el-button>
              <el-table :data="contacts" border stripe>
                <el-table-column prop="name" label="姓名" width="120" />
                <el-table-column prop="position" label="职位" width="150" />
                <el-table-column prop="email" label="邮箱" width="200">
                  <template #default="{ row }">
                    <a :href="generateMailtoLink(row.email)" target="_blank" class="email-link">
                      {{ row.email }}
                    </a>
                    <el-button
                      link
                      type="primary"
                      size="small"
                      @click="openEmailClient(row.email)"
                      style="margin-left: 8px"
                    >
                      发邮件
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column prop="phone" label="电话" width="150" />
                <el-table-column prop="isPrimary" label="主要联系人" width="100">
                  <template #default="{ row }">
                    <el-tag v-if="row.isPrimary" type="success" size="small">是</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template #default="{ row }">
                    <el-button link type="primary" size="small" @click="handleEditContact(row)">
                      编辑
                    </el-button>
                    <el-button link type="danger" size="small" @click="handleDeleteContact(row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>

          <!-- 跟进记录 Tab -->
          <el-tab-pane label="跟进记录" name="followups">
            <div class="tab-content">
              <el-button type="primary" icon="Plus" @click="handleAddFollowUp" style="margin-bottom: 16px">
                添加跟进
              </el-button>

              <!-- 跟进统计 -->
              <el-row :gutter="16" class="followup-stats" v-if="followUpStats.length > 0">
                <el-col :span="6" v-for="stat in followUpStats" :key="stat.followType">
                  <el-card shadow="hover" class="stat-card">
                    <div class="stat-content">
                      <div class="stat-icon" :class="getFollowUpIconClass(stat.followType)">
                        <el-icon :size="24"><component :is="getFollowUpIcon(stat.followType)" /></el-icon>
                      </div>
                      <div class="stat-info">
                        <div class="stat-value">{{ stat.count }}</div>
                        <div class="stat-label">{{ getFollowUpTypeLabel(stat.followType) }}</div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>

              <!-- 跟进时间线 -->
              <el-timeline class="followup-timeline">
                <el-timeline-item
                  v-for="record in followUpRecords"
                  :key="record.id"
                  :timestamp="formatDateTime(record.startTime)"
                  placement="top"
                  :type="getTimelineItemType(record.sentiment)"
                >
                  <el-card shadow="hover" class="followup-card">
                    <div class="followup-header">
                      <el-tag :type="getFollowUpTypeTag(record.followType)" size="small">
                        {{ getFollowUpTypeLabel(record.followType) }}
                      </el-tag>
                      <el-tag v-if="record.direction" size="small" :type="record.direction === 'inbound' ? 'info' : 'primary'">
                        {{ record.direction === 'inbound' ? '收到' : '发出' }}
                      </el-tag>
                      <el-tag v-if="record.sentiment" size="small" :type="getSentimentType(record.sentiment)">
                        {{ getSentimentLabel(record.sentiment) }}
                      </el-tag>
                      <span class="followup-creator">by {{ record.creatorName }}</span>
                    </div>
                    <h4 class="followup-subject">{{ record.subject }}</h4>
                    <p class="followup-content">{{ record.content }}</p>
                    <div v-if="record.outcome" class="followup-outcome">
                      <strong>结果：</strong>{{ record.outcome }}
                    </div>
                    <div v-if="record.nextStep" class="followup-next">
                      <strong>下一步：</strong>{{ record.nextStep }}
                    </div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>

              <el-empty v-if="followUpRecords.length === 0" description="暂无跟进记录" />
            </div>
          </el-tab-pane>

          <!-- 邮件历史 Tab -->
          <el-tab-pane label="邮件历史" name="emails">
            <div class="tab-content">
              <el-button type="primary" icon="Edit" @click="handleSendEmail" style="margin-bottom: 16px">
                写信
              </el-button>

              <el-table :data="emailHistory" border stripe v-loading="emailLoading">
                <el-table-column prop="subject" label="主题" min-width="250" show-overflow-tooltip>
                  <template #default="{ row }">
                    <el-icon v-if="!row.read" color="#F56C6C"><CircleCloseFilled /></el-icon>
                    {{ row.subject }}
                  </template>
                </el-table-column>
                <el-table-column prop="direction" label="方向" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.direction === 'inbound' ? 'info' : 'primary'" size="small">
                      {{ row.direction === 'inbound' ? '收到' : '发出' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="time" label="时间" width="160" />
                <el-table-column label="操作" width="100">
                  <template #default="{ row }">
                    <el-button link type="primary" size="small" @click="viewEmailDetail(row)">
                      查看
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>

          <!-- 附件 Tab -->
          <el-tab-pane label="附件文档" name="attachments">
            <div class="tab-content">
              <el-upload
                action="#"
                multiple
                :auto-upload="false"
                :on-change="handleAttachmentUpload"
                drag
              >
                <el-icon :size="50"><UploadFilled /></el-icon>
                <div class="el-upload__text">拖拽文件到此处或<em>点击上传</em></div>
              </el-upload>

              <el-table :data="attachments" border stripe style="margin-top: 16px">
                <el-table-column prop="fileName" label="文件名" min-width="200" />
                <el-table-column prop="fileSize" label="大小" width="100">
                  <template #default="{ row }">
                    {{ formatFileSize(row.fileSize) }}
                  </template>
                </el-table-column>
                <el-table-column prop="category" label="分类" width="120" />
                <el-table-column prop="uploadedTime" label="上传时间" width="160" />
                <el-table-column label="操作" width="150">
                  <template #default="{ row }">
                    <el-button link type="primary" size="small" @click="downloadAttachment(row)">
                      下载
                    </el-button>
                    <el-button link type="danger" size="small" @click="deleteAttachment(row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>

    <!-- 联系人对话框 -->
    <el-dialog v-model="contactDialogVisible" :title="contactDialogTitle" width="600px">
      <el-form :model="contactForm" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="contactForm.name" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="contactForm.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="contactForm.email" placeholder="请输入邮箱地址" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="contactForm.phone" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="主要联系人">
          <el-switch v-model="contactForm.isPrimary" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="contactDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveContact">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import dayjs from 'dayjs'

const router = useRouter()
const route = useRoute()

// State
const isEdit = ref(false)
const saving = ref(false)
const activeTab = ref('basic')
const formRef = ref<FormInstance>()

// Customer data
const formData = reactive<any>({
  id: null,
  companyName: '',
  companyNameCn: '',
  website: '',
  emailDomain: '',
  customerAvatar: '',
  companyLogo: '',
  socialLinkedin: '',
  socialFacebook: '',
  socialTwitter: '',
  industry: '',
  companySize: '',
  businessType: '',
  mainProducts: '',
  mainMarkets: '',
  establishedYear: null,
  annualRevenue: null,
  taxId: '',
  registrationNumber: '',
  importLicense: '',
  country: '',
  province: '',
  city: '',
  address: '',
  timezone: '',
  languagePreference: '',
  level: '',
  priority: '',
  creditRating: '',
  paymentTerms: '',
  source: '',
  acquisitionChannel: '',
  firstContactDate: null,
  customerLifecycle: 'prospect',
  certificationIso: 0,
  certificationCe: 0,
  certificationFda: 0,
  riskLevel: '',
  riskFactors: '',
  opportunityValue: null,
  conversionProbability: null,
  expectedCloseDate: null,
  satisfactionScore: 0,
  npsScore: 0,
  totalOrder: 0,
  remark: '',
})

const certifications = ref<string[]>([])

// Contacts
const contacts = ref<any[]>([])
const contactDialogVisible = ref(false)
const contactForm = reactive({
  id: null,
  name: '',
  position: '',
  email: '',
  phone: '',
  isPrimary: false,
})

// Follow-up records
const followUpRecords = ref<any[]>([])
const followUpStats = ref<any[]>([])

// Email history
const emailHistory = ref<any[]>([])
const emailLoading = ref(false)

// Attachments
const attachments = ref<any[]>([])

// Stats
const customerStats = ref({
  emailCount: 0,
  callCount: 0,
  meetingCount: 0,
})

// Computed
const primaryContact = computed(() => contacts.value.find(c => c.isPrimary) || contacts.value[0])
const hasSocialMedia = computed(() => formData.socialLinkedin || formData.socialFacebook || formData.socialTwitter)
const contactDialogTitle = computed(() => contactForm.id ? '编辑联系人' : '添加联系人')

// Form rules
const formRules: FormRules = {
  companyName: [{ required: true, message: '请输入公司名称', trigger: 'blur' }],
  website: [
    { type: 'url', message: '请输入有效的网址', trigger: 'blur' },
  ],
}

// Methods
function goBack() {
  router.back()
}

function handleEdit() {
  isEdit.value = true
}

function handleCancel() {
  isEdit.value = false
  loadCustomerData()
}

async function handleSave() {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        // Update certifications
        formData.certificationIso = certifications.value.includes('iso') ? 1 : 0
        formData.certificationCe = certifications.value.includes('ce') ? 1 : 0
        formData.certificationFda = certifications.value.includes('fda') ? 1 : 0

        // TODO: Call API to save
        await new Promise(resolve => setTimeout(resolve, 1000))

        ElMessage.success('保存成功')
        isEdit.value = false
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        saving.value = false
      }
    }
  })
}

function handleDelete() {
  ElMessageBox.confirm('确定要删除此客户吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    ElMessage.success('已删除')
    router.back()
  })
}

function handleDuplicate() {
  ElMessage.info('复制功能开发中')
}

function handleExport() {
  ElMessage.info('导出功能开发中')
}

function handleSendEmail() {
  if (primaryContact.value?.email) {
    openEmailClient(primaryContact.value.email)
  } else {
    ElMessage.warning('该客户没有联系邮箱')
  }
}

function generateMailtoLink(email: string, subject?: string, body?: string) {
  let link = `mailto:${email}`
  const params = []
  if (subject) params.push(`subject=${encodeURIComponent(subject)}`)
  if (body) params.push(`body=${encodeURIComponent(body)}`)
  if (params.length > 0) link += '?' + params.join('&')
  return link
}

function openEmailClient(email: string, subject?: string, body?: string) {
  const mailtoLink = generateMailtoLink(email, subject, body)
  window.open(mailtoLink, '_blank')
}

function handleAddContact() {
  Object.assign(contactForm, { id: null, name: '', position: '', email: '', phone: '', isPrimary: false })
  contactDialogVisible.value = true
}

function handleEditContact(row: any) {
  Object.assign(contactForm, row)
  contactDialogVisible.value = true
}

function handleDeleteContact(row: any) {
  ElMessageBox.confirm('确定要删除此联系人吗？', '提示', {
    type: 'warning',
  }).then(() => {
    const index = contacts.value.findIndex(c => c.id === row.id)
    if (index > -1) contacts.value.splice(index, 1)
    ElMessage.success('已删除')
  })
}

function saveContact() {
  if (!contactForm.name) {
    ElMessage.warning('请输入联系人姓名')
    return
  }

  if (contactForm.id) {
    const index = contacts.value.findIndex(c => c.id === contactForm.id)
    if (index > -1) contacts.value[index] = { ...contactForm }
  } else {
    contacts.value.push({ ...contactForm, id: Date.now() })
  }

  contactDialogVisible.value = false
  ElMessage.success('保存成功')
}

function handleAddFollowUp() {
  ElMessage.info('添加跟进记录功能开发中')
}

function viewEmailDetail(row: any) {
  ElMessage.info('查看邮件详情功能开发中')
}

function handleAttachmentUpload(file: any, fileList: any[]) {
  ElMessage.success(`已选择 ${fileList.length} 个文件`)
}

function downloadAttachment(row: any) {
  ElMessage.info('下载功能开发中')
}

function deleteAttachment(row: any) {
  ElMessageBox.confirm('确定要删除此附件吗？', '提示', {
    type: 'warning',
  }).then(() => {
    const index = attachments.value.findIndex(a => a.id === row.id)
    if (index > -1) attachments.value.splice(index, 1)
    ElMessage.success('已删除')
  })
}

// Helper functions
function getPriorityType(priority: string) {
  const types: Record<string, any> = { high: 'danger', medium: 'warning', low: 'info' }
  return types[priority] || ''
}

function getPriorityLabel(priority: string) {
  const labels: Record<string, string> = { high: '高优先级', medium: '中优先级', low: '低优先级' }
  return labels[priority] || priority
}

function getLevelType(level: string) {
  const types: Record<string, any> = { A: 'danger', B: 'warning', C: 'info', D: '' }
  return types[level] || ''
}

function getLifecycleLabel(lifecycle: string) {
  const labels: Record<string, string> = {
    prospect: '潜在客户',
    lead: '销售线索',
    opportunity: '商机',
    customer: '正式客户',
    churned: '流失客户',
  }
  return labels[lifecycle] || lifecycle
}

function getRiskType(risk: string) {
  const types: Record<string, any> = { low: 'success', medium: 'warning', high: 'danger' }
  return types[risk] || ''
}

function getRiskLabel(risk: string) {
  const labels: Record<string, string> = { low: '低风险', medium: '中风险', high: '高风险' }
  return labels[risk] || risk
}

function getFollowUpTypeLabel(type: string) {
  const labels: Record<string, string> = {
    email: '邮件',
    call: '电话',
    meeting: '会议',
    visit: '拜访',
    message: '消息',
    other: '其他',
  }
  return labels[type] || type
}

function getFollowUpTypeTag(type: string) {
  const tags: Record<string, any> = {
    email: 'primary',
    call: 'success',
    meeting: 'warning',
    visit: 'danger',
    message: 'info',
  }
  return tags[type] || ''
}

function getFollowUpIcon(type: string) {
  const icons: Record<string, string> = {
    email: 'Message',
    call: 'Phone',
    meeting: 'VideoCamera',
    visit: 'Location',
    message: 'ChatDotRound',
  }
  return icons[type] || 'Document'
}

function getFollowUpIconClass(type: string) {
  return `icon-${type}`
}

function getSentimentType(sentiment: string) {
  const types: Record<string, any> = { positive: 'success', neutral: '', negative: 'danger' }
  return types[sentiment] || ''
}

function getSentimentLabel(sentiment: string) {
  const labels: Record<string, string> = { positive: '积极', neutral: '中性', negative: '消极' }
  return labels[sentiment] || sentiment
}

function getTimelineItemType(sentiment: string) {
  const types: Record<string, any> = { positive: 'success', neutral: 'primary', negative: 'danger' }
  return types[sentiment] || 'primary'
}

function formatMoney(value: number | null | undefined) {
  if (!value) return '$0'
  return `$${Number(value).toLocaleString()}`
}

function formatDateTime(date: string | null | undefined) {
  if (!date) return '-'
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

function formatFileSize(bytes: number) {
  if (!bytes) return '0 B'
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

async function loadCustomerData() {
  const customerId = route.params.id
  // TODO: Load customer data from API
  console.log('Loading customer:', customerId)
}

async function loadFollowUpRecords() {
  const customerId = route.params.id
  // TODO: Load follow-up records from API
  console.log('Loading follow-ups for customer:', customerId)
}

async function loadEmailHistory() {
  emailLoading.value = true
  try {
    // TODO: Load email history from API
    await new Promise(resolve => setTimeout(resolve, 500))
  } finally {
    emailLoading.value = false
  }
}

onMounted(() => {
  loadCustomerData()
  loadFollowUpRecords()
  loadEmailHistory()
})
</script>

<style scoped lang="scss">
.customer-detail-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }
}

.main-content {
  margin-bottom: 20px;
}

.info-card {
  margin-bottom: 16px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }
}

.customer-profile {
  .profile-header {
    display: flex;
    gap: 16px;
    margin-bottom: 16px;

    .company-avatar {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      font-size: 32px;
      font-weight: 600;
      flex-shrink: 0;
    }

    .profile-info {
      flex: 1;
      min-width: 0;

      .company-name {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 4px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .company-name-cn {
        font-size: 14px;
        color: #909399;
        margin: 0 0 8px;
      }

      .profile-tags {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }
    }
  }

  .quick-contact {
    .contact-item {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 0;
      font-size: 14px;
      color: #606266;

      .el-icon {
        color: #909399;
      }

      .contact-link {
        color: #409eff;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;

    .stat-item {
      text-align: center;
      padding: 12px;
      background: #f5f7fa;
      border-radius: 6px;

      .stat-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.social-links {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .social-link {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 12px;
    border-radius: 6px;
    text-decoration: none;
    color: #606266;
    transition: all 0.3s;

    &:hover {
      background: #f5f7fa;
      color: #409eff;
    }

    &.linkedin:hover {
      color: #0077b5;
    }

    &.facebook:hover {
      color: #1877f2;
    }

    &.twitter:hover {
      color: #1da1f2;
    }
  }
}

.risk-opportunity {
  .risk-item,
  .opportunity-item,
  .probability-item {
    margin-bottom: 12px;

    .label {
      font-size: 13px;
      color: #909399;
      margin-bottom: 6px;
    }

    .value {
      font-size: 18px;
      font-weight: 600;
      color: #67c23a;
    }
  }
}

.detail-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 0;
    background: #fff;
    padding: 0 20px;
    border-radius: 8px 8px 0 0;
  }

  :deep(.el-tabs__content) {
    padding: 20px;
    background: #fff;
    border-radius: 0 0 8px 8px;
  }
}

.detail-form {
  max-width: 100%;
}

.tab-content {
  min-height: 400px;
}

.followup-stats {
  margin-bottom: 20px;

  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: 12px;

      .stat-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;

        &.icon-email {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        &.icon-call {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &.icon-meeting {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.icon-visit {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-info {
        .stat-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
        }

        .stat-label {
          font-size: 13px;
          color: #909399;
        }
      }
    }
  }
}

.followup-timeline {
  padding: 20px 0;

  .followup-card {
    .followup-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;

      .followup-creator {
        margin-left: auto;
        font-size: 13px;
        color: #909399;
      }
    }

    .followup-subject {
      font-size: 15px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px;
    }

    .followup-content {
      font-size: 14px;
      color: #606266;
      line-height: 1.6;
      margin: 0 0 12px;
    }

    .followup-outcome,
    .followup-next {
      font-size: 13px;
      color: #909399;
      margin-bottom: 4px;

      strong {
        color: #606266;
      }
    }
  }
}

.email-link {
  color: #409eff;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}
</style>
