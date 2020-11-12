<template>
  <div style="height: 100%; width: 100%" class="main-back">
    <v-row
      class="fill-height"
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
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      devecieId: this.getDeviceID,
      topic: "streaming1",
      successSnack: false,
      altMsg: "",
      getKey: false,
      items: [
        {
          text: "실시간 상담",
          subtext: "대기를 통해 상담을 원하는 학생과 매칭될 수 있어요.",
        },
        {
          text: "녹음 상담",
          subtext: "녹음된 학생의 고민을 듣고 상담해주세요.",
        },
        {
          text: "일지 관리",
          subtext: "상담한 학생들의 상담 일지를 날짜별로 관리해보세요. ",
        },
        {
          text: "마이페이지",
          subtext: "나의 일정을 관리하고 상담 현황을 확인할 수 있어요.",
        },
      ],
      transparent: "rgba(255, 255, 255, 0)",
    };
  },
  created() {
    http.delete(`/counseling/liveList`);
  },
  methods: {
    itemClick(i) {
      if (i === 0) {
        this.subscribe();
      } else if (i === 1) {
        this.goRecordList();
      } else if (i === 2) {
        this.goMyMenteeList();
      } else {
        this.goMypage();
      }
    },
    subscribe() {
      if (this.getIsReady) {
        this.successSnack = true;
        this.altMsg = "이미 상담 대기 중입니다.";
        setTimeout(function () {
          // Code here
          this.successSnack = false;
        }, 1000);
      } else {
        this.$store.commit("changeIsReady", true);
        http.get(`/counseling/getMenteeCnt`).then((res) => {
          if (res.data == "empty") {
            this.successSnack = true;
            this.altMsg = "상담을 대기합니다.";
            this.subscribeTokenToTopic(this.getDeviceID, this.topic);
            let mentorname = this.$store.getters["getUserNum"];
            http.get(`/counseling/addReady/${mentorname}`).catch((e) => {
              console.log(e);
              this.successSnack = false;
            });
          } else {
            // 학생 대기
            this.successSnack = true;
            console.dir(res);
            console.dir(res.conRoom);
            console.dir(res.data);
            console.dir(res.data.conRoom);
            console.log("여기!!!! ::::"+res.data.conRoom.room+" "+res.data.conRoom.num);
            this.altMsg = "상담을 시작합니다.";
            // this.$router.push(
            //   `/counselorWRTC/${res.conRoom.room}&${res.conRoom.num}`
            // );
          }
        });
      }
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
        })
        .catch((e) => {
          console.log(e);
        });
    },
    goLiveList() {
      this.$router.push(`/liveList`);
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
      // console.log("hhhhhhh");
      this.errorSnack = true;
      this.altMsg = "상담이 취소되었습니다. ";
        this.$store.commit("changeIsReady", false);
      this.unsubscribeTokenToTopic(this.getDeviceID);
      this.errorSnack = false;
    },
    unsubscribeTokenToTopic(token) {
      console.log("unsubscribeTokenToTopic");
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
  },
  computed: {
    ...mapGetters(["getDeviceID", "getUserNum", "getIsReady"]),
  },
  mounted() {
    // window.addEventListener("keypress", function (event) {
    //   if (event.key == "F5") {
    //     console.log("key press");
    //     this.getKey = true;
    //   }
    // });

    // window.addEventListener("beforeunload", function (event) {
    //   console.log("unload");

    //   if (!this.getKey) {
    //     console.log("press f5!");
    //     this.getKey = false;
    //     // event.preventDefault;
    //     // event.returnValue = "";
    //   } else {
    //     console.log("else");
    //     console.log(this.getKey);

    //     // this.unsubscribe();
    //     // if (this.getIsReady) {
    //       console.log("s;hdfisd;dsbsdkbsd");
    //       axios({
    //         method: "POST",
    //         url: "https://iid.googleapis.com/iid/v1:batchRemove",
    //         data: {
    //           to: "/topics/streaming1",
    //           registration_tokens: [this.getDeviceID],
    //         },
    //         headers: {
    //           "Content-type": "application/json",
    //           Authorization:
    //             "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd",
    //         },
    //       })
    //         .then((response) => {
    //           if (response.status < 200 || response.status >= 400) {
    //             throw (
    //               "Error subscribing to topic: " +
    //               response.status +
    //               " - " +
    //               response.text()
    //             );
    //           }
    //           console.log("unsubscribe success : " + response);
    //         })
    //         .catch((e) => {
    //           console.log(e);
    //         });

    //       let num = this.getUserNum;
    //       http.delete(`/counseling/deleteReadyMentor/${num}`).then(() => {});
    //     }
        
        
    //     event.preventDefault;
    //     event.returnValue = "";
    //   // }
    // });
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
</style>
