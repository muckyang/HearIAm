<template>
  <div class="record-main d-flex justify-content-center p-5">
  <div>
      <audio src=""> </audio>
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
    
  },
};
</script>

<style scoped>
.record-main {
  background-image: linear-gradient(to bottom, #93dfff, #e0f6ff);
  height: 100%;
}
</style>
