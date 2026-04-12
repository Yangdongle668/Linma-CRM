/**
 * 表单验证工具
 */

/**
 * 手机号验证
 */
export function validatePhone(phone: string): boolean {
  const reg = /^1[3-9]\d{9}$/
  return reg.test(phone)
}

/**
 * 邮箱验证
 */
export function validateEmail(email: string): boolean {
  const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  return reg.test(email)
}

/**
 * 身份证验证
 */
export function validateIdCard(idCard: string): boolean {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return reg.test(idCard)
}

/**
 * URL验证
 */
export function validateUrl(url: string): boolean {
  const reg = /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([/\w .-]*)*\/?$/
  return reg.test(url)
}

/**
 * 密码强度验证（至少8位，包含字母和数字）
 */
export function validatePassword(password: string): { valid: boolean; message: string } {
  if (password.length < 8) {
    return { valid: false, message: '密码长度不能少于8位' }
  }
  if (!/[a-zA-Z]/.test(password)) {
    return { valid: false, message: '密码必须包含字母' }
  }
  if (!/\d/.test(password)) {
    return { valid: false, message: '密码必须包含数字' }
  }
  return { valid: true, message: '' }
}

/**
 * 用户名验证（4-20位，只能包含字母、数字、下划线）
 */
export function validateUsername(username: string): { valid: boolean; message: string } {
  const reg = /^[a-zA-Z0-9_]{4,20}$/
  if (!reg.test(username)) {
    return {
      valid: false,
      message: '用户名长度为4-20位，只能包含字母、数字和下划线',
    }
  }
  return { valid: true, message: '' }
}

/**
 * 中文验证
 */
export function validateChinese(text: string): boolean {
  const reg = /^[\u4e00-\u9fa5]+$/
  return reg.test(text)
}

/**
 * 金额验证（正数，最多两位小数）
 */
export function validateAmount(amount: string | number): boolean {
  const reg = /^\d+(\.\d{1,2})?$/
  return reg.test(String(amount)) && Number(amount) > 0
}

/**
 * 整数验证
 */
export function validateInteger(value: string | number): boolean {
  const reg = /^\d+$/
  return reg.test(String(value))
}

/**
 * 车牌号验证
 */
export function validateLicensePlate(plate: string): boolean {
  const reg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳]$/
  return reg.test(plate)
}

/**
 * 统一社会信用代码验证
 */
export function validateCreditCode(code: string): boolean {
  const reg = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/
  return reg.test(code)
}

/**
 * Element Plus 表单验证规则生成器
 */
export const formRules = {
  /**
   * 必填规则
   */
  required: (message?: string) => ({
    required: true,
    message: message || '此项为必填项',
    trigger: ['blur', 'change'],
  }),

  /**
   * 邮箱验证规则
   */
  email: (message?: string) => ({
    pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    message: message || '请输入正确的邮箱地址',
    trigger: 'blur',
  }),

  /**
   * 手机号验证规则
   */
  phone: (message?: string) => ({
    pattern: /^1[3-9]\d{9}$/,
    message: message || '请输入正确的手机号码',
    trigger: 'blur',
  }),

  /**
   * URL验证规则
   */
  url: (message?: string) => ({
    pattern: /^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})([/\w .-]*)*\/?$/,
    message: message || '请输入正确的URL地址',
    trigger: 'blur',
  }),

  /**
   * 数字验证规则
   */
  number: (message?: string) => ({
    pattern: /^\d+(\.\d+)?$/,
    message: message || '请输入有效的数字',
    trigger: 'blur',
  }),

  /**
   * 整数验证规则
   */
  integer: (message?: string) => ({
    pattern: /^\d+$/,
    message: message || '请输入有效的整数',
    trigger: 'blur',
  }),

  /**
   * 最小长度规则
   */
  minLength: (min: number, message?: string) => ({
    min,
    message: message || `长度不能少于${min}个字符`,
    trigger: 'blur',
  }),

  /**
   * 最大长度规则
   */
  maxLength: (max: number, message?: string) => ({
    max,
    message: message || `长度不能超过${max}个字符`,
    trigger: 'blur',
  }),

  /**
   * 范围验证规则
   */
  range: (min: number, max: number, message?: string) => ({
    validator: (_rule: any, value: any, callback: any) => {
      const num = Number(value)
      if (isNaN(num) || num < min || num > max) {
        callback(new Error(message || `值必须在${min}到${max}之间`))
      } else {
        callback()
      }
    },
    trigger: 'blur',
  }),

  /**
   * 自定义验证器
   */
  custom: (validator: (value: any) => boolean, message: string) => ({
    validator: (_rule: any, value: any, callback: any) => {
      if (!value || !validator(value)) {
        callback(new Error(message))
      } else {
        callback()
      }
    },
    trigger: ['blur', 'change'],
  }),
}

/**
 * 组合多个验证规则
 */
export function combineRules(...rules: any[]) {
  return rules
}

/**
 * 常用表单验证规则预设
 */
export const commonRules = {
  // 姓名
  name: [formRules.required('请输入姓名'), formRules.minLength(2, '姓名至少2个字符')],

  // 手机号
  phone: [formRules.required('请输入手机号'), formRules.phone()],

  // 邮箱
  email: [formRules.required('请输入邮箱'), formRules.email()],

  // 密码
  password: [
    formRules.required('请输入密码'),
    formRules.minLength(8, '密码至少8位'),
    formRules.custom(
      (val) => /[a-zA-Z]/.test(val) && /\d/.test(val),
      '密码必须包含字母和数字'
    ),
  ],

  // 确认密码
  confirmPassword: (passwordField: string = 'password') => [
    formRules.required('请确认密码'),
    {
      validator: (_rule: any, value: any, callback: any) => {
        if (value !== passwordField) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],

  // 金额
  amount: [formRules.required('请输入金额'), formRules.number()],

  // 数量
  quantity: [formRules.required('请输入数量'), formRules.integer()],

  // URL
  url: [formRules.required('请输入URL'), formRules.url()],

  // 身份证
  idCard: [
    formRules.required('请输入身份证号'),
    formRules.custom(validateIdCard, '请输入正确的身份证号'),
  ],
}
