import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/order/lease/page',
    method: 'get',
    params
  })
}

export function getOrderById(id) {
  return request({
    url: `/order/lease/${id}`,
    method: 'get'
  })
}

export function addOrder(data) {
  return request({
    url: '/order/lease',
    method: 'post',
    data
  })
}

export function updateOrder(data) {
  return request({
    url: '/order/lease',
    method: 'put',
    data
  })
}

export function deleteOrder(id) {
  return request({
    url: `/order/lease/${id}`,
    method: 'delete'
  })
}

export function updateOrderStatus(id, status) {
  return request({
    url: `/order/lease/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function getMyOrderList(params) {
  return request({
    url: '/order/lease/my/page',
    method: 'get',
    params
  })
}

export function applyLease(data) {
  return request({
    url: '/order/lease/apply',
    method: 'post',
    data
  })
}
