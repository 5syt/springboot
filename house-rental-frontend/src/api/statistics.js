import request from '@/utils/request'

export function getStatistics() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

export function getHouseTypeStatistics() {
  return request({
    url: '/statistics/houseType',
    method: 'get'
  })
}

export function getOrderTrendStatistics() {
  return request({
    url: '/statistics/orderTrend',
    method: 'get'
  })
}
