<template>
  <div style="height: 100%; width: 100%; background: linear-gradient(to left, #93dfff, #f5a2bb);">
    마이페이지

    <div>
      <!-- <img alt="Vue logo" src="./assets/logo.png" style="width: 50px; vertical-align: middle"> -->
      <span style="padding: 10px">Vue Daily Scheduler</span>
    </div>
    <div style="width: 700px; margin: 0 auto; padding: 20px">
      <Scheduler
        v-model="schedule"
        :dayTable="dayTable"
        bg="#223642"
        bgHover="#84dafc7a"
        bgActive="#84c9fc"
        textColor="#000"
      />
    </div>
    <div>
      <ReRecordListComp />
    </div>
  </div>
</template>

<script>
import Scheduler from "../../components/mentor/Scheduler.vue";
import ReRecordListComp from "../../components/mentor/ReRecordListComp.vue";
export default {
  name:"MentorMypage",
  components: {
    Scheduler,
    ReRecordListComp,
  },
  data() {
    return {
      dayTable:['일','월','화','수','목','금','토'],
      schedule: {
        0: [],
        1: [],
        2: [],
        3: [],
        4: [],
        5: [],
        6: [],
      },
    };
  },
}
</script>

<style>
#app {
  font-family: "S-CoreDream-5Medium";
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>