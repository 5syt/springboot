import request from '@/utils/request'

export function getStatistics() {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

export function getHouseTypeStatistics() {
  return request({
    url: '/statistics/houseTypePie',
    method: 'get'
  })
}

export function getOrderTrendStatistics() {
  return request({
    url: '/statistics/orderTrend',
    method: 'get'
  })
}
