import request from '@/utils/request'

export function getHouseList(params) {
  return request({
    url: '/house/list',
    method: 'get',
    params
  })
}

export function getHouseById(id) {
  return request({
    url: `/house/${id}`,
    method: 'get'
  })
}

export function addHouse(data) {
  return request({
    url: '/house',
    method: 'post',
    data
  })
}

export function updateHouse(data) {
  return request({
    url: '/house',
    method: 'put',
    data
  })
}

export function deleteHouse(id) {
  return request({
    url: `/house/${id}`,
    method: 'delete'
  })
}

export function updateHouseStatus(id, status) {
  return request({
    url: `/house/status/${id}`,
    method: 'put',
    params: { status }
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
