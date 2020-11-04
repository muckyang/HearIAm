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
      <v-btn v-model="fab" :color="btn_color" dark fab @click="alarmClick()">
        <v-icon v-if="fab">mdi-close </v-icon>
        <v-icon v-else> mdi-bell </v-icon>
      </v-btn>
    </template>
    <v-card min-width="300" style="margin-right: 230px; text-align: center">
      <v-list dense>
        <h4>알람</h4>
        <v-list-item-group v-model="alarm" color="primary">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-bell-alert</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>상담요청이 왔어요!</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item v-for="(item, index) in list" :key="index">
            <v-list-item-title @click="onjoin(item)">
              실시간 상담 요청이 왔습니다!</v-list-item-title
            >
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-card>
  </v-speed-dial>
  <!-- <div>
    <div class="my-2">
      <v-btn color="error" fab small dark @click="move()"
        >{{ alert_num }}
      </v-btn>
    </div>
  </div> -->

  <!-- <div class="text-center">
    <v-menu offset-y>
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="error" fab small dark v-bind="attrs" v-on="on">
          {{ alert_num }}
        </v-btn>
      </template>
      <v-list>
        <v-list-item v-for="(item, index) in list" :key="index">
          <v-list-item-title @click="onjoin(item)">
            실시간 상담 요청이 왔습니다!</v-list-item-title
          >
        </v-list-item>
      </v-list>
    </v-menu>
  </div> -->
</template>
<script>
import http from "@/util/http-common.js";
import axios from "axios";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      alert_num: 0,
      list: [],
      btn_color: "#0a7a78", //빨 : #F44336   그린 :  #0a7a78
      fab: false,
      alarm: null,
    };
  },
  methods: {
    onjoin(data) {
      let mentorName = this.getUserNum;
      if (data.mentor == 1) {
        http
          .get(`/counseling/isRoom/${mentorName}/${data.num}`)
          .then((res) => {
            if (res.data == "fail") {
              alert("이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ! ");
              this.$router.push("/");
            } else {
              alert(" 상담을 시작합니다. ");
              this.unsubscribe();
              this.$router.push(`/counselorWRTC/${data.room}&${mentorName}`);
            }
          })
          .catch((e) => {
            console.log(e);
          });
      } else {
        alert("이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ! ");
      }
    },
    unsubscribe() {
      let token = this.getDeviceID;
      this.$store.commit("changeIsReady", false);
      let topic = "streaming";
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
      this.btn_color = "#0a7a78";
    },
  },
  mounted() {
    this.alert_num = this.getUserNum;
    http.get(`/counseling/alarmList/${this.getUserNum}`).then((res) => {
      this.list = res.data;
      this.alert_num = this.list.length;
      // this.getAlarmCnt = this.list.length;
      // this.$store.commit('setAlarmCnt', this.list.length);
      // console.log("알람 온 수 : "+ this.getAlarmCnt);
      if (this.list.length > 0) {
        this.btn_color = this.btn_color = "#F44336";
      }
    });
  },
  computed: {
    ...mapGetters([
      "getUserName",
      "getUserNum",
      "getDeviceID",
      "getAlarmCnt",
      "getAlarmFlag",
    ]),
  },
  // watch:{
  //   getAlarmCnt(val){
  //     if(val>0){ //알람불 들어옴 빨강
  //       // this.$store.commit('setAlarmFlag',true);
  //       this.btn_color = "#F44336";
  //     }
  //   }
  // },
};
</script>