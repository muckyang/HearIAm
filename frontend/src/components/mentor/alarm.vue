<template>
  <!-- <div>
    <div class="my-2">
      <v-btn color="error" fab small dark @click="move()"
        >{{ alert_num }}
      </v-btn>
    </div>
  </div> -->

  <div class="text-center">
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
  </div>
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
    };
  },
  methods: {
    onjoin(data) {
      let mentorName = this.getUserNum;
      console.log(mentorName + " " + data.room);
      http
        .get(`/counseling/isRoom/${mentorName}/${data.room}`)
        .then((res) => {
          console.log(res.data);
          if (res.data == "fail") {
            alert("이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ! ");
          } else {
            alert(" 상담을 시작합니다. ");
            this.unsubscribe();
            this.$router.push(`/counselorWRTC/${data.room}&${mentorName}`);
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    unsubscribe() {
      let token = this.getDeviceID;
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
      console.log("dekl " + this.getUserNum);
      http
        .delete(`/counseling/deleteReadyMentor/${this.getUserNum}`)
        .then(() => {});
    },
  },
  mounted: function () {},
  watch: {
    getUserNum() {
      http.get(`/counseling/alarmList/${this.getUserNum}`).then((res) => {
        console.dir(res);
        this.list = res.data;
        console.log(this.list.length);
        this.alert_num = this.list.length;
      });
    },
  },
  computed: {
    ...mapGetters(["getUserName", "getUserNum", "getDeviceID"]),
  },
};
</script>