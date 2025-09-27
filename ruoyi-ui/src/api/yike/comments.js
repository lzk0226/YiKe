import request from '@/utils/request'

// 查询评论列表
export function listComments(query) {
  return request({
    url: '/yike/comments/list',
    method: 'get',
    params: query
  })
}

// 查询评论详细
export function getComments(id) {
  return request({
    url: '/yike/comments/' + id,
    method: 'get'
  })
}

// 新增评论
export function addComments(data) {
  return request({
    url: '/yike/comments',
    method: 'post',
    data: data
  })
}

// 修改评论
export function updateComments(data) {
  return request({
    url: '/yike/comments',
    method: 'put',
    data: data
  })
}

// 删除评论
export function delComments(id) {
  return request({
    url: '/yike/comments/' + id,
    method: 'delete'
  })
}
