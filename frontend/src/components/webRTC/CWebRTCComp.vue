<template>
  <div style="height: 100%; width: 100%" class="webRtc-back">
    <v-dialog v-model="dialog" persistent max-width="1300">
      <v-card style="background-color: white; border: 1px solid white" outlined>
        <v-container v-if="!menteeName" style="opacity: 1">
          <h1 class="mt-15">
            상담 준비가 완료 되었다면 <br />
            시작버튼을 눌러주세요.
          </h1>
          <v-btn
            v-if="!isProgress"
            style="font-size: 0.9rem; color:white"
             color="#262272"
            class="btn btn-primary mt-5"
            @click="onJoin"
          >
            상담 시작
          </v-btn>
        </v-container>
        <v-container>
          <v-row align="center"
             justify="center">
            <v-col cols="4" v-if="menteeName"> 
              <h2>{{ menteeName }}님과 상담중 입니다.</h2>
              <p>상담이 끝났다면 버튼을 눌러주세요.</p>
              <v-btn
                v-if="isProgress"
                style="font-size: 0.9rem; color:white"
                color="#262272"
                class="btn btn-primary"
                @click="onLeave"
              >
                상담 종료
              </v-btn>
            </v-col>
            <v-divider vertical v-if="menteeName" ></v-divider>
            <v-col cols="7">
              <v-row >
                <v-col cols="5" v-if="menteeName"  class="ml-10" >
                  <Bar :chartData="chartData" :options="options" />
                </v-col>
                <v-col cols="4">
                  <WebRTC
                    class="mt-5"
                    ref="webrtc"
                    width="100%"
                    :roomId="roomId"
                    cameraHeight="200"
                    @error="onError"
                    @childs-event="parentsMethod"
                  />
                </v-col>
              </v-row>
              <v-row
                style="height: 250px"
                v-if="menteeName"
                align="center"
                justify="center"
              >
                <v-col cols="10" class="mt-8">
                  <v-textarea
                    v-model="memo"
                    label="memo"
                    outlined
                    row-height="100"
                    color="black"
                  >
                  </v-textarea>
                </v-col>
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
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
      altMsg: "",
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
        labels: [
          "angry",
          "disgusted",
          "fearful",
          "happy",
          "neutral",
          "sad",
          "surprised",
        ],
        datasets: [
          {
            label: "Emotion",
            backgroundColor: "#f87979",
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
      http.delete(`/counseling/deleteReadyMentor/${num}`).then(() => {});
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
</style>