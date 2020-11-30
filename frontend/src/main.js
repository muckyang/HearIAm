import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';

import firebase from 'firebase/app'
import '@firebase/messaging'
import configset from './fcm/firebaseConfig'
// import { mapMutations } from "vuex";
import http from "@/util/http-common.js";

firebase.initializeApp(configset);

Notification.requestPermission()
    .then((permission) => {
        if (permission !== 'granted') {
            alert('알림을 허용해주세요');
        }
    })

const messaging = firebase.messaging();
messaging.usePublicVapidKey('BCslFJO_va9K_9Qeo8HbYWjwI2rg4CrmcfNt5Uy0ffzDfoLFSB0bug-lxfTyfdRXCxskYgtXTxfh2xAux9kSbRs');

messaging.getToken().then((currentToken) => {
    if (currentToken) {
        // console.log("currentToken : "+currentToken);
        store.commit('setDeviceId', currentToken);
    } else {
        console.log('No Instance ID token available. Request permission to generate one.');
    }
});

messaging.onMessage((payload) => {
    const title = payload.data.title;
    const options = {
        body: payload.data.body,
        icon: './logo.png',
    };
    const notification = new Notification(title, options);
    notification.onclick = function(event) {
        event.preventDefault(); // prevent the browser from focusing the Notification's tab
        let num = payload.data.room_num * 1;
        console.dir(payload);
        console.dir(payload.data.room);
        let mentorNum = store.getters['getUserNum'];
        http
          .get(`/counseling/isRoom/${mentorNum}/${num}`)
          .then((res) => {
            if (res.data == "fail") {
              alert("이미 상담 중입니다. 다른 학생과 상담하세요.");
              router.push("/");
            } else {             
              router.push(
                `/counselorWRTC/${payload.data.room}&${num}`
              );
            }
          })
          .catch((e) => {
            console.log(e);
          });

        console.log(num);
        // if (num > 0) { //실시간 상담
        //     router.push({ name: 'stMatch', params: { room: payload.data.room, room_num: num } });
        // }
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