import request from '@/utils/request'

// 查询用户关注列表
export function listFollows(query) {
  return request({
    url: '/yike/follows/list',
    method: 'get',
    params: query
  })
}

// 查询用户关注详细
export function getFollows(id) {
  return request({
    url: '/yike/follows/' + id,
    method: 'get'
  })
}

// 新增用户关注
export function addFollows(data) {
  return request({
    url: '/yike/follows',
    method: 'post',
    data: data
  })
}

// 修改用户关注
export function updateFollows(data) {
  return request({
    url: '/yike/follows',
    method: 'put',
    data: data
  })
}

// 删除用户关注
export function delFollows(id) {
  return request({
    url: '/yike/follows/' + id,
    method: 'delete'
  })
}
