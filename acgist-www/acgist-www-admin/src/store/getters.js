const getters = {
  sidebar: state => state.app.sidebar,
  avatar: state => state.user.avatar || '/logo.png',
  device: state => state.app.device,
  token: state => state.user.token,
  name: state => state.user.name
};

export default getters;
