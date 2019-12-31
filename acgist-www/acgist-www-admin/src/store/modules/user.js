import { resetRouter } from '@/router';
import { login, logout, getPermission } from '@/api/user';
import { getToken, setToken, removeToken } from '@/utils/auth';

const getDefaultState = () => {
  return {
    name: '',
    token: getToken(),
    avatar: ''
  };
};

const state = getDefaultState();

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState());
  },
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  }
};

const actions = {
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response;
        commit('SET_TOKEN', data.token);
        setToken(data.token);
        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },

  getPermission({ commit, state }) {
    return new Promise((resolve, reject) => {
      getPermission(state.token).then(response => {
        const { data } = response;
        if (!data) {
          reject('权限验证失败（重新登陆）');
        }
        const { name, avatar } = data;
        commit('SET_NAME', name);
        commit('SET_AVATAR', avatar);
        resolve(data);
      }).catch(error => {
        reject(error);
      });
    });
  },

  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken();
        resetRouter();
        commit('RESET_STATE');
        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },

  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken();
      commit('RESET_STATE');
      resolve();
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
