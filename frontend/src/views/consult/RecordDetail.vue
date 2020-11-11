<template>
  <div
    style="
      width: 100%;
      padding-top: 70px;
      background-color: #0f0d2d;
      height: 100%;
    "
    align="center"
  >
    <div class="px-5 pt-5 content-box">
      <div style="position: absolute">
        <span class="icon-line" @click="goBack()"
          ><v-icon style="color: crimson">mdi-arrow-left-thick</v-icon
          >뒤로</span
        >
      </div>
      <h1 align="center">녹음 상담 답변하기</h1>
      <v-row style="height: 70%">
        <v-col cols="5">
          <div align="center">
            <Bar :chartData="chartData" :options="options" />
          </div>
        </v-col>
        <v-col cols="7" class="pb-0 pt-10">
          <div align="left" class="ml-3 mb-2" style="font-size: 0.9rem">
            <span>* 재생하기 버튼을 눌러 녹음 내용을 들은 후, 답변을 입력해주세요.</span
            >
          </div>
          <v-textarea
            solo
            rounded
            v-model="answer"
            no-resize
            rows="14"
            style="white-space: pre-line"
            placeholder="상담 내용을 입력해주세요."
            class="mb-0"
          ></v-textarea>
          <v-row justify="center">
            <v-col class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
              <v-btn
                @click="loadTransform()"
                v-if="!isPlay"
                class="main-btn"
                large
                >재생하기</v-btn
              >
              <audio :src="getAudio(record.recordDir)" id="audio"></audio>
              <audio id="convert" :controls="isPlay"></audio>
            </v-col>
            <v-col class="mb-6 col-lg-5 col-md-5 col-sm-12 col-xs-12">
              <v-btn @click="sendD" class="main-btn" large>답변완료</v-btn>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <v-dialog v-model="sendDialog" persistent max-width="400">
        <v-card>
          <v-card-title style="font-size: 1.5rem">
            답변을 완료하시겠습니까?
          </v-card-title>
          <v-card-text></v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="#262272" text @click="sendDialog = false">
              아니오
            </v-btn>
            <v-btn color="#262272" text @click="send"> 예 </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>

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
      v-model="failSnack"
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
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import Bar from "@/components/webRTC/Bar.js";
import bufferToWav from "audiobuffer-to-wav";
import { mapGetters, mapState } from "vuex";

export default {
  name: "RecordList",
  components: {
    Bar,
  },
  data() {
    return {
      record: [],
      answer: "",
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
      chartData: null,
      emotion: [0, 0, 0, 0, 0, 0, 0],
      angry: {},
      disgusted: {},
      fearful: {},
      happy: {},
      neutral: {},
      sad: {},
      surprised: {},
      playtime: null,
      isPlay: false,
      successSnack: false,
      failSnack: false,
      altMsg: "",
      sendDialog: false,
    };
  },
  created() {
    http
      .get(`/record/getRecordDetail/${this.$route.params.num}`)
      .then((res) => {
        this.record = res.data;
      })
      .catch((err) => {
        console.log(err);
      });
    http
      .get(`/counseling/loadEmotion/${this.$route.params.num}`)
      .then((res) => {
        this.angry = res.data.angry.split(`|`).map(Number);
        this.disgusted = res.data.disgusted.split(`|`).map(Number);
        this.fearful = res.data.fearful.split(`|`).map(Number);
        this.happy = res.data.happy.split(`|`).map(Number);
        this.neutral = res.data.neutral.split(`|`).map(Number);
        this.sad = res.data.sad.split(`|`).map(Number);
        this.surprised = res.data.surprised.split(`|`).map(Number);
      });
    this.fillData();
  },
  mounted() {
    let that = this;
    let audio = document.getElementById("convert");
    audio.addEventListener("timeupdate", function () {
      that.playtime = audio.currentTime.toFixed();
    });
  },
  methods: {
    goBack() {
      window.history.back();
    },
    getAudio(audio) {
      // return 'http://localhost:3000/record/' + audio;
      return "../../../record/" + audio;
    },
    sendD() {
      if (!this.isPlay) {
        this.failSnack = true;
        this.altMsg = "녹음 내용을 먼저 확인해주세요.";
      } else if (this.answer.length == 0) {
        this.failSnack = true;
        this.altMsg = "답변을 입력해주세요.";
      } else {
        this.sendDialog = true;
      }
    },
    send() {
      this.sendDialog = false;
      http
        .post(
          `/record/sendAnswer/${this.$route.params.num}/${this.getUserNum}`,
          this.answer
        )
        .then(() => {
          this.successSnack = true;
          this.altMsg = "답변이 완료되었습니다.";
          setTimeout(() => {
            this.$router.push("/recordList").catch((err) => {
              console.log(err);
            });
          }, 1500);
        })
        .catch((err) => {
          console.log(err);
        });
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

    async loadTransform() {
      this.isPlay = true;
      let arrayBuffer = await (
        await fetch(this.getAudio(this.record.recordDir))
      ).arrayBuffer();

      let AudioBuffer = await new AudioContext().decodeAudioData(arrayBuffer);

      let outputAudioBuffer = this.robot1Transform(AudioBuffer);

      outputAudioBuffer.then(function (result) {
        var anchor = document.getElementById("convert");

        var wav = bufferToWav(result);
        var blob = new window.Blob([new DataView(wav)], {
          type: "audio/wav",
        });

        var url = window.URL.createObjectURL(blob);
        anchor.src = url;
        anchor.play();
      });
    },

    async robot1Transform(audioBuffer) {
      let ctx = new OfflineAudioContext(
        audioBuffer.numberOfChannels,
        audioBuffer.length,
        audioBuffer.sampleRate
      );

      // Source
      let source = ctx.createBufferSource();
      source.buffer = audioBuffer;

      // Wobble
      let oscillator1 = ctx.createOscillator();
      oscillator1.frequency.value = 10;
      oscillator1.type = "sawtooth";
      let oscillator2 = ctx.createOscillator();
      oscillator2.frequency.value = 30;
      oscillator2.type = "sawtooth";
      let oscillator3 = ctx.createOscillator();
      oscillator3.frequency.value = 10;
      oscillator3.type = "sawtooth";
      // ---
      let oscillatorGain = ctx.createGain();
      oscillatorGain.gain.value = 0.004;
      // ---
      let delay = ctx.createDelay();
      delay.delayTime.value = 0.01;

      // Create graph
      oscillator1.connect(oscillatorGain);
      oscillator2.connect(oscillatorGain);
      // oscillator3.connect(oscillatorGain);
      oscillatorGain.connect(delay.delayTime);
      // ---
      source.connect(delay);
      delay.connect(ctx.destination);

      // Render
      oscillator1.start(0);
      oscillator2.start(0);
      oscillator3.start(0);
      source.start(0);
      // fire.start(0);
      let outputAudioBuffer = await ctx.startRendering();
      return outputAudioBuffer;
    },
  },
  watch: {
    playtime(time) {
      this.emotion[0] = this.angry[time];
      this.emotion[1] = this.disgusted[time];
      this.emotion[2] = this.fearful[time];
      this.emotion[3] = this.happy[time];
      this.emotion[4] = this.neutral[time];
      this.emotion[5] = this.sad[time];
      this.emotion[6] = this.surprised[time];
      this.fillData();
    },
  },
  computed: {
    ...mapGetters(["getUserNum", "getUserID"]),
    ...mapState({
      userNum: (state) => `${state.user.getUserNum}`,
      userID: (state) => `${state.user.getUserID}`,
    }),
  },
};
</script>

<style scoped>
</style>
