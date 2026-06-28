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
        meta: { title: '首页', icon: 'el-icon-s-home' }
      }
    ]
  }
]

export const asyncRoutes = [
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'el-icon-s-tools' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'el-icon-user' }
      }
    ]
  },
  {
    path: '/house',
    component: Layout,
    redirect: '/house/type',
    meta: { title: '房屋管理', icon: 'el-icon-office-building' },
    children: [
      {
        path: 'type',
        name: 'HouseType',
        component: () => import('@/views/house/type/index.vue'),
        meta: { title: '房屋类型', icon: 'el-icon-menu' }
      },
      {
        path: 'info',
        name: 'HouseInfo',
        component: () => import('@/views/house/info/index.vue'),
        meta: { title: '房屋信息', icon: 'el-icon-house' }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/lease',
    meta: { title: '订单管理', icon: 'el-icon-document' },
    children: [
      {
        path: 'lease',
        name: 'LeaseOrder',
        component: () => import('@/views/order/lease/index.vue'),
        meta: { title: '租赁订单', icon: 'el-icon-s-order' }
      }
    ]
  },
  {
    path: '/announcement',
    component: Layout,
    redirect: '/announcement/list',
    meta: { title: '公告管理', icon: 'el-icon-bell' },
    children: [
      {
        path: 'list',
        name: 'AnnouncementList',
        component: () => import('@/views/announcement/list/index.vue'),
        meta: { title: '公告列表', icon: 'el-icon-message' }
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
        meta: { title: '个人中心', icon: 'el-icon-user-solid' }
      }
    ]
  }
]

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
