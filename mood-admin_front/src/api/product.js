import request from '@/utils/request'

export function getProductList() {
  return request({
    url: '/admin/product/list',
    method: 'get'
  })
}

export function getProductById(id) {
  return request({
    url: `/admin/product/${id}`,
    method: 'get'
  })
}

export function addProduct(data) {
  return request({
    url: '/admin/product/add',
    method: 'post',
    data
  })
}

export function updateProduct(data) {
  return request({
    url: '/admin/product/update',
    method: 'put',
    data
  })
}

export function updateProductStatus(id, status) {
  return request({
    url: `/admin/product/status/${id}`,
    method: 'put',
    data: { status }
  })
}

export function restockProduct(data) {
  return request({
    url: '/admin/product/restock',
    method: 'post',
    data
  })
}

export function deleteProduct(id) {
  return request({
    url: `/admin/product/delete/${id}`,
    method: 'delete'
  })
}
