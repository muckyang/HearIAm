<template>
  <div class="record-main justify-content-center p-5">
  <div>
      <audio :src="getAudio(record.recordDir)" controls> </audio>
      <!-- <audio src="../../assets/record/656850999record.wav" controls> </audio> -->
      <!-- <audio :src="`../../assets/record/${record.recordDir}`" controls> </audio> -->
  </div>
  <div>
      감정 그래프 넣는 자리
  </div>
  </div>
</template>

<script>
import http from "@/util/http-common.js";

export default {
  name: "RecordList",
  data() {
    return {
      record: [],
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
  },
  methods: {
    getAudio(audio){
        return "http://localhost:3000/record/"+audio;
    }
  },
};
</script>

<style scoped>
.record-main {
  background-image: linear-gradient(to bottom, #93dfff, #e0f6ff);
  height: 100%;
}
</style>
