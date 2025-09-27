import request from '@/utils/request'

// 查询笔记列表
export function listNotes(query) {
  return request({
    url: '/yike/notes/list',
    method: 'get',
    params: query
  })
}

// 查询笔记详细
export function getNotes(id) {
  return request({
    url: '/yike/notes/' + id,
    method: 'get'
  })
}

// 新增笔记
export function addNotes(data) {
  return request({
    url: '/yike/notes',
    method: 'post',
    data: data
  })
}

// 修改笔记
export function updateNotes(data) {
  return request({
    url: '/yike/notes',
    method: 'put',
    data: data
  })
}

// 删除笔记
export function delNotes(id) {
  return request({
    url: '/yike/notes/' + id,
    method: 'delete'
  })
}
