import request from '@/utils/request'

export function getProfile() {
  return request({
    url: '/profile/info',
    method: 'get'
  })
}

export function updateProfile(data) {
  return request({
    url: '/profile/update',
    method: 'put',
    data
  })
}

export function updatePassword(data) {
  return request({
    url: '/profile/updatePwd',
    method: 'put',
    data
  })
}

export function uploadAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/profile/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
