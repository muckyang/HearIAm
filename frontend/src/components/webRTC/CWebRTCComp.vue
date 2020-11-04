<template>
  <div class="container">
    <v-dialog v-model="dialog" persistent max-width="1300">
      <v-card>
        <v-container>
          <v-row style="height: 120px">
            <v-col cols="10">
              <h1 v-if="!menteeName">상담 시작을 눌러주세요!!</h1>
              <h1 v-else>{{ menteeName }}님과 상담중 입니다.</h1>
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="2">
              <WebRTC
                ref="webrtc"
                width="100%"
                :roomId="roomId"
                cameraHeight="120"
                @error="onError"
                @childs-event="parentsMethod"
              />
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-row>
                <v-col cols="4">
                  <Bar :chartData="chartData" :options="options" />
                </v-col>
                <v-col cols="8">
                  <v-row>
                    <v-textarea
                      v-model="memo"
                      label="메모"
                      outlined
                      auto-grow
                      row-height="30"
                      rows="12"
                      background-color="amber lighten-4"
                      color="orange orange-darken-4"
                    >
                    </v-textarea>
                  </v-row>
                </v-col>
              </v-row>
              <v-row>
                <v-spacer></v-spacer>
                <v-btn
                  v-if="!isProgress"
                  type="button"
                  class="btn btn-primary"
                  @click="onJoin"
                >
                  상담 시작
                </v-btn>
                <v-btn
                  v-else
                  type="button"
                  class="btn btn-primary"
                  @click="onLeave"
                >
                  상담 종료
                </v-btn>
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/Cwebrtc.vue";
import Bar from "@/components/webRTC/Bar.js";
import * as io from "socket.io-client";
window.io = io;
import { mapGetters } from "vuex";
import http from "@/util/http-common.js";

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
    },
    onLeave() {
      this.$store.commit("changeIsRemote", false);
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
      alert(`상담 내용이 저장되었습니다.`);
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
      console.dir(this.allEmotion);
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
  },
  computed: {
    ...mapGetters([
      "isProfileLoaded",
      "getRole",
      "getQualification",
      "getUserName",
      "getUserNum",
      "getUserID",
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