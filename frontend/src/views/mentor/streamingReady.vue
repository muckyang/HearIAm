<template>
  <div>
    <v-btn @click="onjoin()"> ready </v-btn>
  </div>
</template>
<script>
import http from "@/util/http-common.js";
export default {
  props: {
    room: {
      type: String,
      default: "",
    },
    room_num: {
      type: Number,
      default: 0,
    },
  },
  methods: {
    onjoin() {
      let mentorName = this.$store.getters['getUserNum'];
      http
        .get(`/counseling/isRoom/${mentorName}/${this.room}`)
        .then((res) => {
          console.log(res.data);
          if (res.data == "fail") {
            alert("이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ! ");
          } else {
            alert(" 상담을 시작합니다. ")
            this.$router.push(`/counselorWRTC/${this.room}&${this.room_num}`);
          }
        })
        .catch((e) => {
          console.log(e);
        });
      
    },
  },
};
</script>