import request from '@/utils/request'

// 查询笔记类型列表
export function listTypes(query) {
  return request({
    url: '/yike/types/list',
    method: 'get',
    params: query
  })
}

// 查询笔记类型详细
export function getTypes(id) {
  return request({
    url: '/yike/types/' + id,
    method: 'get'
  })
}

// 新增笔记类型
export function addTypes(data) {
  return request({
    url: '/yike/types',
    method: 'post',
    data: data
  })
}

// 修改笔记类型
export function updateTypes(data) {
  return request({
    url: '/yike/types',
    method: 'put',
    data: data
  })
}

// 删除笔记类型
export function delTypes(id) {
  return request({
    url: '/yike/types/' + id,
    method: 'delete'
  })
}
