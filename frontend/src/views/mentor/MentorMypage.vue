<template>
  <div
    id="mypage"
    style="
      width: 100%;
      padding-top: 70px;
      background-color: #0f0d2d;
      padding-bottom:30px;
    "
    align="center"
  >

    <div class="px-5 pt-5 content-box3" style="overflow:scroll;">
      <v-tabs v-model="tab" color="black" grow>
        <v-tab style="font-size: 1.3rem" @click="setH(0)">
          <v-icon class="mr-1" style="font-size: 1.5rem"
            >mdi-calendar-check-outline</v-icon
          >
          이번 주 일정표</v-tab
        >
        <v-tab style="font-size: 1.3rem" @click="setH(1)"
          ><v-icon class="mr-1" style="font-size: 1.5rem"
            >mdi-microphone-outline</v-icon
          >내게 온 녹음상담</v-tab
        >
        <v-tab style="font-size: 1.3rem" @click="setH(1)"
          ><v-icon class="mr-1" style="font-size: 1.5rem"
            >mdi-monitor</v-icon
          >실시간상담 예약내역</v-tab
        >
      </v-tabs>

      <v-tabs-items v-model="tab">
        <div style="padding-top:1rem;">
        </div>
        <div style="position:absolute;">
        <span class="icon-line ml-2" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span>
      </div>
        <v-tab-item>
          <div style="margin: 0 auto; padding: 20px">
            <Scheduler
              v-model="schedule"
              :dayTable="dayTable"
              bg="#e9e8f8"
              bgHover="#4f4c86"
              bgActive="#4f4c86"
              textColor="#000"
            />
        <!-- <div style="position:absolute;bottom:1rem;"><span class="icon-line" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span></div> -->
          </div>
        </v-tab-item>
        <v-tab-item>
          <div>
            <ReRecordListComp />
          </div>
        <!-- <div style="position:absolute;"><span class="icon-line" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span></div> -->
        </v-tab-item>
        <v-tab-item >
          <div>
            <ReserveListComp />
          </div>
        <!-- <div style="position:absolute;bottom:1rem;background-color:white"><span class="icon-line" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span></div> -->
        </v-tab-item>
      </v-tabs-items>

    </div>
  </div>
</template>

<script>
import Scheduler from "../../components/mentor/Scheduler.vue";
import ReRecordListComp from "../../components/mentor/ReRecordListComp.vue";
import ReserveListComp from "../../components/mentor/ReserveListComp.vue";
export default {
  name: "MentorMypage",
  components: {
    Scheduler,
    ReRecordListComp,
    ReserveListComp,
  },
  data() {
    return {
      dayTable: ["일", "월", "화", "수", "목", "금", "토"],
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
  methods:{
    setH(n){
      // console.log("in")
      if(n==1){
        // console.log(n)
      document.getElementById('mypage').style.height="100%";
      }else if(n==0){
        document.getElementById('mypage').style.height="auto";
      }
    },
    goBack(){
      this.$router.push("/")
    }
  }
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

.content-box3{
  width:90%;
  height:98%;
  background: -moz-linear-gradient(top left, #ff7987, #a23bbe);
  background: -webkit-linear-gradient(top left, #ff7987, #a23bbe);
  -moz-background-origin: border;
  background-origin: border-box;
  border: 10px solid transparent;
  border-radius: 25px;
  box-shadow:
    inset 0 0 12px white, /* Inset shadow */
    0 0 12px white, /* Outset shadow */
    inset -2500px 0 0 white; /* The background color */
}
</style>
