import request from '@/utils/request';

export function getRoutes() {
  return request({
    url: '/admin/role/routes',
    method: 'get'
  });
}

export function getRoles() {
  return request({
    url: '/admin/role',
    method: 'get'
  });
}

export function addRole(data) {
  return request({
    url: '/admin/role/add',
    method: 'post',
    data
  });
}

export function updateRole(id, data) {
  return request({
    url: `/admin/role/update/${id}`,
    method: 'post',
    data
  });
}

export function deleteRole(id) {
  return request({
    url: `/admin/role/delete${id}`,
    method: 'post'
  });
}
