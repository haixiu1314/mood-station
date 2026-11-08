import request from '@/utils/request'

export function getOrderList() {
  return request({
    url: '/admin/order/list',
    method: 'get'
  })
}

export function getOrderById(id) {
  return request({
    url: `/admin/order/${id}`,
    method: 'get'
  })
}

export function shipOrder(id, expressInfo) {
  return request({
    url: `/admin/order/ship/${id}`,
    method: 'put',
    data: expressInfo
  })
}

export function refundOrder(id) {
  return request({
    url: `/admin/order/refund/${id}`,
    method: 'put'
  })
}

export function getPendingRefunds() {
  return request({
    url: '/admin/order/refund/pending',
    method: 'get'
  })
}

export function approveRefund(id) {
  return request({
    url: `/admin/order/refund/approve/${id}`,
    method: 'put'
  })
}

export function rejectRefund(id) {
  return request({
    url: `/admin/order/refund/reject/${id}`,
    method: 'put'
  })
}

export function updateExpressStatus(id, data) {
  return request({
    url: `/admin/order/express/${id}`,
    method: 'put',
    data
  })
}

export function getDashboardData(days = 7) {
  return request({
    url: `/admin/order/dashboard?days=${days}`,
    method: 'get'
  })
}

export function deleteOrder(id) {
  return request({
    url: `/admin/order/${id}`,
    method: 'delete'
  })
}
