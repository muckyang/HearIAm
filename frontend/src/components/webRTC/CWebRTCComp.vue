<template>
  <div class="container">
    <v-container>
      <v-row>
        <v-col>
          <v-row>
            <v-col>
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
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="5">
              <Bar :chartData="chartData" :options="options" />
            </v-col>
            <v-col>
              <v-row style="height: 220px">
                <v-col cols="6">
                  <WebRTC
                    ref="webrtc"
                    width="100%"
                    :roomId="roomId"
                    cameraHeight="200"
                    @error="onError"
                    @childs-event="parentsMethod"
                  />
                </v-col>
                <v-col cols="6">
                  <v-select
                    class="mt-7"
                    v-model="title"
                    :items="items"
                    label="고민이 무엇인가요?"
                    solo
                  ></v-select>
                  <v-text-field
                    v-if="isSelf"
                    v-model="title2"
                    v-bind="attrs"
                    v-on="on"
                    hint="자유롭게 고민을 입력해주세요."
                    persistent-hint
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <textarea
                  v-model="memo"
                  outlined
                  auto-grow
                  row-height="30"
                  rows="12"
                  style="background-color: white; width: 100%"
                >
                </textarea>
              </v-row>
            </v-col>
          </v-row>
          <v-row v-if="isProgress">
            <v-col> </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
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
      http.get(`/counseling/liveMenteeInfo/${this.$route.params.num}`).then((res)=>{
        this.menteeNum = res.data.num;
        this.menteeName = res.data.name;
      })
      this.$refs.webrtc.join();
    },
    onLeave() {
      if(this.title == "직접 입력"){
        this.title = this.title2;
      }
      http.put(`/counseling/finishLive/${this.$route.params.num}`,{
        title: this.title,
        memo: this.memo
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
      this.$router.push(`/myMenteeInfo/${this.menteeNum}&${this.menteeName}`)
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
      }else{
        this.isSelf = false;
      }
    },
  },
};
</script>