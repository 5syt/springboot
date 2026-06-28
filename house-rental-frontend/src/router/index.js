import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/views/layout/index.vue'

Vue.use(VueRouter)

export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    hidden: true
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404.vue'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'el-icon-s-home', roles: ['ADMIN', 'USER'] }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'el-icon-s-tools', roles: ['ADMIN'] },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'el-icon-user', roles: ['ADMIN'] }
      }
    ]
  },
  {
    path: '/house',
    component: Layout,
    redirect: '/house/type',
    meta: { title: '房屋管理', icon: 'el-icon-office-building', roles: ['ADMIN'] },
    children: [
      {
        path: 'type',
        name: 'HouseType',
        component: () => import('@/views/house/type/index.vue'),
        meta: { title: '房屋类型', icon: 'el-icon-menu', roles: ['ADMIN'] }
      },
      {
        path: 'info',
        name: 'HouseInfo',
        component: () => import('@/views/house/info/index.vue'),
        meta: { title: '房屋信息', icon: 'el-icon-house', roles: ['ADMIN'] }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/lease',
    meta: { title: '订单管理', icon: 'el-icon-document', roles: ['ADMIN'] },
    children: [
      {
        path: 'lease',
        name: 'LeaseOrder',
        component: () => import('@/views/order/lease/index.vue'),
        meta: { title: '租赁订单', icon: 'el-icon-s-order', roles: ['ADMIN'] }
      }
    ]
  },
  {
    path: '/announcement',
    component: Layout,
    redirect: '/announcement/list',
    meta: { title: '公告管理', icon: 'el-icon-bell', roles: ['ADMIN'] },
    children: [
      {
        path: 'list',
        name: 'AnnouncementList',
        component: () => import('@/views/announcement/list/index.vue'),
        meta: { title: '公告列表', icon: 'el-icon-message', roles: ['ADMIN'] }
      }
    ]
  },
  {
    path: '/house-view',
    component: Layout,
    redirect: '/house-view/list',
    meta: { title: '房屋浏览', icon: 'el-icon-house', roles: ['USER'] },
    children: [
      {
        path: 'list',
        name: 'HouseViewList',
        component: () => import('@/views/house/info/index.vue'),
        meta: { title: '房屋列表', icon: 'el-icon-house', roles: ['USER'] }
      }
    ]
  },
  {
    path: '/my-order',
    component: Layout,
    redirect: '/my-order/list',
    meta: { title: '我的订单', icon: 'el-icon-s-order', roles: ['USER'] },
    children: [
      {
        path: 'list',
        name: 'MyOrderList',
        component: () => import('@/views/order/lease/index.vue'),
        meta: { title: '我的订单', icon: 'el-icon-s-order', roles: ['USER'] }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'el-icon-user-solid', roles: ['ADMIN', 'USER'] }
      }
    ]
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

export const asyncRoutes = []

const createRouter = () => new VueRouter({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
