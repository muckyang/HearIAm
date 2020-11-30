<template>
  <div>
    <v-list-item-content @click="goWebRTC()">
      <v-list-item-title>{{ menteeName }}</v-list-item-title>
      <v-list-item-subtitle>{{ setTime() }}</v-list-item-subtitle>
    </v-list-item-content>
  </div>
</template>

<script>
import http from "@/util/http-common.js";

export default {
  name: "LiveListComp",
  props: {
    num: { type: Number },
    mentor: { type: Number },
    mentee: { type: Number },
    room: { type: String },
    date: { type: String },
    status: { type: String },
  },
  data() {
    return {
      menteeName: null,
    };
  },
  created() {
    this.findName();
  },
  methods: {
    goWebRTC() {
      this.$router.push(`/counselorWRTC/${this.room}&${this.num}`);
    },
    setTime: function () {
      let today = new Date();
      let timeValue = new Date(
        Number(this.date.slice(0, 4)),
        Number(this.date.slice(5, 7) - 1),
        Number(this.date.slice(8, 10)),
        Number(this.date.slice(11, 13)),
        Number(this.date.slice(14, 16)),
        Number(this.date.slice(17, 19)),
        0
      );

      timeValue.setHours(timeValue.getHours() + 9);
      let betweenTime = Math.floor(
        (today.getTime() - timeValue.getTime()) / 1000 / 60
      );
      if (betweenTime < 1) return "방금전";
      if (betweenTime < 60) {
        return `${betweenTime}분전`;
      }

      const betweenTimeHour = Math.floor(betweenTime / 60);
      if (betweenTimeHour < 24) {
        return `${betweenTimeHour}시간전`;
      }

      const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
      if (betweenTimeDay < 365) {
        return `${betweenTimeDay}일전`;
      }
      return this.date;
    },
    findName() {
      http.get(`/user/get/${this.mentee}`).then((res) => {
        this.menteeName = res.data.name;
      });
    },
  },
};
</script>

<style>
</style>