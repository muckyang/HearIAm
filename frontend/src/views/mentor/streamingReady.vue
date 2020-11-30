<template>
  <div>
    <div class="main-back">
      <div style="height: 100vh" class="d-flex justify-content-center">
        <v-col class="my-auto" align="center">
          <v-btn v-if="this.getIsReady" @click="unsubscribe()">대기 취소</v-btn>
          <div>
            <v-btn
              depressed
              elevation="9"
              fab
              icon
              style="
                width: 260px;
                height: 250px;
                background: linear-gradient(to top, black, #0a7a78);
              "
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/t_stream_btn.png"
                  style="width: 100%; height: 100%"
                  @click="subscribe()"
                />
              </div>
            </v-btn>
          </div>
        </v-col>
        <v-col class="my-auto" align="center">
          <div>
            <v-btn
              depressed
              elevation="9"
              fab
              icon
              style="
                width: 260px;
                height: 250px;
                background: linear-gradient(to top, black, #0a7a78);
              "
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/t_voice_btn.png"
                  style="width: 100%; height: 100%"
                  @click="goRecordList()"
                />
              </div>
            </v-btn>
          </div>
        </v-col>
        <v-col class="my-auto" align="center">
          <div>
            <v-btn
              depressed
              elevation="9"
              fab
              icon
              style="
                width: 260px;
                height: 250px;
                background: linear-gradient(to top, black, #0a7a78);
              "
              @click="goMyMenteeList()"
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/t_report_btn.png"
                  style="width: 100%; height: 100%"
                />
              </div>
            </v-btn>
          </div>
        </v-col>
        <div
          style="
            height: 100%;
            width: 100%;
            background: linear-gradient(to right, #93dfff, #f5a2bb);
          "
        >
          <div style="height: 100vh" class="d-flex justify-content-center">
            <v-col class="my-auto" align="center">
              <v-btn @click="onjoin()"> ready </v-btn>
            </v-col>
          </div>
        </div>
      </div>
    </div>
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
  data() {
    return {
      errorSnack: false,
      successSnack: false,
      altMsg: "",
    };
  },
  methods: {
    onjoin() {
      let mentorName = this.$store.getters["getUserNum"];
      http
        .get(`/counseling/isRoom/${mentorName}/${this.room_num}`)
        .then((res) => {
          console.log(res.data);
          //예약에 따른 conRoom 찾아서 조건 달기
          //list 리턴할때 예약에따라서 알람리스트에 나올 text 저장해두기. conRoom, mentor, text
          if (res.data == "fail") {
            this.errorSnack = true;
            this.altMsg =
              "이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ!";
          } else {
            this.successSnack = true;
            this.altMsg = "상담을 시작합니다.";
            this.unsubscribe();
            this.$router.push(`/counselorWRTC/${this.room}&${this.room_num}`);
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    unsubscribe() {
      let token = this.$store.getters["getDeviceID"];
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
        .delete(
          `/counseling/deleteReadyMentor/${this.$store.getters["getUserNum"]}`
        )
        .then(() => {});
    },
  },
};
</script>