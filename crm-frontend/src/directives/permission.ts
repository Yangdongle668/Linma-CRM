/**
 * 权限控制指令
 */

import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * v-permission 指令
 * 用法: v-permission="'admin'" 或 v-permission="['admin', 'editor']"
 */
export const permission: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding)
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding)
  },
}

function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
  const userStore = useUserStore()
  const { value } = binding

  if (value && Array.isArray(value) && value.length > 0) {
    const permissionRoles = value
    const hasPermission = userStore.userInfo?.roles?.some((role: string) =>
      permissionRoles.includes(role)
    )

    if (!hasPermission) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  } else {
    throw new Error('请设置权限标签值')
  }
}

/**
 * v-role 指令
 * 用法: v-role="'admin'"
 */
export const role: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkRole(el, binding)
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkRole(el, binding)
  },
}

function checkRole(el: HTMLElement, binding: DirectiveBinding) {
  const userStore = useUserStore()
  const { value } = binding

  if (value) {
    const hasRole = userStore.userInfo?.roles?.includes(value)

    if (!hasRole) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  } else {
    throw new Error('请设置角色标签值')
  }
}

/**
 * v-auth 指令（基于权限码）
 * 用法: v-auth="'user:create'" 或 v-auth="['user:create', 'user:update']"
 */
export const auth: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkAuth(el, binding)
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkAuth(el, binding)
  },
}

function checkAuth(el: HTMLElement, binding: DirectiveBinding) {
  const userStore = useUserStore()
  const { value } = binding

  if (value && Array.isArray(value) && value.length > 0) {
    const permissions = value
    const hasPermission = permissions.some((perm: string) =>
      userStore.userInfo?.permissions?.includes(perm)
    )

    if (!hasPermission) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  } else if (typeof value === 'string') {
    const hasPermission = userStore.userInfo?.permissions?.includes(value)

    if (!hasPermission) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  } else {
    throw new Error('请设置权限码')
  }
}

/**
 * 注册所有自定义指令
 */
export function registerDirectives(app: any) {
  app.directive('permission', permission)
  app.directive('role', role)
  app.directive('auth', auth)
}
