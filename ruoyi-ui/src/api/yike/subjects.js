import request from '@/utils/request'

// 查询学科分类列表
export function listSubjects(query) {
  return request({
    url: '/yike/subjects/list',
    method: 'get',
    params: query
  })
}

// 查询学科分类详细
export function getSubjects(id) {
  return request({
    url: '/yike/subjects/' + id,
    method: 'get'
  })
}

// 新增学科分类
export function addSubjects(data) {
  return request({
    url: '/yike/subjects',
    method: 'post',
    data: data
  })
}

// 修改学科分类
export function updateSubjects(data) {
  return request({
    url: '/yike/subjects',
    method: 'put',
    data: data
  })
}

// 删除学科分类
export function delSubjects(id) {
  return request({
    url: '/yike/subjects/' + id,
    method: 'delete'
  })
}
