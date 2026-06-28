import router from './router'
import store from './store'
import { getToken } from '@/utils/auth'
import { asyncRoutes, resetRouter } from '@/router'

const whiteList = ['/login', '/404']

router.beforeEach(async(to, from, next) => {
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const hasMenu = store.getters.menu && store.getters.menu.length > 0
      if (hasMenu) {
        next()
      } else {
        try {
          await store.dispatch('user/getMenu')
          const accessRoutes = asyncRoutes
          router.addRoutes(accessRoutes)
          router.addRoutes([{ path: '*', redirect: '/404', hidden: true }])
          next({ ...to, replace: true })
        } catch (error) {
          await store.dispatch('user/resetToken')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

router.afterEach(() => {
})
