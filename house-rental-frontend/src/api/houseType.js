import request from '@/utils/request'

export function getHouseTypeList(params) {
  return request({
    url: '/houseType/list',
    method: 'get',
    params
  })
}

export function getHouseTypeAll() {
  return request({
    url: '/houseType/all',
    method: 'get'
  })
}

export function getHouseTypeById(id) {
  return request({
    url: `/houseType/${id}`,
    method: 'get'
  })
}

export function addHouseType(data) {
  return request({
    url: '/houseType',
    method: 'post',
    data
  })
}

export function updateHouseType(data) {
  return request({
    url: '/houseType',
    method: 'put',
    data
  })
}

export function deleteHouseType(id) {
  return request({
    url: `/houseType/${id}`,
    method: 'delete'
  })
}
