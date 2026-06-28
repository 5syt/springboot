import router from './router'
import store from './store'
import { getToken } from '@/utils/auth'

const whiteList = ['/login', '/404']

router.beforeEach(async(to, from, next) => {
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const hasUserInfo = store.getters.userInfo && store.getters.userInfo.id
      if (hasUserInfo) {
        next()
      } else {
        try {
          await store.dispatch('user/getUserInfo')
          next()
        } catch (error) {
          console.error('获取用户信息失败:', error)
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

const originalPush = router.push
router.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
