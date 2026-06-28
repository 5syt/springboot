import { login as loginApi, getUserInfo as getUserInfoApi, logout as logoutApi, getMenu as getMenuApi } from '@/api/auth'
import { setToken, getToken, removeToken, setUserInfo, getUserInfo, removeUserInfo, setMenu, getMenu, removeMenu, clearAuth } from '@/utils/auth'

const state = {
  token: getToken(),
  userInfo: getUserInfo(),
  menu: getMenu()
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
    setToken(token)
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
    setUserInfo(userInfo)
  },
  SET_MENU: (state, menu) => {
    state.menu = menu
    setMenu(menu)
  },
  CLEAR_AUTH: (state) => {
    state.token = ''
    state.userInfo = {}
    state.menu = []
    clearAuth()
  }
}

const actions = {
  login({ commit }, loginForm) {
    return new Promise((resolve, reject) => {
      loginApi(loginForm).then(res => {
        const { token, userInfo } = res.data
        commit('SET_TOKEN', token)
        commit('SET_USER_INFO', userInfo)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getUserInfoApi().then(res => {
        commit('SET_USER_INFO', res.data)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getMenu({ commit }) {
    return new Promise((resolve, reject) => {
      getMenuApi().then(res => {
        commit('SET_MENU', res.data || [])
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  logout({ commit }) {
    return new Promise((resolve, reject) => {
      logoutApi().then(() => {
        commit('CLEAR_AUTH')
        resolve()
      }).catch(() => {
        commit('CLEAR_AUTH')
        resolve()
      })
    })
  },
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('CLEAR_AUTH')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
