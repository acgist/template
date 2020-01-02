import Vue from 'vue';
import App from './App';
import store from './store';
import router from './router';
import ElementUI from 'element-ui';
// import locale from 'element-ui/lib/locale/lang/en';

import '@/icons';
import '@/permission';
import '@/assets/font.css';
import '@/styles/index.scss';
import 'normalize.css/normalize.css';
import 'element-ui/lib/theme-chalk/index.css';

import { mockXHR } from '../mock';
// TODO：删除NODE_ENV和mock
if (process.env.NODE_ENV === 'production') {
  mockXHR();
}

Vue.use(ElementUI); // 中文
// Vue.use(ElementUI, { locale }); // 英文

Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
});
