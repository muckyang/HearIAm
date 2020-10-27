<template>
  <div id="app">
    <div>
      <!-- <img alt="Vue logo" src="./assets/logo.png" style="width: 50px; vertical-align: middle"> -->
      <span style="padding: 10px">Vue Daily Scheduler</span>
    </div>
    <div style="width: 700px; margin: 0 auto; padding: 20px">
      <ScheduleTest
        v-model="schedule"
        :dayTable="dayTable"
        bg="#223642"
        bgHover="#84dafc7a"
        bgActive="#84c9fc"
        textColor="#000"
      />
    </div>
    <!-- <div
      style="margin: 20px; padding: 20px; background-color: gray; color: white"
    >
      <code>{{ JSON.stringify(schedule) }}</code>
    </div> -->
  </div>
</template>

<script>
import ScheduleTest from "./ScheduleTest.vue";
export default {
  name: "App",
  components: {
    ScheduleTest,
  },
  data() {
    return {
      dayTable:['월','화','수','목','금','토','일'],
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
};
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
