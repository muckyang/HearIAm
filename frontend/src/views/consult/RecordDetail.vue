<template>
  <div class="record-main justify-content-center p-5">
    <v-row justify="center">
      <v-col cols="3" style="height: 270px">
        <Bar :chartData="chartData" :options="options" />
      </v-col>
    </v-row>
    <div>
      <audio :src="getAudio(record.recordDir)" controls id="audio"></audio>
    </div>
    <div align="center">
      <div style="width: 50%">
        <v-textarea
          solo
          rounded
          v-model="answer"
          outlined
          rows="10"
          style="white-space:pre-line"
          placeholder="상담 내용을 입력해주세요."
        ></v-textarea>
        <div align="right">
          <v-btn @click="send">답변하기</v-btn>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import Bar from "@/components/webRTC/Bar.js";

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
    let audio = document.getElementById("audio");
    audio.addEventListener("timeupdate", function () {
      that.playtime = audio.currentTime.toFixed();
    });
  },
  methods: {
    getAudio(audio) {
      return "http://localhost:3000/record/" + audio;
      // return "http://localhost:8081/record/" + audio;
    },
    send() {
      // var ans = JSON.stringify(this.answer);
      var ans = this.answer;
      http
        .post(`/record/sendAnswer/${this.$route.params.num}`, ans)
        .then(() => {
          alert("답변이 완료되었습니다.");
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
};
</script>

<style scoped>
.record-main {
  background-image: linear-gradient(to bottom, #93dfff, #e0f6ff);
  height: 100%;
}
</style>
