import request from '@/utils/request'

export function getFeedbackList() {
  return request({
    url: '/admin/feedback/list',
    method: 'get'
  })
}

export function getFeedbackById(id) {
  return request({
    url: `/admin/feedback/${id}`,
    method: 'get'
  })
}
