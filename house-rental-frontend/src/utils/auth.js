const TokenKey = 'hr_token'
const UserInfoKey = 'hr_user_info'
const MenuKey = 'hr_menu'

export function getToken() {
  return localStorage.getItem(TokenKey) || ''
}

export function setToken(token) {
  return localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  return localStorage.removeItem(TokenKey)
}

export function getUserInfo() {
  const info = localStorage.getItem(UserInfoKey)
  return info ? JSON.parse(info) : {}
}

export function setUserInfo(userInfo) {
  return localStorage.setItem(UserInfoKey, JSON.stringify(userInfo))
}

export function removeUserInfo() {
  return localStorage.removeItem(UserInfoKey)
}

export function getMenu() {
  const menu = localStorage.getItem(MenuKey)
  return menu ? JSON.parse(menu) : []
}

export function setMenu(menu) {
  return localStorage.setItem(MenuKey, JSON.stringify(menu))
}

export function removeMenu() {
  return localStorage.removeItem(MenuKey)
}

export function clearAuth() {
  removeToken()
  removeUserInfo()
  removeMenu()
}
