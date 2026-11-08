import request from '@/utils/request'

export function getShippedOrders() {
  return request({
    url: '/admin/express/shipped',
    method: 'get'
  })
}

export function updateExpressStatus(id, data) {
  return request({
    url: `/admin/express/status/${id}`,
    method: 'put',
    data
  })
}

export function refundOrder(id) {
  return request({
    url: `/admin/express/refund/${id}`,
    method: 'put'
  })
}

export function getExpressCompanies() {
  return request({
    url: '/admin/express/companies',
    method: 'get'
  })
}
