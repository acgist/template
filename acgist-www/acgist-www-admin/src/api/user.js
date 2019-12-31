import request from '@/utils/request';

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  });
}

export function getPermission(token) {
  return request({
    url: '/admin/permission',
    method: 'get',
    params: { token }
  });
}

export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  });
}
