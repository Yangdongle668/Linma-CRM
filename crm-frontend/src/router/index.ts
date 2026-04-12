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
        component: () => import('@/views/dashboard/index-enhanced.vue'),
        meta: {
          title: '首页',
          icon: 'HomeFilled',
          requiresAuth: true,
        },
      },
      // 任务管理
      {
        path: 'task/list',
        name: 'TaskList',
        component: () => import('@/views/task/list.vue'),
        meta: {
          title: '任务列表',
          requiresAuth: true,
        },
      },
      // 客户管理
      {
        path: 'customer/list',
        name: 'CustomerList',
        component: () => import('@/views/customer/list.vue'),
        meta: {
          title: '客户列表',
          requiresAuth: true,
        },
      },
      {
        path: 'customer/detail/:id',
        name: 'CustomerDetail',
        component: () => import('@/views/customer/detail.vue'),
        meta: {
          title: '客户详情',
          requiresAuth: true,
        },
      },
      {
        path: 'customer/follow',
        name: 'CustomerFollow',
        component: () => import('@/views/customer/follow.vue'),
        meta: {
          title: '跟进记录',
          requiresAuth: true,
        },
      },
      // 产品管理
      {
        path: 'product/list',
        name: 'ProductList',
        component: () => import('@/views/product/list.vue'),
        meta: {
          title: '产品列表',
          requiresAuth: true,
        },
      },
      {
        path: 'product/category',
        name: 'ProductCategory',
        component: () => import('@/views/product/category.vue'),
        meta: {
          title: '产品分类',
          requiresAuth: true,
        },
      },
      // 询盘管理
      {
        path: 'inquiry/list',
        name: 'InquiryList',
        component: () => import('@/views/inquiry/list.vue'),
        meta: {
          title: '询盘列表',
          requiresAuth: true,
        },
      },
      {
        path: 'inquiry/reply',
        name: 'InquiryReply',
        component: () => import('@/views/inquiry/reply.vue'),
        meta: {
          title: '询盘回复',
          requiresAuth: true,
        },
      },
      // 报价单管理
      {
        path: 'quotation/list',
        name: 'QuotationList',
        component: () => import('@/views/quotation/list.vue'),
        meta: {
          title: '报价单列表',
          requiresAuth: true,
        },
      },
      // 订单管理
      {
        path: 'order/list',
        name: 'OrderList',
        component: () => import('@/views/order/list.vue'),
        meta: {
          title: '订单列表',
          requiresAuth: true,
        },
      },
      // 合同管理
      {
        path: 'contract/list',
        name: 'ContractList',
        component: () => import('@/views/contract/list.vue'),
        meta: {
          title: '合同列表',
          requiresAuth: true,
        },
      },
      {
        path: 'contract/template',
        name: 'ContractTemplate',
        component: () => import('@/views/contract/template.vue'),
        meta: {
          title: '合同模板',
          requiresAuth: true,
        },
      },
      // 物流管理
      {
        path: 'shipping/list',
        name: 'ShippingList',
        component: () => import('@/views/shipping/list.vue'),
        meta: {
          title: '发货单',
          requiresAuth: true,
        },
      },
      {
        path: 'shipping/tracking',
        name: 'ShippingTracking',
        component: () => import('@/views/shipping/tracking.vue'),
        meta: {
          title: '物流追踪',
          requiresAuth: true,
        },
      },
      // 财务管理
      {
        path: 'finance/receivable',
        name: 'FinanceReceivable',
        component: () => import('@/views/finance/receivable.vue'),
        meta: {
          title: '应收账款',
          requiresAuth: true,
        },
      },
      {
        path: 'finance/payable',
        name: 'FinancePayable',
        component: () => import('@/views/finance/payable.vue'),
        meta: {
          title: '应付账款',
          requiresAuth: true,
        },
      },
      {
        path: 'finance/profit',
        name: 'FinanceProfit',
        component: () => import('@/views/finance/profit.vue'),
        meta: {
          title: '利润分析',
          requiresAuth: true,
        },
      },
      // 数据分析
      {
        path: 'analytics/dashboard',
        name: 'AnalyticsDashboard',
        component: () => import('@/views/analytics/dashboard.vue'),
        meta: {
          title: '数据看板',
          requiresAuth: true,
        },
      },
      {
        path: 'analytics/funnel',
        name: 'AnalyticsFunnel',
        component: () => import('@/views/analytics/funnel.vue'),
        meta: {
          title: '销售漏斗',
          requiresAuth: true,
        },
      },
      {
        path: 'analytics/ranking',
        name: 'AnalyticsRanking',
        component: () => import('@/views/analytics/ranking.vue'),
        meta: {
          title: '业绩排行',
          requiresAuth: true,
        },
      },
      // 消息中心
      {
        path: 'message/email',
        name: 'MessageEmail',
        component: () => import('@/views/message/email.vue'),
        meta: {
          title: '邮箱客户端',
          requiresAuth: true,
        },
      },
      {
        path: 'message/template',
        name: 'MessageTemplate',
        component: () => import('@/views/message/template.vue'),
        meta: {
          title: '邮件模板',
          requiresAuth: true,
        },
      },
      {
        path: 'message/notification',
        name: 'MessageNotification',
        component: () => import('@/views/message/notification.vue'),
        meta: {
          title: '通知中心',
          requiresAuth: true,
        },
      },
      // 系统设置
      {
        path: 'system/user',
        name: 'SystemUser',
        component: () => import('@/views/system/user.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true,
        },
      },
      {
        path: 'system/role',
        name: 'SystemRole',
        component: () => import('@/views/system/role.vue'),
        meta: {
          title: '角色管理',
          requiresAuth: true,
        },
      },
      {
        path: 'system/config',
        name: 'SystemConfig',
        component: () => import('@/views/system/config.vue'),
        meta: {
          title: '系统配置',
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
