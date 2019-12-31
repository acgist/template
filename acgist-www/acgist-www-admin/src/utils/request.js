import axios from 'axios';
import store from '@/store';
import { getToken } from '@/utils/auth';
import { MessageBox, Message } from 'element-ui';

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 5000
});

service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['X-Token'] = getToken();
    }
    return config;
  },
  error => {
    console.log('请求失败：' + error);
    if (error && error.response) {
      switch (error.response.status) {
        case 404:
          error.message = '请求错误：未找到该资源';
          break;
        default:
          error.message = `请求错误：${error.response.status}`;
          break;
      }
    } else {
      error.message = '连接到服务器失败';
    }
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== '0000') {
      Message({
        type: 'error',
        message: res.message || '系统异常',
        duration: 5 * 1000
      });
      if (res.code === 2000 || res.code === 2001) {
        MessageBox.confirm('没有权限', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload();
          });
        });
      }
      return Promise.reject(new Error(res.message || '系统异常'));
    } else {
      return res;
    }
  },
  error => {
    console.log('错误信息：' + error);
    Message({
      type: 'error',
      message: error.message,
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service;
