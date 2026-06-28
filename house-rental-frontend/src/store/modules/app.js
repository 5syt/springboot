const state = {
  sidebarCollapsed: localStorage.getItem('sidebarCollapsed') === 'true'
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebarCollapsed = !state.sidebarCollapsed
    localStorage.setItem('sidebarCollapsed', state.sidebarCollapsed)
  },
  SET_SIDEBAR_COLLAPSED: (state, collapsed) => {
    state.sidebarCollapsed = collapsed
    localStorage.setItem('sidebarCollapsed', collapsed)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  setSideBarCollapsed({ commit }, collapsed) {
    commit('SET_SIDEBAR_COLLAPSED', collapsed)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
