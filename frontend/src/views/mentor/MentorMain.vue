<template>
  <div>
    <div class="main-back">
      <div style="height: 100vh" class="d-flex justify-content-center">
        <v-col class="my-auto" align="center">
          <alarmList></alarmList>
          <v-btn v-if="readyClick" @click="unsubscribe()">대기 취소</v-btn>
          <v-btn @click="logout()">로그아웃</v-btn>
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
      </div>
    </div>
  </div>
</template>

<script>
import { AUTH_LOGOUT } from "@/store/actions/auth";
import http from "@/util/http-common.js";
import axios from "axios";
import alarmList from "@/components/mentor/alarm.vue";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      devecieId: this.$store.getters["getDeviceID"],
      topic: "streaming",
      readyClick:false,
    };
  },
  components: {
    alarmList,
  },
  methods: {
    subscribe() {
      console.log("click subscribe btn");
      this.readyClick= true;
      http.get(`/counseling/getMenteeCnt`).then((res) => {
        console.log(res.data);
        if (res.data == "empty") {
          //상담사 대기
          this.subscribeTokenToTopic(this.devecieId, this.topic);
          let mentorname = this.$store.getters["getUserNum"];
          http.get(`/counseling/addReady/${mentorname}`).then((res) => {
            console.log("add ready success : " + res);
          });
        } else {
          // 학생 대기
          alert(" 상담을 시작합니다. ");
          this.$router.push(`/counselorWRTC/${res.data.room}&${res.data.num}`);
        }
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
    goLiveList() {
      this.$router.push(`/liveList`);
    },
    logout: function () {
      this.unsubscribe();
      this.$store.dispatch(AUTH_LOGOUT).then(() => {});
      // this.$router.push("/").catch(() => {});
      window.location.href = "/";
    },
    goMypage() {
      this.$router.push(`/mentorMypage`);
    },
    goMyMenteeList() {
      this.$router.push(`/myMenteeList`);
    },
    goRecordList() {
      this.$router.push(`/recordList`);
    },
    unsubscribe() {
      this.readyClick = false;
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
        "getUserNum",
      ]),
      }
};
</script>