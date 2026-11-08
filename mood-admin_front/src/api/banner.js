import request from '@/utils/request'

export function getBannerList() {
  return request({
    url: '/admin/banner/list',
    method: 'get'
  })
}

export function getBannerById(id) {
  return request({
    url: `/admin/banner/${id}`,
    method: 'get'
  })
}

export function addBanner(data) {
  return request({
    url: '/admin/banner/add',
    method: 'post',
    data
  })
}

export function updateBanner(data) {
  return request({
    url: '/admin/banner/update',
    method: 'put',
    data
  })
}

export function deleteBanner(id) {
  return request({
    url: `/admin/banner/delete/${id}`,
    method: 'delete'
  })
}

export function updateBannerStatus(id, status) {
  return request({
    url: `/admin/banner/status/${id}`,
    method: 'put',
    data: { isActive: status }
  })
}

export function uploadBannerImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/banner/upload',
    method: 'post',
    data: formData
  })
}
