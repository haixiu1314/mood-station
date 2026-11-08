import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function getUserList() {
  return request({
    url: '/admin/user/list',
    method: 'get'
  })
}

export function getUserById(id) {
  return request({
    url: `/admin/user/${id}`,
    method: 'get'
  })
}

export function banUser(id) {
  return request({
    url: `/admin/user/ban/${id}`,
    method: 'put'
  })
}

export function unbanUser(id) {
  return request({
    url: `/admin/user/unban/${id}`,
    method: 'put'
  })
}

export function deleteUser(id) {
  return request({
    url: `/admin/user/delete/${id}`,
    method: 'delete'
  })
}

export function giftCoins(data) {
  return request({
    url: '/admin/user/gift-coins',
    method: 'post',
    data
  })
}
