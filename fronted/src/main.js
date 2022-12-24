import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import JwChat from 'jwchat'
// import 'jwchat/lib/JwChat.css';

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(JwChat)

new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
