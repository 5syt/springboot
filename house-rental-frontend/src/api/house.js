import request from '@/utils/request'

export function getHouseList(params) {
  return request({
    url: '/house/info/page',
    method: 'get',
    params
  })
}

export function getHouseById(id) {
  return request({
    url: `/house/info/${id}`,
    method: 'get'
  })
}

export function addHouse(data) {
  return request({
    url: '/house/info',
    method: 'post',
    data
  })
}

export function updateHouse(data) {
  return request({
    url: '/house/info',
    method: 'put',
    data
  })
}

export function deleteHouse(id) {
  return request({
    url: `/house/info/${id}`,
    method: 'delete'
  })
}

export function updateHouseStatus(id) {
  return request({
    url: `/house/info/${id}/status`,
    method: 'put'
  })
}

export function uploadHouseImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/house/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
