<template>
  <div style="height: 100%; width: 100%" class="main-back">
    <v-row
      class="fill-height d-none d-sm-flex"
      align="center"
      justify="center"
      style="padding: 0"
    >
      <template v-for="(item, i) in items">
        <v-col :key="i" cols="12" md="3" style="padding: 0">
          <v-hover v-slot="{ hover }">
            <v-expand-transition>
              <v-card
                :elevation="hover ? 12 : 2"
                :class="{ 'on-hover': hover }"
                height="100vh"
                tile
                @click="itemClick(i)"
              >
                <v-container fill-height>
                  <v-layout align-center justify-center>
                    <v-card-title class="white--text" style="opacity: 1">
                      <div>
                        <p
                          class="ma-0 text-center"
                          style="font-size: 2em; font-weight:bold;"
                        >
                          {{ item.text }}
                        </p>
                        <v-divider
                          class="my-10 white"
                          style="opacity: 1"
                        ></v-divider>
                        <p
                          class="text-center mx-5"
                          style="opacity: 1; font-size: 1em"
                        >
                          {{ item.subtext }}
                        </p>
                      </div>
                    </v-card-title>
                  </v-layout>
                </v-container>
              </v-card>
            </v-expand-transition>
          </v-hover>
        </v-col>
      </template>
    </v-row>
    <!-- 반응 -->
    <div class="d-flex d-sm-none fill-height">
      <v-container style="padding: 0">
        <v-row no-gutters style="height: 50vh">
           <v-col cols="6" @click="itemClick(1)">
            <v-card height="50vh" outlined style="background-color: rgb(14, 1, 27, .5);">
              <v-container fill-height>
                <v-layout align-center justify-center>
                  <p
                    class="ma-0 font-weight-bold text-center white--text"
                    style="font-size: 2em"
                  >
                    녹음 상담
                  </p>
                </v-layout>
              </v-container>
            </v-card>
          </v-col>
          <v-col cols="6" @click="itemClick(0)">
            <v-card height="50vh" outlined style="background-color: rgb(14, 1, 27, .2);">
              <v-container fill-height>
                <v-layout align-center justify-center>
                  <p
                    class="ma-0 font-weight-bold text-center white--text"
                    style="font-size: 2em"
                  >
                    실시간 상담
                  </p>
                </v-layout>
              </v-container>
            </v-card>
          </v-col>
        </v-row>
        <v-row no-gutters style="height: 50vh">
          <v-col cols="6" @click="itemClick(3)">
            <v-card height="50vh" outlined style="background-color: rgb(14, 1, 27, 0.2);">
              <v-container fill-height>
                <v-layout align-center justify-center>
                  <p
                    class="ma-0 font-weight-bold text-center white--text"
                    style="font-size: 2em"
                  >
                    마이페이지
                  </p>
                </v-layout>
              </v-container>
            </v-card>
          </v-col>
           <v-col cols="6" @click="itemClick(2)">
            <v-card height="50vh" outlined style="background-color: rgb(14, 1, 27, 0.5);">
              <v-container fill-height>
                <v-layout align-center justify-center>
                  <p
                    class="ma-0 font-weight-bold text-center white--text"
                    style="font-size: 2em"
                  >
                    상담예약
                  </p>
                </v-layout>
              </v-container>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </div>
    <v-dialog v-model="reser_dialog" max-width="600" min-height="500">
      <div class="px-5 pt-5 reser-back">
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
  </div>
</template>
<script>
import { AUTH_LOGOUT } from "@/store/actions/auth";
import http from "@/util/http-common.js";
import axios from "axios";
import ReserveMain from "@/views/reserve/ReserveMain.vue";
export default {
  components: {
    ReserveMain,
  },
  data() {
    return {
      devecieId: this.$store.getters["getDeviceID"],
      topic: "streaming1",
      reser_dialog: false,
      items: [
        {
          text: "실시간 상담",
          subtext: "전문 상담사와 예약 없이 바로 상담해보세요. ",
        },
        {
          text: "녹음 상담",
          subtext:
            "실시간 상담이 부담스럽다면 녹음을 통해 상담해보세요.",
        },
        {
          text: "상담 예약",
          subtext: "대기중인 상담사가 없다면 예약 서비스를 이용해보세요.",
        },
        {
          text: "마이페이지",
          subtext: "나의 상담 내역과 예약 현황을 확인할 수 있어요.",
        },
      ],
      transparent: "rgba(255, 255, 255, 0)",
      errorSnack: false,
      altMsg: "",
    };
  },
  created() {
    http.delete(`/counseling/liveList`);
  },
  methods: {
    itemClick(i) {
      if (i === 0) {
        this.goLive();
      } else if (i === 1) {
        this.goRecord();
      } else if (i === 2) {
        this.reser_dialog = true;
      } else {
        this.goMypage();
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
    logout: function () {
      this.unsubscribe();
      this.$store.dispatch(AUTH_LOGOUT).then(() => {});
      // this.$router.push("/").catch(() => {});
      window.location.href = "/";
    },
    goMypage() {
      this.$router.push(`/menteeMypage`).catch(() => {});
    },
    goReserve() {
      this.$router.push(`/reserveMain`).catch(() => {});
    },
    goRecord() {
      this.$router.push("/recordConsult/1").catch(() => {});
    },
    unsubscribe() {
      this.unsubscribeTokenToTopic(this.devecieId, this.topic);
    },
    unsubscribeTokenToTopic(token, topic) {
      axios({
        method: "POST",
        url: "https://iid.googleapis.com/iid/v1:batchRemove",
        data: {
          to: "/topics/" + topic,
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

      // http.delete(`/counseling/deleteReadyMentee/${this.$store.getters['getUserNum']}`).then(()=>{
      //   // alert("삭제 완료");
      // });
    },
    reserveDone(msg) {
      this.reser_dialog = msg;
    },
  },
};
</script>
<style scoped>
.v-card {
  transition: 0.4s ease-in;
  background-color: rgb(14, 1, 27, 0.2);
}

.v-card:not(.on-hover) {
  background-color: rgb(14, 1, 27, 0.8);
}

.show-btns {
  color: rgba(255, 255, 255, 1) !important;
}
.reser-back {
  background-color: white;
  background: -moz-linear-gradient(top left, #ff7987, #a23bbe);
  background: -webkit-linear-gradient(top left, #ff7987, #a23bbe);
  -moz-background-origin: border;
  background-origin: border-box;
  border: 10px solid transparent;
  border-radius: 25px;
  box-shadow: inset 0 0 2px white,
    /* Inset shadow */ /*0 0 12px white, /* Outset shadow */ inset -999px 0 0 white; /* The background color */
}
</style>
