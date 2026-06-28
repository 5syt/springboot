const getters = {
  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  menu: state => state.user.menu,
  roles: state => state.user.roles || [],
  sidebarCollapsed: state => state.app.sidebarCollapsed,
  isLogin: state => !!state.user.token,
  username: state => (state.user.userInfo && state.user.userInfo.username) || '',
  isAdmin: state => {
    const roles = state.user.roles || []
    return roles.includes('ADMIN')
  }
}

export default getters
