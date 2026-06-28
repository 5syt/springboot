import request from '@/utils/request'

export function getHouseTypeList(params) {
  return request({
    url: '/house/type/page',
    method: 'get',
    params
  })
}

export function getHouseTypeAll() {
  return request({
    url: '/house/type/list',
    method: 'get'
  })
}

export function getHouseTypeById(id) {
  return request({
    url: `/house/type/${id}`,
    method: 'get'
  })
}

export function addHouseType(data) {
  return request({
    url: '/house/type',
    method: 'post',
    data
  })
}

export function updateHouseType(data) {
  return request({
    url: '/house/type',
    method: 'put',
    data
  })
}

export function deleteHouseType(id) {
  return request({
    url: `/house/type/${id}`,
    method: 'delete'
  })
}
