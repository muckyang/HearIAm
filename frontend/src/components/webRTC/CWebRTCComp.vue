<template>
  <div style="height: 100%; width: 100%" class="webRtc-back">
    <v-dialog v-model="dialog" persistent max-width="1300">
      <div >
        <div v-if="!isSangdam" style="border: 3px solid white; border-radius:10px;color:white;">
        <v-container style="height:400px; padding-top:9%;">
          <div>
          <h1>
            상담 준비가 완료 되었다면 <br />
            시작버튼을 눌러주세요.
          </h1>
          <v-btn
            v-if="!isProgress"
            x-large
            style="font-size:1.2rem;border-radius:30px; color:white"
             color="#ff7987"
            class="btn btn-primary mt-5 heartbeat"
            @click="sangdamStart()"
          >
            상담 시작
          </v-btn>
          </div>
        </v-container>
        </div>
        <div style="height: 600px; background-color:white;" v-if="isSangdam">
          <v-row align="center" justify="center" style="height: 100%">
            <v-col cols="3" v-if="menteeName">
              <h2>
                <span style="color: #262272">{{ menteeName }}</span
                >님과 상담중
              </h2>
              <p>상담이 끝났다면 아래 버튼을 눌러주세요.</p>
              <v-btn
                v-if="isProgress"
                style="font-size: 0.9rem; color: white"
                color="#262272"
                class="btn btn-primary"
                @click="onLeave"
              >
                상담 종료
              </v-btn>
            </v-col>
            <v-divider vertical v-if="menteeName"></v-divider>
            <v-col cols="4" style="height: 100%; padding-top: 4%">
              <div style="width: 100%" v-if="menteeName">
                <span style="font-size: 1.5rem; font-weight: bold"
                  >청소년 실시간 감정그래프<v-icon color="black" class="ml-1"
                    >mdi-chart-bar</v-icon
                  ></span
                >
                <br /><span style="font-size: 0.9rem"
                  >*학생의 익명성을 위해, 카메라 화면 대신 감정이
                  보여집니다.</span
                >
                <div class="mt-5">
                  <Bar :chartData="chartData" :options="options" />
                </div>
              </div>
            </v-col>
            <v-col cols="4" style="height: 100%; padding-top: 4%">
              <div
                v-if="menteeName"
                align="center"
                justify="center"
                style="height: 350px"
              >
                <div
                  style="width: 100%; font-weight: bold"
                  align="left"
                  class="mb-1"
                >
                  <v-icon color="black">mdi-note-text-outline</v-icon>메모장
                </div>
                <v-textarea
                  hide-details
                  v-model="memo"
                  placeholder="상담 내용을 기록하세요"
                  outlined
                  rows="11"
                  color="black"
                >
                </v-textarea>
              </div>
              <div style="height: 100px">
                <v-row>
                  <div
                    style="width: 50%; height: 100%"
                    align="center"
                    class="my-auto"
                  >
                    <div
                      style="
                        width: 85%;
                        border-radius: 20px;
                        font-size: 0.9rem;
                        background-color: #ff7987;
                        color: white;
                      "
                      class="py-2"
                    >
                      <v-icon color="white" style="font-size: 1rem" class="mr-1"
                        >mdi-check-circle-outline</v-icon
                      >실시간 감정 상태와<br />작성한 상담 내용은<br />
                      일지관리에 저장됩니다.
                    </div>
                  </div>
                  <div style="width: 50%">
                    <WebRTC
                      class=""
                      ref="webrtc"
                      :roomId="roomId"
                      cameraHeight="140"
                      @error="onError"
                      @childs-event="parentsMethod"
                      @isLeave="menteeLeave()"
                    />
                    <span style="font-size: 0.9rem">
                      청소년에게 보여지는 화면</span
                    >
                  </div>
                </v-row>
              </div>
            </v-col>
          </v-row>
        </div>
      </div>
    </v-dialog>

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
     <v-snackbar
      v-model="errorSnack"
      top
      flat
      color="error"
      rounded="pill"
      :timeout="2000"
    > <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
    <v-snackbar
      v-model="primarySnack"
      top
      flat
      color="primary"
      rounded="pill"
      :timeout="60000"
    >
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/Cwebrtc.vue";
import Bar from "@/components/webRTC/Bar.js";
import * as io from "socket.io-client";
window.io = io;
import { mapGetters } from "vuex";
import http from "@/util/http-common.js";
import axios from "axios";
export default {
  name: "CWebRTCComp",
  components: {
    WebRTC,
    Bar,
  },
  data() {
    return {
      img: null,
      roomId: null,
      message: null,
      chartData: null,
      emotion: [0, 0, 0, 0, 0, 0, 0],
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                max: 100,
                min: 0,
              },
            },
          ],
        },
      },
      allEmotion: {
        angry: "",
        disgusted: "",
        fearful: "",
        happy: "",
        neutral: "",
        sad: "",
        surprised: "",
      },
      isProgress: false,
      memo: "",
      title: "",
      title2: "",
      items: [
        "시험 성적 때문에 고민이에요.",
        "진로에 대한 고민이 있어요.",
        "괴롭힘을 당하고 있어요.",
        "친구문제로 상담을 받고 싶어요.",
        "직접 입력",
      ],
      isSelf: false,
      menteeNum: null,
      menteeName: null,
      dialog: true,
      successSnack: false,
      primarySnack: false,
      errorSnack:false,
      altMsg: "",
      isSangdam: false,
    };
  },
  mounted() {
    if (this.getRole != `mentor`) {
      this.$router.push("/");
    }
    this.roomId = this.$route.params.room;
    this.fillData();
  },
  methods: {
    sangdamStart() {
      http
        .get(`/counseling/isRoom/${this.getUserNum}/${this.$route.params.num}`)
        .then((res) => {
          if (res.data != "fail") {
            this.successSnack = true;
            this.altMsg = "상담을 시작합니다.";
            this.isSangdam = true;
            setTimeout(() => {
              this.onJoin();
            }, 1000);
          } else {
            this.errorSnack = true;
            this.altMsg = "이미 상담 중입니다. 다른 학생과 상담하세요.";
            setTimeout(() => {
              this.$router.push("/");
            }, 1500);
          }
        });
    },
    onJoin() {
      this.isProgress = true;
      http.put(
        `/counseling/joinLive/${this.$route.params.num}/${this.getUserNum}`
      );
      http
        .get(`/counseling/liveMenteeInfo/${this.$route.params.num}`)
        .then((res) => {
          this.menteeNum = res.data.num;
          this.menteeName = res.data.name;
        });
      this.$refs.webrtc.join();
      this.unsubscribe();
    },
    onLeave() {
      if (this.title == "직접 입력") {
        this.title = this.title2;
      }
      http.put(`/counseling/finishLive/${this.$route.params.num}`, {
        memo: this.memo,
      });
      http.post(`/counseling/saveEmotion`, {
        num: this.$route.params.num,
        angry: this.allEmotion.angry,
        disgusted: this.allEmotion.disgusted,
        fearful: this.allEmotion.fearful,
        happy: this.allEmotion.happy,
        neutral: this.allEmotion.neutral,
        sad: this.allEmotion.sad,
        surprised: this.allEmotion.surprised,
      });
      this.$refs.webrtc.leave();
      this.successSnack = true;
      this.altMsg = "상담 내용이 저장되었습니다.";
      this.$router.push(`/myMenteeInfo/${this.menteeNum}&${this.menteeName}`);
    },
    onError(error, stream) {
      console.log("On Error Event", error, stream);
    },
    sendMessage() {
      this.$refs.webrtc.sendMessage(this.message);
    },
    parentsMethod: function (message) {
      this.emotion = [
        Math.floor(message.angry * 100),
        Math.floor(message.disgusted * 100),
        Math.floor(message.fearful * 100),
        Math.floor(message.happy * 100),
        Math.floor(message.neutral * 100),
        Math.floor(message.sad * 100),
        Math.floor(message.surprised * 100),
      ];
      this.allEmotion.angry = this.allEmotion.angry.concat(
        this.emotion[0],
        `|`
      );
      this.allEmotion.disgusted = this.allEmotion.disgusted.concat(
        this.emotion[1],
        `|`
      );
      this.allEmotion.fearful = this.allEmotion.fearful.concat(
        this.emotion[2],
        `|`
      );
      this.allEmotion.happy = this.allEmotion.happy.concat(
        this.emotion[3],
        `|`
      );
      this.allEmotion.neutral = this.allEmotion.neutral.concat(
        this.emotion[4],
        `|`
      );
      this.allEmotion.sad = this.allEmotion.sad.concat(this.emotion[5], `|`);
      this.allEmotion.surprised = this.allEmotion.surprised.concat(
        this.emotion[6],
        `|`
      );
      // console.dir(this.allEmotion);
      this.fillData();
    },
    fillData() {
      this.chartData = {
        labels: ["화남", "역겨움", "두려움", "행복", "무표정", "슬픔", "놀람"],
        datasets: [
          {
            label: "실시간 감정 정보",
            backgroundColor: [
              "#031926",
              "#468189",
              "#77ACA2",
              "#9DBEBB",
              "#F4E9CD",
              "#E9D758",
              "#FF8552",
            ],
            data: this.emotion,
          },
        ],
      };
    },
    unsubscribe() {
      this.$store.commit("changeIsReady", false);
      this.unsubscribeTokenToTopic(this.getDeviceID);
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
    menteeLeave() {
      this.primarySnack = true;
      this.altMsg = "청소년이 상담을 종료했습니다.";
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
      "getDeviceID",
    ]),
  },
  watch: {
    title(v) {
      if (v == "직접 입력") {
        this.isSelf = true;
      } else {
        this.isSelf = false;
      }
    },
  },
};
</script>
<style>
.webRtc-back {
  background-image: url("../../assets/webBack.png");
  background-size: cover;
}
.heartbeat {
  -webkit-animation: heartbeat 2s ease-in-out infinite both;
  animation: heartbeat 2s ease-in-out infinite both;
}
@-webkit-keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
@keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
</style>