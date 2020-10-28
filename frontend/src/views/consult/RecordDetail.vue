<template>
  <div class="record-main justify-content-center p-5">
    <div>
      <audio :src="getAudio(record.recordDir)" controls></audio>
      <!-- <audio src="../../assets/record/656850999record.wav" controls> </audio> -->
      <!-- <audio :src="`../../assets/record/${record.recordDir}`" controls> </audio> -->
    </div>
    <div>감정 그래프 넣는 자리</div>
    <div align="center">
      <div style="width:50%;">
      <v-textarea
        solo
        rounded
        v-model="answer"
        outlined
        rows="10"
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

export default {
  name: "RecordList",
  data() {
    return {
      record: [],
      answer: "",
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
    getAudio(audio) {
      return "http://localhost:3000/record/" + audio;
    },
    send(){
      http
      .post(`/record/sendAnswer/${this.$route.params.num}/${this.answer}`)
      .then((res) => {
        alert("답변이 완료되었습니다.");
        this.$router.push('/recordList');
      })
      .catch((err) => {
        console.log(err);
      });
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
