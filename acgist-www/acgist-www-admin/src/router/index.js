import Vue from 'vue';
import Layout from '@/layout';
import Router from 'vue-router';

Vue.use(Router);

export const constantRoutes = [
  {
    path: '/login',
    hidden: true,
    component: () => import('@/views/login/index')
  },

  {
    path: '/404',
    hidden: true,
    component: () => import('@/views/404')
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '面板',
      meta: { title: '面板', icon: 'dashboard' },
      component: () => import('@/views/dashboard/index')
    }]
  },

  {
    path: '/system/',
    name: '系统管理',
    meta: { title: '系统管理', icon: '.cog' },
    component: Layout,
    redirect: '/system/user',
    children: [
      {
        path: 'user',
        name: '系统用户',
        meta: { title: '系统用户', icon: '.user-tie' },
        component: () => import('@/views/system/user')
      },
      {
        path: 'role',
        name: '系统角色',
        meta: { title: '系统角色', icon: '.tree' },
        component: () => import('@/views/system/role')
      },
      {
        path: 'permission',
        name: '系统权限',
        meta: { title: '系统权限', icon: '.lock' },
        component: () => import('@/views/system/permission')
      },
      {
        path: 'status',
        name: '系统状态',
        meta: { title: '系统状态', icon: '.terminal' },
        component: () => import('@/views/system/permission')
      },
      {
        path: 'service',
        name: '服务管理',
        meta: { title: '服务管理', icon: '.equalizer' },
        component: () => import('@/views/system/permission')
      },
      {
        path: 'channel',
        name: '支付管理',
        meta: { title: '支付管理', icon: '.credit-card' },
        component: () => import('@/views/system/permission')
      },
      {
        path: 'cache',
        name: '缓存管理',
        meta: { title: '缓存管理', icon: '.drawer' },
        component: () => import('@/views/icons/index')
      },
      {
        path: 'gateway',
        name: '网关管理',
        meta: { title: '网关管理', icon: '.cloud' },
        component: () => import('@/views/system/permission')
      },
      {
        path: 'font',
        name: '字体管理',
        meta: { title: '字体管理', icon: '.font' },
        component: () => import('@/views/icons/index')
      }
    ]
  },

  {
    path: '/user/',
    name: '用户管理',
    meta: { title: '用户管理', icon: '.user' },
    component: Layout,
    redirect: '/user/list',
    children: [
      {
        path: 'list',
        name: '用户列表',
        meta: { title: '用户列表', icon: '.users' },
        component: () => import('@/views/system/user')
      },
      {
        path: 'role',
        name: '用户角色',
        meta: { title: '用户角色', icon: '.tree' },
        component: () => import('@/views/system/user')
      },
      {
        path: 'permission',
        name: '用户权限',
        meta: { title: '用户权限', icon: '.lock' },
        component: () => import('@/views/system/user')
      }
    ]
  },

  {
    path: '/order/',
    name: '订单管理',
    meta: { title: '订单管理', icon: '.bookmark' },
    component: Layout,
    redirect: '/order/list',
    children: [
      {
        path: 'list',
        name: '订单列表',
        meta: { title: '订单列表', icon: '.bookmarks' },
        component: () => import('@/views/system/user')
      },
      {
        path: 'business',
        name: '支付列表',
        meta: { title: '支付列表', icon: '.coin-yen' },
        component: () => import('@/views/system/user')
      }
    ]
  },

  {
    path: '/statistics/',
    name: '统计分析',
    meta: { title: '统计分析', icon: '.stats-bars' },
    component: Layout,
    redirect: '/statistics/order',
    children: [
      {
        path: 'user',
        name: '用户',
        meta: { title: '用户', icon: '.user' },
        component: () => import('@/views/system/user')
      },
      {
        path: 'order',
        name: '订单',
        meta: { title: '订单', icon: '.bookmark' },
        component: () => import('@/views/system/user')
      },
      {
        path: 'business',
        name: '支付',
        meta: { title: '支付', icon: '.credit-card' },
        component: () => import('@/views/system/user')
      }
    ]
  },

  {
    path: '/center/',
    component: Layout,
    redirect: '/center/index',
    children: [{
      path: 'index',
      name: '个人中心',
      meta: { title: '个人中心', icon: '.home' },
      component: () => import('@/views/dashboard/index')
    }]
  },

  {
    path: '*',
    redirect: '/404',
    hidden: true
  }

];

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
});

const router = createRouter();

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}

export default router;
