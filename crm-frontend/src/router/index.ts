import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 配置NProgress
NProgress.configure({ showSpinner: false })

/**
 * 路由元数据接口
 */
interface RouteMeta {
  title?: string        // 页面标题
  icon?: string         // 菜单图标
  hidden?: boolean      // 是否在菜单中隐藏
  requiresAuth?: boolean // 是否需要认证
  roles?: string[]      // 允许访问的角色
}

/**
 * 路由配置
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
      requiresAuth: false,
    },
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    meta: {
      requiresAuth: true,
    },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: {
          title: '首页',
          icon: 'HomeFilled',
          requiresAuth: true,
        },
      },
    ],
  },
  // 404页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: {
      title: '页面不存在',
    },
  },
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

/**
 * 路由守卫
 * 未登录用户访问需要认证的页面时,重定向到登录页
 * 已登录用户访问登录页时,重定向到首页
 */
router.beforeEach((to, from, next) => {
  // 开始进度条
  NProgress.start()

  // 设置页面标题
  document.title = to.meta.title 
    ? `${to.meta.title} - 外贸CRM系统` 
    : '外贸CRM系统'

  // 获取token
  const token = localStorage.getItem('crm_token')

  if (to.meta.requiresAuth) {
    // 需要认证的页面
    if (token) {
      // 已登录,直接访问
      next()
    } else {
      // 未登录,重定向到登录页,并保存原目标路径
      next({
        path: '/login',
        query: { redirect: to.fullPath },
      })
    }
  } else {
    // 不需要认证的页面
    if (to.path === '/login' && token) {
      // 已登录用户访问登录页,重定向到首页
      next({ path: '/' })
    } else {
      next()
    }
  }
})

// 路由加载完成
router.afterEach(() => {
  NProgress.done()
})

export default router
