import request from '@/utils/request'

// 查询用户收藏列表
export function listFavorites(query) {
  return request({
    url: '/yike/favorites/list',
    method: 'get',
    params: query
  })
}

// 查询用户收藏详细
export function getFavorites(id) {
  return request({
    url: '/yike/favorites/' + id,
    method: 'get'
  })
}

// 新增用户收藏
export function addFavorites(data) {
  return request({
    url: '/yike/favorites',
    method: 'post',
    data: data
  })
}

// 修改用户收藏
export function updateFavorites(data) {
  return request({
    url: '/yike/favorites',
    method: 'put',
    data: data
  })
}

// 删除用户收藏
export function delFavorites(id) {
  return request({
    url: '/yike/favorites/' + id,
    method: 'delete'
  })
}
