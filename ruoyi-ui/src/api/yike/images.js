import request from '@/utils/request'

// 查询笔记图片列表
export function listImages(query) {
  return request({
    url: '/yike/images/list',
    method: 'get',
    params: query
  })
}

// 查询笔记图片详细
export function getImages(id) {
  return request({
    url: '/yike/images/' + id,
    method: 'get'
  })
}

// 新增笔记图片
export function addImages(data) {
  return request({
    url: '/yike/images',
    method: 'post',
    data: data
  })
}

// 修改笔记图片
export function updateImages(data) {
  return request({
    url: '/yike/images',
    method: 'put',
    data: data
  })
}

// 删除笔记图片
export function delImages(id) {
  return request({
    url: '/yike/images/' + id,
    method: 'delete'
  })
}
