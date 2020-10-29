import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';

import firebase from 'firebase/app'
import '@firebase/messaging'
import configset from './fcm/firebaseConfig'
// import { mapMutations } from "vuex";

firebase.initializeApp(configset);

Notification.requestPermission()
.then((permission) => {
  console.log('permission ', permission)
  if (permission !== 'granted') {
    alert('알림을 허용해주세요')
  }
})

const messaging = firebase.messaging();
messaging.usePublicVapidKey('BCslFJO_va9K_9Qeo8HbYWjwI2rg4CrmcfNt5Uy0ffzDfoLFSB0bug-lxfTyfdRXCxskYgtXTxfh2xAux9kSbRs');

messaging.getToken().then((currentToken) => {
    if (currentToken) {
        console.log("currentToken : "+currentToken);
        // token.value = currentToken;
        store.commit('setDeviceId',currentToken);
    } else {
        // Show permission request.
        console.log('No Instance ID token available. Request permission to generate one.');
    }
});

messaging.onMessage((payload) => {
    console.log("main.js - payload : "+payload+" "+payload.data+" "+payload.data.title);
    const title = payload.data.title;
    const options = {
        body: payload.data.body,
        icon: './logo.png',
    };
    const notification = new Notification(title, options);
    notification.onclick = function(event) {
      event.preventDefault(); // prevent the browser from focusing the Notification's tab
      // window.open('http://localhost:8080/liveList');
      window.open('https://k3b201.p.ssafy.io/liveList');
    }
    return notification;
});

Vue.config.productionTip = false
new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
