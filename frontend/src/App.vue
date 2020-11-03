<template>
  <v-app>
    <v-app-bar
      v-if="getRole == `mentee`"
      absolute
      flat
      style="background-color: rgba(255, 255, 255, 0)"
    >
      <v-btn text dark @click="goHome()"><h2>Hear I Am</h2></v-btn>
      <v-spacer></v-spacer>
      <v-btn text dark @click="goLive()">1:1 상담</v-btn>
      <v-btn text dark @click="goRecord()">음성 상담</v-btn>
      <v-btn text dark @click="goReserve()">상담 예약</v-btn>
      <v-btn text dark @click="goMypage()">마이페이지</v-btn>
      <v-btn text dark @click="logout()">로그아웃</v-btn>
    </v-app-bar>
    <v-app-bar
      v-if="getRole == `mentor`"
      absolute
      flat
      style="background-color: rgba(255, 255, 255, 0)"
    >
      <v-btn text dark @click="goHome()"><h2>Hear I Am</h2></v-btn>
      <v-spacer></v-spacer>
      <v-btn text dark @click="subscribe()">1:1 상담 현황</v-btn>
      <v-btn text dark @click="goRecordList()">음성 상담 현황</v-btn>
      <v-btn text dark @click="goMyMenteeList()">일지 관리</v-btn>
      <v-btn text dark @click="goMypage()">마이페이지</v-btn>
      <v-btn text dark @click="logout()">로그아웃</v-btn>
    </v-app-bar>
    <v-main>
      <router-view class="main-back" style="padding-top: 64px" />
    </v-main>
    <v-speed-dial
      v-model="fab"
      bottom
      right
      fixed
      direction="top"
      transition="slide-x-reverse-transition"
    >
      <template v-slot:activator>
        <v-btn v-model="fab" color="#0a7a78" dark fab>
          <v-icon v-if="fab">mdi-close </v-icon>
          <v-icon v-else> mdi-bell </v-icon>
        </v-btn>
      </template>
      <v-card min-width="300" style="margin-right:230px; text-align:center;">
        <v-list dense>
          <v-header>알람</v-header>
          <v-list-item-group v-model="selectedItem" color="primary">
            <v-list-item>
              <v-list-item-icon>
                <v-icon>mdi-bell-alert</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>상담요청이 왔어요!</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-icon>
                <v-icon>mdi-bell-alert</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>주문하신 상품이 배송되었어요!</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card>
    </v-speed-dial>
  </v-app>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import { AUTH_LOGOUT } from "@/store/actions/auth";
import http from "@/util/http-common.js";
import axios from "axios";

export default {
  name: "App",
  components: {},
  data() {
    return {
      fab: false,
    };
  },
  methods: {
    logout: function () {
      this.unsubscribe();
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.drawer = false;
      });
      // this.$router.push("/").catch(() => {});
      window.location.href="/";
    },
    goHome() {
      this.$router.push("/").catch(() => {});
    },
    goMypage() {
      if (this.getRole == `mentee`) {
        this.$router.push(`/menteeMypage`).catch(() => {});
      } else if (this.getRole == `mentor`) {
        this.$router.push(`/mentorMypage`).catch(() => {});
      }
    },
    goLive() {
      http
        .get(`/counseling/isMentee`)
        .then((res) => {
          console.dir(res);
          console.log(res.data);
          if (res.data == 0) {
            alert("대기중인 멘토가 없어요! 예약하기를 이용해주세요! ");
          } else {
            this.$router.push(`/userWRTC`);
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    goReserve() {
      this.$router.push(`/reserveMain`).catch(() => {});
    },
    goRecord() {
      this.$router.push("/recordConsult/1").catch(() => {});
    },
    goMyMenteeList() {
      this.$router.push(`/myMenteeList`);
    },
    goRecordList() {
      this.$router.push(`/recordList`);
    },
    subscribe() {
      console.log("click subscribe btn");
      this.subscribeTokenToTopic(this.devecieId, this.topic);
      // let mentorname = this.$store.getters['getUserNum'];
      http.get(`/counseling/addReady/${this.getUserNum}`).then((res)=>{
        console.log("add ready success : "+res)
      });
    },
    subscribeTokenToTopic(token, topic) {
      axios({
        method: "POST",
        url:
          "https://iid.googleapis.com/iid/v1/" + token + "/rel/topics/" + topic,
        headers: {
          Authorization:
            "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd",
        },
      })
        .then((response) => {
          if (response.status < 200 || response.status >= 400) {
            throw (
              "Error subscribing to topic: " +
              response.status +
              " - " +
              response.text()
            );
          }
          console.log("subscribe success : " + response);
        })
        .catch((e) => {
          console.log(e);
        });
    },
    unsubscribe() {
      this.unsubscribeTokenToTopic(this.devecieId);
    },
    unsubscribeTokenToTopic(token) {
      axios({
        method: "POST",
        url: "https://iid.googleapis.com/iid/v1:batchRemove",
        data: {
          to: "/topics/streaming",
          registration_tokens: [token],
        },
        headers: {
          "Content-type": "application/json",
          Authorization:
            "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd",
        },
      })
        .then((response) => {
          if (response.status < 200 || response.status >= 400) {
            throw (
              "Error subscribing to topic: " +
              response.status +
              " - " +
              response.text()
            );
          }
          console.log("unsubscribe success : " + response);
        })
        .catch((e) => {
          console.log(e);
        });

         let num = this.getUserNum;
      http.delete(`/counseling/deleteReadyMentor/${num}`).then(()=>{
      });
       
    },
  },
  computed: {
    ...mapGetters([
      "isProfileLoaded",
      "getRole",
      "getQualification",
      "getUserName",
      "getUserNum",
      "getUserID",
    ]),
    ...mapState({
      authLoading: (state) => state.auth.status === "loading",
      role: (state) => `${state.user.getRole}`,
      qualification: (state) => `${state.user.getQualification}`,
      userNum: (state) => `${state.user.getUserNum}`,
      userName: (state) => `${state.user.getUserName}`,
      userID: (state) => `${state.user.getUserID}`,
    }),
  },
};
</script>
<style>
@font-face {
  font-family: "S-CoreDream-5Medium";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/S-CoreDream-5Medium.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "yg-jalnan";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_four@1.2/JalnanOTF00.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
#app {
  font-family: "S-CoreDream-5Medium";
}
body {
  -ms-overflow-style: none !important;
}
::-webkit-scrollbar {
  display: none !important;
}
html {
  scroll-behavior: smooth;
}
#create .v-speed-dial {
  position: absolute;
}

#create .v-btn--floating {
  position: relative;
}
</style>