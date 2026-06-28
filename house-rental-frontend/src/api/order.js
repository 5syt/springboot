import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

export function getOrderById(id) {
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

export function addOrder(data) {
  return request({
    url: '/order',
    method: 'post',
    data
  })
}

export function updateOrder(data) {
  return request({
    url: '/order',
    method: 'put',
    data
  })
}

export function deleteOrder(id) {
  return request({
    url: `/order/${id}`,
    method: 'delete'
  })
}

export function updateOrderStatus(id, status) {
  return request({
    url: `/order/status/${id}`,
    method: 'put',
    params: { status }
  })
}
