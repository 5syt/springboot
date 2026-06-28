import request from '@/utils/request'

export function getAnnouncementList(params) {
  return request({
    url: '/announcement/page',
    method: 'get',
    params
  })
}

export function getAnnouncementById(id) {
  return request({
    url: `/announcement/${id}`,
    method: 'get'
  })
}

export function addAnnouncement(data) {
  return request({
    url: '/announcement',
    method: 'post',
    data
  })
}

export function updateAnnouncement(data) {
  return request({
    url: '/announcement',
    method: 'put',
    data
  })
}

export function deleteAnnouncement(id) {
  return request({
    url: `/announcement/${id}`,
    method: 'delete'
  })
}

export function updateAnnouncementStatus(id) {
  return request({
    url: `/announcement/${id}/status`,
    method: 'put'
  })
}
