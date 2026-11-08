import request from '@/utils/request'

export function getPostList() {
  return request({
    url: '/admin/post/list',
    method: 'get'
  })
}

export function getPostById(id) {
  return request({
    url: `/admin/post/${id}`,
    method: 'get'
  })
}

export function deletePost(id) {
  return request({
    url: `/admin/post/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeletePosts(ids) {
  return request({
    url: '/admin/post/batch-delete',
    method: 'post',
    data: { ids }
  })
}

export function getCommentList() {
  return request({
    url: '/admin/post/comment/list',
    method: 'get'
  })
}

export function getCommentById(id) {
  return request({
    url: `/admin/post/comment/${id}`,
    method: 'get'
  })
}

export function deleteComment(id) {
  return request({
    url: `/admin/post/comment/delete/${id}`,
    method: 'delete'
  })
}

export function batchDeleteComments(ids) {
  return request({
    url: '/admin/post/comment/batch-delete',
    method: 'post',
    data: { ids }
  })
}

// 申诉管理
export function getAppealList() {
  return request({
    url: '/admin/post/appeal/list',
    method: 'get'
  })
}

export function getAppealById(id) {
  return request({
    url: `/admin/post/appeal/${id}`,
    method: 'get'
  })
}

export function reviewAppeal(data) {
  return request({
    url: '/admin/post/appeal/review',
    method: 'post',
    data
  })
}

export function getAppealCount() {
  return request({
    url: '/admin/post/appeal/count',
    method: 'get'
  })
}
