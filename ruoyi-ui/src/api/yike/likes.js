import request from '@/utils/request'

// 查询用户点赞记录列表
export function listLikes(query) {
  return request({
    url: '/yike/likes/list',
    method: 'get',
    params: query
  })
}

// 查询用户点赞记录详细
export function getLikes(id) {
  return request({
    url: '/yike/likes/' + id,
    method: 'get'
  })
}

// 新增用户点赞记录
export function addLikes(data) {
  return request({
    url: '/yike/likes',
    method: 'post',
    data: data
  })
}

// 修改用户点赞记录
export function updateLikes(data) {
  return request({
    url: '/yike/likes',
    method: 'put',
    data: data
  })
}

// 删除用户点赞记录
export function delLikes(id) {
  return request({
    url: '/yike/likes/' + id,
    method: 'delete'
  })
}
