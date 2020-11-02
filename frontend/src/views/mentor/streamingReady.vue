<template>
  <div>
    <v-btn @click="onjoin()"> ready </v-btn>
  </div>
</template>
<script>
import http from "@/util/http-common.js";
import axios from "axios";
export default {
  props: {
    room: {
      type: String,
      default: "",
    },
    room_num: {
      type: Number,
      default: 0,
    },
  },
  methods: {
    onjoin() {
      let mentorName = this.$store.getters['getUserNum'];
      http
        .get(`/counseling/isRoom/${mentorName}/${this.room}`)
        .then((res) => {
          console.log(res.data);
          if (res.data == "fail") {
            alert("이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ! ");
          } else {
            alert(" 상담을 시작합니다. ")
            this.unsubscribe();
            this.$router.push(`/counselorWRTC/${this.room}&${this.room_num}`);
          }
        })
        .catch((e) => {
          console.log(e);
        });
      
    },
    unsubscribe(){
      let token = this.$store.getters["getDeviceID"];
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
      }
    },
};
</script>