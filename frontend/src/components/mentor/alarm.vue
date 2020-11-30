<template>
  <v-speed-dial
    v-model="fab"
    bottom
    right
    fixed
    direction="top"
    transition="slide-x-reverse-transition"
  >
    <template v-slot:activator>
       <v-btn v-model="fab" :color="getAlarmBtn" dark fab @click="alarmClick()">
        <v-icon v-if="fab">mdi-close </v-icon>
        <v-icon v-else> mdi-bell </v-icon>
      </v-btn>
    </template>
    <v-card min-width="300" style="margin-right: 230px; text-align: center">
      <v-list dense>
        <h4>알람</h4>
        <v-list-item-group v-model="alarm" color="primary">
          <v-list-item v-if="getAlarmList.length == 0">
            <v-list-item-icon>
              <v-icon>mdi-bell-alert</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>알람이 없어요!</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item v-for="(item, index) in getAlarmList" :key="index">
            <v-list-item-icon>
              <v-icon>mdi-bell-alert</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title @click="onjoin(item)">
                실시간 상담 요청이 왔습니다!</v-list-item-title
              >
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-card>
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

    <v-snackbar
      v-model="successSnack"
      top
      flat
      color="success"
      rounded="pill"
      :timeout="2000"
    >
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
  </v-speed-dial>

</template>
<script>
import http from "@/util/http-common.js";
import axios from "axios";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      alert_num: 0,
      list: this.getAlarmList,
      btn_color: this.getAlarmBtn, //빨 : #F44336   그린 :  #49358b  F44336
      fab: false,
      alarm: null,
      errorSnack: false,
      successSnack: false,
      altMsg: "",
    };
  },
  methods: {
    onjoin(data) {
      // console.dir(data);
      let mentorName = this.getUserNum;
      if (data.mentor == 1) {
        http
          .get(`/counseling/isRoom/${mentorName}/${data.num}`)
          .then((res) => {
            if (res.data == "fail") {
              this.errorSnack = true;
              this.altMsg =
                "이미 상담 중입니다. 다음엔 차례를 기다려 주세요.";
              this.$router.push("/");
            } else {
              this.unsubscribe();
              this.$router.push(
                `/counselorWRTC/${data.conRoom.room}&${data.num}`
              );
            }
          })
          .catch((e) => {
            console.log(e);
          });
      } else {
        this.errorSnack = true;
        this.altMsg = "이미 상담 중입니다. 다른 학생과 상담하세요.";
      }
    },
    unsubscribe() {
      let token = this.getDeviceID;
      this.$store.commit("changeIsReady", false);
      let topic = "streaming1";
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
      http
        .delete(`/counseling/deleteReadyMentor/${this.getUserNum}`)
        .then(() => {});
    },
    alarmClick() {
      //알람 끈다 초록
      // this.$store.commit('setAlarmCnt',0);
      // this.$store.commit('setAlarmFlag',false);
      this.$store.commit("changeAlarmBtn", "#49358b");
    },
  },
  updated() {
    http
      .get(`/counseling/alarmList/${this.$store.getters["getUserNum"]}`)
      .then((res) => {
        this.$store.commit("setAlarmList", res.data);
        let len = this.$store.getters["getAlarmList"].length;
        if (len > 0) {
          this.$store.commit("changeAlarmBtn", "#F44336");
        }
      });
  },
  computed: {
    ...mapGetters([
      "getUserName",
      "getUserNum",
      "getDeviceID",
      "getAlarmList",
      "getAlarmBtn",
    ]),
  },
};
</script>