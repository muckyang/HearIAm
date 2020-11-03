<template>
  <div class="container" style="height: 100%; width: 60%;margin-top:60px;">
      <v-card-title class="text-center justify-center py-6">
        <h2 class="font-weight-bold">
          마이페이지
        </h2>
      </v-card-title>

      <v-tabs v-model="tab" background-color="#bbcfe9" color="black" grow>
        <v-tab>나의 일정</v-tab>
        <v-tab>내게 온 녹화</v-tab>
        <v-tab>1:1 예약</v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item>
          <div style="width: 700px; margin: 0 auto; padding: 20px">
            <Scheduler v-model="schedule" :dayTable="dayTable" bg="#223642" bgHover="#84dafc7a" bgActive="#84c9fc" textColor="#000" />
          </div>
        </v-tab-item>
        <v-tab-item>
          <div>
            <ReRecordListComp />
          </div>
        </v-tab-item>
        <v-tab-item>
          <div>
            <ReserveListComp />
          </div>
        </v-tab-item>
      </v-tabs-items>
    
  </div>
</template>

<script>
import Scheduler from '../../components/mentor/Scheduler.vue';
import ReRecordListComp from '../../components/mentor/ReRecordListComp.vue';
import ReserveListComp from '../../components/mentor/ReserveListComp.vue';
export default {
  name: 'MentorMypage',
  components: {
    Scheduler,
    ReRecordListComp,
    ReserveListComp,
  },
  data() {
    return {
      dayTable: ['일', '월', '화', '수', '목', '금', '토'],
      schedule: {
        0: [],
        1: [],
        2: [],
        3: [],
        4: [],
        5: [],
        6: [],
      },
      tab: null,
    };
  },
};
</script>

<style>
#app {
  font-family: 'S-CoreDream-5Medium';
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
