<template>
  <div>
    <v-col cols="4">
      <Doughnut :chartData="chartData" />
    </v-col>
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
    };
  },
  created() {
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
</style>