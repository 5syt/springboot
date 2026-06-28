const getters = {
  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  menu: state => state.user.menu,
  sidebarCollapsed: state => state.app.sidebarCollapsed,
  isLogin: state => !!state.user.token,
  username: state => state.user.userInfo?.username || ''
}

export default getters
