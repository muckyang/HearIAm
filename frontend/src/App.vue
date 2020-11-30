<template>
  <v-app>
    <v-app-bar
      v-if="getRole"
      fixed
      flat
      dark
      hide-on-scroll
      scroll-target="#scrolling-techniques-4"
      style="background-color: rgba(255, 255, 255, 0)"
    >
      <v-btn class="d-none d-sm-flex" text @click="goHome()"
        ><h2>Hear I Am</h2></v-btn
      >
      <v-spacer class="d-none d-sm-flex"></v-spacer>
      <v-btn
        class=" d-none d-sm-flex"
        v-if="getIsReady && getRole == `mentor`"
        text
        @click="unsubscribe()"
        >상담 대기 취소</v-btn
      >
      <!-- <v-btn
        class="d-none d-sm-flex"
        v-if="getRole == `mentor`"
        text
        @click="subscribe()"
        >실시간 상담 대기</v-btn
      > -->
      <v-btn
        class=" d-none d-sm-flex"
        v-if="getRole == `mentee`"
        text
        @click="goLive()"
        >실시간 상담</v-btn
      >

      <v-btn
        class=" d-none d-sm-flex"
        v-if="getRole == `mentee`"
        text
        @click="goRecord()"
        >녹음 상담</v-btn
      >
      <v-btn
        class=" d-none d-sm-flex"
        v-if="getRole == `mentee`"
        text
        @click="reser_dialog = true"
        >상담 예약</v-btn
      >

      <v-btn
        class=" d-none d-sm-flex"
        v-if="getRole == `mentor`"
        text
        @click="goRecordList()"
        >녹음 상담 현황</v-btn
      >
      <v-btn
        class=" d-none d-sm-flex"
        v-if="getRole == `mentor`"
        text
        @click="goMyMenteeList()"
        >일지 관리</v-btn
      >
      <v-btn class=" d-none d-sm-flex" text @click="goMypage()"
        >마이페이지</v-btn
      >
      <v-btn class=" d-none d-sm-flex" text @click="logout()"
        >로그아웃</v-btn
      >
    </v-app-bar>
    <v-sheet
      id="scrolling-techniques-4"
      class="overflow-y-auto"
      style="height: 100% !important"
    >
      <div
        v-if="getUserName != ''"
        class="d-flex d-sm-none"
        style="position: absolute"
      >
        <input type="checkbox" id="menu__active" />
        <label for="menu__active" class="menu__active">
          <div class="menu__toggle">
            <div class="icon">
              <div class="hamburger"></div>
            </div>
          </div>
          <div class="menu__listings">
            <ul class="circle">
              <li></li>
              <li></li>
              <li @click="logout()">
                <p
                  style="
                    transform: rotate(185deg) skewY(54deg);
                    position: absolute;
                    top: 105px;
                    left: 40px;
                  "
                >
                  로그<br />아웃
                </p>
              </li>
              <li @click="reser_dialog = true">
                <p
                  style="
                    transform: rotate(185deg) skewY(54deg);
                    position: absolute;
                    top: 80px;
                    left: 25px;
                  "
                >
                  상담<br />예약
                </p>
              </li>
              <li></li>
              <li></li>
              <li></li>
              <li></li>
              <li>
                <p
                  @click="goMypage()"
                  style="
                    transform: rotate(185deg) skewY(55deg);
                    position: absolute;
                    top: 80px;
                    left: 15px;
                  "
                >
                  마이<br />페이지
                </p>
              </li>
              <li @click="goLive()">
                <p
                  style="
                    transform: rotate(185deg) skewY(54deg);
                    position: absolute;
                    top: 90px;
                    left: 15px;
                  "
                >
                  실시간<br />상담
                </p>
              </li>
              <li @click="goRecord()">
                <p
                  style="
                    transform: rotate(185deg) skewY(54deg);
                    position: absolute;
                    top: 80px;
                    left: 20px;
                  "
                >
                  녹음<br />
                  상담
                </p>
              </li>
            </ul>
          </div>
        </label>
      </div>
      <v-main>
        <router-view />
      </v-main>
    </v-sheet>

    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <span v-bind="attrs" v-on="on">
          <v-speed-dial
            v-model="center"
            v-if="getRole == `mentee`"
            bottom
            right
            fixed
            direction="top"
            transition="slide-x-reverse-transition"
          >
            <template v-slot:activator>
              <v-btn
                class="d-none d-sm-flex"
                v-model="center"
                color="#49358b"
                dark
                fab
                @click="viewCenter()"
              >
                <v-icon> mdi-map </v-icon>
              </v-btn>
              <v-btn
                class="d-flex d-sm-none"
                v-model="center"
                color="#49358b"
                dark
                small
                fab
                @click="viewCenter()"
              >
                <v-icon small> mdi-map </v-icon>
              </v-btn>
            </template>
          </v-speed-dial>
        </span>
      </template>
      <span>상담센터 검색</span>
    </v-tooltip>
    <alarmComp v-if="getRole == `mentor`"></alarmComp>

    <v-dialog v-model="reser_dialog" max-width="600" min-height="500">
      <div class="px-5 pt-5 reser-back" >
        <span style="font-size: 2rem; font-weight:bold;">실시간 상담 예약</span>
        <v-icon
          class="mb-3"
          style="float: right; color:crimson"
          @click="reser_dialog=false"
          >mdi-close</v-icon
        >
        <ReserveMain :reser_dialog="reser_dialog" @reserve="reserveDone()" />
      </div>
    </v-dialog>

    <v-dialog v-model="center_dialog" width="90%">
      <div class="px-5 pt-5 center-back" style="overflow-y: scroll">
        <span style="font-size: 2rem; font-weight:bold;">청소년 상담센터 찾기</span>
        <v-icon
          class="mb-3"
          color="red"
          style="float: right"
          @click="center_dialog = false"
          >mdi-close</v-icon
        >
        <CounsultingCenter />
      </div>
    </v-dialog>

    <v-snackbar
      v-model="errorSnack"
      top
      flat
      color="error"
      rounded="pill"
      :timeout="2000"
    >
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
  </v-app>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import { AUTH_LOGOUT } from "@/store/actions/auth";
import http from "@/util/http-common.js";
import axios from "axios";
import alarmComp from "@/components/mentor/alarm.vue";
import ReserveMain from "@/views/reserve/ReserveMain.vue";
import CounsultingCenter from "@/views/ConsultingCenter.vue";

export default {
  name: "App",
  components: {
    alarmComp,
    ReserveMain,
    CounsultingCenter,
  },
  data() {
    return {
      reser_dialog: false,
      errorSnack: false,
      altMsg: "",
      center: false,
      center_dialog: false,
    };
  },
  updated() {
    if (this.getRole == "mentor") {
      http
        .get(`/counseling/alarmList/${this.$store.getters["getUserNum"]}`)
        .then((res) => {
          this.$store.commit("setAlarmList", res.data);
          let len = this.$store.getters["getAlarmList"].length;
          if (len > 0) {
            this.$store.commit("changeAlarmBtn", "#49358b");
          }
        });
    }
  },
  methods: {
    logout: function () {
      this.unsubscribe();
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.drawer = false;
      });
      // this.$router.push("/").catch(() => {});
      window.location.href = "/";
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
          // console.dir(res);
          // console.log(res.data);
          if (res.data == 0) {
            this.errorSnack = true;
            this.altMsg = "대기중인 멘토가 없어요! 예약하기를 이용해주세요!";
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
      this.$router.push(`/myMenteeList`).catch(() => {});
    },
    goRecordList() {
      this.$router.push(`/recordList`).catch(() => {});
    },
    // subscribe() {
    //   http.delete(`/counseling/liveList`);
    //   // console.log("click subscribe btn");
    //   this.$store.commit("changeIsReady", true);
    //   this.subscribeTokenToTopic(this.getDeviceID, this.topic);
    //   // let mentorname = this.$store.getters['getUserNum'];
    //   http.get(`/counseling/addReady/${this.getUserNum}`).then((res) => {
    //     console.log("add ready success : " + res);
    //   });
    // },
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
      this.errorSnack = true;
      this.altMsg = "상담이 취소되었습니다. ";
      this.$store.commit("changeIsReady", false);
      this.unsubscribeTokenToTopic(this.getDeviceID);
      this.errorSnack = false;
    },
    unsubscribeTokenToTopic(token) {
      axios({
        method: "POST",
        url: "https://iid.googleapis.com/iid/v1:batchRemove",
        data: {
          to: "/topics/streaming1",
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
      http.delete(`/counseling/deleteReadyMentor/${num}`).then(() => {});
    },
    reserveDone(msg) {
      this.reser_dialog = msg;
    },
    viewCenter() {
      this.center_dialog = true;
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
      "getIsReady",
      "getDeviceID",
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
.shadow {
  text-shadow: #464646 3px 3px 3px;
}
/* .v-sheet {
    height: 100% !important;
} */
.reser-back {
  background-color: white;
  background: -moz-linear-gradient(top left, #ff7987, #a23bbe);
  background: -webkit-linear-gradient(top left, #ff7987, #a23bbe);
  -moz-background-origin: border;
  background-origin: border-box;
  border: 10px solid transparent;
  border-radius: 25px;
  box-shadow: inset 0 0 0px white,
    /* Inset shadow */ /*0 0 12px white, /* Outset shadow */ inset -999px 0 0 white; /* The background color */
}

.center-back {
  background-color: white;
  background: -moz-linear-gradient(top left, #ff7987, #a23bbe);
  background: -webkit-linear-gradient(top left, #ff7987, #a23bbe);
  -moz-background-origin: border;
  background-origin: border-box;
  border: 10px solid transparent;
  border-radius: 25px;
  box-shadow: inset 0 0 2px white,
    /* Inset shadow */ /*0 0 12px white, /* Outset shadow */ inset -1500px 0 0 white; /* The background color */
}

.icon {
  position: absolute;
  /* top: 70%;
  left: 70%; */
  top: 100px;
  right: 10px;
  transform: translateX(-50%) translateY(-50%);
}
.hamburger {
  content: " ";
  position: relative;
  width: 20px;
  border: 2px solid #392338;
  border-radius: 5px;
  transition: all 0.333333333333333s ease;
}
.hamburger:after,
.hamburger:before {
  content: " ";
  position: absolute;
  left: 50%;
  width: 100%;
  border: 2px solid #392338;
  border-radius: 5px;
  transform: translateX(-50%);
  transition-property: top, bottom, transform;
  transition-duration: 1s, 1s, 0.25s;
}
.hamburger:after {
  top: -8px;
}
.hamburger:before {
  bottom: -8px;
}
.menu {
  pointer-events: none;
}
.menu--circle {
  position: absolute;
  z-index: 13;
  width: 243.33333333333334px;
  height: 243.33333333333334px;
  background-color: #392338;
  overflow: hidden;
}
.menu__active {
  position: relative;
}
.menu__toggle {
  z-index: 11;
  position: absolute;
  top: 100px;
  left: -150px;
  /* top: 100px;
  left: -150px; */
  width: 200px;
  height: 200px;
  background-color: #fff;
  border-radius: 50%;
  transition: transform 1.3s;
  cursor: pointer;
  pointer-events: auto;
}
.menu__listings {
  z-index: 10;
  position: absolute;
  top: 10px;
  left: -250px;
  width: 400px;
  height: 400px;
  border-radius: 50%;
  transform: scale(0.1) rotate(150deg);
  transition: transform 1s;
}
.menu__arrow {
  visibility: hidden;
  position: absolute;
}
.menu__arrow input[type="radio"] {
  position: fixed;
  top: -99999px;
  left: -99999px;
}
.menu__arrow ul {
  padding: 0;
  margin: 0;
  list-style: none;
}
.menu__arrow--top {
  top: 0.5em;
  left: 220px;
}
.menu__arrow--top .arrow {
  transform: rotate(-45deg);
}
.menu__arrow--left {
  top: 220px;
  left: 0.5em;
}
.menu__arrow--left .arrow {
  transform: rotate(-135deg);
}
.arrow {
  width: 20px;
  height: 20px;
  border-right: 6.666666666666667px solid #fff;
  border-top: 6.666666666666667px solid #fff;
  border-radius: 3px;
  transition: border-color 0.3s;
  cursor: pointer;
  pointer-events: auto;
}
.arrow:hover {
  border-color: #ff947f;
  transition: border-color 0.3s;
}
.circle {
  position: relative;
  padding: 0;
  margin: 0;
  height: 100%;
  width: 100%;
  background-color: #fff;
  border-radius: 50%;
  transform: rotate(60deg);
  overflow: hidden;
  list-style: none;
}
.circle li {
  position: absolute;
  top: 0;
  right: 0;
  width: 50%;
  height: 50%;
  transform-origin: 0 100%;
}
.circle li .placeholder {
  position: absolute;
  left: -100%;
  padding-top: 1.5em;
  width: 200%;
  height: 200%;
  text-align: center;
  transform: skewY(54deg) rotate(18deg);
}
.circle li .placeholder .upside {
  transform: rotate(180deg);
}
.circle li .placeholder a {
  text-decoration: none;
  pointer-events: auto;
}
.circle li .placeholder button {
  pointer-events: auto;
}
.circle li .placeholder .button {
  font-size: 2.3em;
  background-color: transparent;
  border: none;
  color: #392338;
  cursor: pointer;
}
.circle li:nth-child(9) {
  transform: rotate(0deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(10) {
  transform: rotate(30deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(11) {
  transform: rotate(60deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(12) {
  transform: rotate(90deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(5) {
  transform: rotate(120deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(6) {
  transform: rotate(150deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(7) {
  transform: rotate(180deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(8) {
  transform: rotate(210deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(1) {
  transform: rotate(240deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(2) {
  transform: rotate(270deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(3) {
  transform: rotate(300deg) skewY(-54deg);
  background-color: #fff;
}
.circle li:nth-child(4) {
  transform: rotate(330deg) skewY(-54deg);
  background-color: #fff;
}
#menu__active {
  position: fixed;
  top: -99999px;
  left: -99999px;
}
#menu__active:checked ~ label .menu__listings {
  transform: rotate(10deg) scale(1);
  transition: transform 1s;
}
#menu__active:checked ~ label .menu__toggle {
  background-color: #392338;
  transition: all 1s;
}
#menu__active:checked ~ label .hamburger {
  border-color: transparent;
  transition: border-color 0.333333333333333s;
}
#menu__active:checked ~ label .hamburger:after {
  top: -2px;
  border-color: #fff;
  transform: translateX(-50%) rotate(45deg);
  transition-property: top, transform;
  transition-duration: 0.25s, 1s;
}
#menu__active:checked ~ label .hamburger:before {
  bottom: -2px;
  border-color: #fff;
  transform: translateX(-50%) rotate(-45deg);
  transition-property: bottom, transform;
  transition-duration: 0.25s, 1s;
}
#menu__active:checked ~ label .button:hover {
  color: #c1264e;
}
#menu__active:checked ~ label .menu__arrow {
  visibility: visible;
  transition: all 1s 1.111111111111111s;
}
#menu__active:checked ~ label .menu__arrow--top label {
  display: none;
}
#menu__active:checked ~ label .menu__arrow--top label[for="degree--up-0"] {
  display: block;
}
#menu__active:checked ~ label #degree--up-0:checked ~ .menu__listings {
  transform: rotate(116deg);
}
#menu__active:checked ~ label #degree--up-0:checked ~ .menu__arrow--top label {
  display: none;
}
#menu__active:checked
  ~ label
  #degree--up-0:checked
  ~ .menu__arrow--top
  label[for="degree--up-1"] {
  display: block;
}
#menu__active:checked ~ label #degree--up-1:checked ~ .menu__listings {
  transform: rotate(224deg);
}
#menu__active:checked
  ~ label
  #degree--up-1:checked
  ~ .menu__listings
  ~ .menu__arrow--top
  label {
  display: none;
}
#menu__active:checked
  ~ label
  #degree--up-1:checked
  ~ .menu__listings
  ~ .menu__arrow--top
  label[for="degree--up-2"] {
  display: block;
}
.logostyle {
  font-size: 1em;
  color: #524d4d;
}
</style>