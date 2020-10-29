<template>
  <div align ="center" >
    <h1> {{ counseling.title }}</h1>
    <br/>
    <div style="width :500px; height:50px">
      <h2> # {{counseling.keyword1}}  # {{counseling.keyword2}}  # {{counseling.keyword3}} </h2>
    </div>
    <div style="width :400px; height:400px">
        <Doughnut :chartData="chartData" />
    </div>
      <h2>상담 내용</h2>
    <div>
        <v-textarea
        solo
        rounded
        readonly
        v-model="counseling.answer"
        outlined
        rows="7"
      ></v-textarea>
      </div>
  </div>
</template>

<script>
import Doughnut from "@/components/mentor/Doughnut.js";
import http from "@/util/http-common.js";

export default {
  name: "CounselingInfoComp",
  components: {
    Doughnut,
  },
  data() {
    return {
      emotion: {},
      angry: 0,
      disgusted: 0,
      fearful: 0,
      happy: 0,
      neutral: 0,
      sad: 0,
      surprised: 0,
      chartData: null,
      counseling: {},
      date: null,
    };
  },
  created() {
    http
      .get(`/counseling/counseling/${this.$route.params.num}`)
      .then((res) => {
        this.counseling = res.data;
        console.log(this.counseling)
        this.date = this.setTime(this.counseling.date);
      });
    http
      .get(`/counseling/loadEmotion/${this.$route.params.num}`)
      .then((res) => {
        this.emotion = res.data;
        this.angry = this.avrage(this.emotion.angry.split(`|`).map(Number));
        this.disgusted = this.avrage(
          this.emotion.disgusted.split(`|`).map(Number)
        );
        this.fearful = this.avrage(this.emotion.fearful.split(`|`).map(Number));
        this.happy = this.avrage(this.emotion.happy.split(`|`).map(Number));
        this.neutral = this.avrage(this.emotion.neutral.split(`|`).map(Number));
        this.sad = this.avrage(this.emotion.sad.split(`|`).map(Number));
        this.surprised = this.avrage(
          this.emotion.surprised.split(`|`).map(Number)
        );
        this.fillData();
      });
  },
  methods: {
    setTime(date) {
      let time =
        Number(date.slice(0, 4)) +
        "년 " +
        Number(date.slice(5, 7)) +
        "월 " +
        Number(date.slice(8, 10)) +
        "일 " +
        Number(date.slice(11, 13)) +
        "시 " +
        Number(date.slice(14, 16)) +
        "분";
      return time;
    },
    avrage(arr) {
      let result = 0;
      for (let i = 0; i < arr.length; i++) {
        result += arr[i];
      }
      return result / arr.length;
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
            borderColor: ["red", "orange", "#ffff00", "green", "blue", "#000080", "purple"],
            backgroundColor: ["red", "orange", "#ffff00", "green", "blue", "#000080", "purple"],
            data: [
              this.angry,
              this.disgusted,
              this.fearful,
              this.happy,
              this.neutral,
              this.sad,
              this.surprised,
            ],
          },
        ],
      };
    },
  },
};
</script>
 <style>
      #jb-container {
        width: 940px;
        margin: 10px auto;
        padding: 20px;
        border: 1px solid #bcbcbc;
      }
      #jb-header {
        padding: 20px;
        margin-bottom: 20px;
        border: 1px solid #bcbcbc;
      }
      #jb-content {
        width: 580px;
        padding: 20px;
        margin-bottom: 20px;
        float: left;
        border: 1px solid #bcbcbc;
      }
      #jb-sidebar {
        width: 260px;
        padding: 20px;
        margin-bottom: 20px;
        float: right;
        border: 1px solid #bcbcbc;
      }
      #jb-footer {
        clear: both;
        padding: 20px;
        border: 1px solid #bcbcbc;
      }
     
    </style>