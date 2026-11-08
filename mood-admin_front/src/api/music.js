import request from '@/utils/request'

export function getMusicList() {
  return request({
    url: '/admin/music/list',
    method: 'get'
  })
}

export function getMusicById(id) {
  return request({
    url: `/admin/music/${id}`,
    method: 'get'
  })
}

export function addMusic(data) {
  return request({
    url: '/admin/music/add',
    method: 'post',
    data
  })
}

export function updateMusic(data) {
  return request({
    url: '/admin/music/update',
    method: 'put',
    data
  })
}

export function deleteMusic(id) {
  return request({
    url: `/admin/music/delete/${id}`,
    method: 'delete'
  })
}

export function uploadMusicFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/music/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}