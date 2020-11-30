<template>
  <div class="record-main container">
    <v-sheet color="#bbcfe9" class="pa-5" rounded="xl">
      <h2 align="center">음성 상담에 대한 답변</h2>
      <v-row style="height: 70%">
        <v-col cols="6">
          <div align="center">
            <Bar :chartData="chartData" :options="options" style="width: 95%" />
          </div>
        </v-col>
        <v-col cols="6" class="pb-0 pt-10">
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
        </v-col>
      </v-row>
      <v-row>
        <v-col class="col-lg-6 col-md-6 col-sm-12 col-xs-12"></v-col>
        <v-col class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
          <v-btn @click="loadTransform()" v-if="!isPlay">재생하기</v-btn>
          <audio :src="getAudio(record.recordDir)" id="audio"></audio>
          <audio id="convert" :controls="isPlay"></audio>
        </v-col>
        <v-col class="mb-6 col-lg-3 col-md-3 col-sm-12 col-xs-12">
          <v-btn @click="send" style="text-align: right">답변하기</v-btn>
        </v-col>
      </v-row>
    </v-sheet>

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
      altMsg: "",
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
    getAudio(audio) {
      // return 'http://localhost:3000/record/' + audio;
      return "../../../record/" + audio;
    },
    send() {
      http
        .post(
          `/record/sendAnswer/${this.$route.params.num}/${this.getUserNum}`,
          this.answer
        )
        .then(() => {
          this.successSnack = true;
          this.altMsg = "답변이 완료되었습니다.";
          this.$router.push("/recordList");
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
.record-main {
  margin-top: 80px;
  /* height: 100%; */
}
</style>
