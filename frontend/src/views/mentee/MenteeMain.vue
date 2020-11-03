<template>
  <div>
    <div class="main-back">
      <!-- background: linear-gradient(to right, #3D5469, #79B7D1); -->
      <div style="height: 100vh" class="d-flex justify-content-center">
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
                  src="@/assets/icons/stream_btnm.png"
                  style="width: 100%; height: 100%"
                  @click="goLive()"
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
              @click="goRecord()"
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/voice_btn.png"
                  style="width: 100%; height: 100%"
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
                  src="@/assets/icons/res_btn.png"
                  style="width: 100%; height: 100%"
                  @click="goReserve()"
                />
              </div>
            </v-btn>
          </div>
          
        </v-col>
      </div>
    </div>
    
  </div>
</template>
<script>
import { AUTH_LOGOUT } from "@/store/actions/auth";
import http from "@/util/http-common.js";
import axios from "axios";
export default {
  data() {
    return {
      devecieId: this.$store.getters["getDeviceID"],
      topic: "streaming",
    };
  },
  methods: {
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
      this.$router.push("/recordConsult/1").catch(()=>{});
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

      http.delete(`/counseling/deleteReadyMentee/${this.$store.getters['getUserNum']}`).then(()=>{
        // alert("삭제 완료");
      }); 

    },
  },
  
};
</script>
