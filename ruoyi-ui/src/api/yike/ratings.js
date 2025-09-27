import request from '@/utils/request'

// 查询笔记评分列表
export function listRatings(query) {
  return request({
    url: '/yike/ratings/list',
    method: 'get',
    params: query
  })
}

// 查询笔记评分详细
export function getRatings(id) {
  return request({
    url: '/yike/ratings/' + id,
    method: 'get'
  })
}

// 新增笔记评分
export function addRatings(data) {
  return request({
    url: '/yike/ratings',
    method: 'post',
    data: data
  })
}

// 修改笔记评分
export function updateRatings(data) {
  return request({
    url: '/yike/ratings',
    method: 'put',
    data: data
  })
}

// 删除笔记评分
export function delRatings(id) {
  return request({
    url: '/yike/ratings/' + id,
    method: 'delete'
  })
}
