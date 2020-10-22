<template>
  <!-- <div class="main-back">
    <div
      data-sal="slide-up"
      data-sal-delay="300"
      data-sal-duration="600"
      data-sal-easing="ease"
      align="center"
      class="m-2"
    >
      <h1 style="font-family: 'Luckiest Guy', cursive; font-size: 3rem">
        Hear I Am
      </h1>
    </div>
    <div class="main-back d-flex justify-content-center">
      <div style="width: 5%"></div>
      <div
        data-sal="slide-up"
        data-sal-delay="300"
        data-sal-duration="600"
        data-sal-easing="ease"
        class="py-2 px-2"
        style="height: 90%; width: 90%; border: 3px solid"
      >
        <div class="mb-2 my-auto" style="height: 69%; border: 3px solid; text-align:center;">
          <div>
          </div>
        </div>
        <div class="d-flex justify-content-center" style="height: 30%">
          <v-col class="door d-flex mr-2" style="background: #93dfff"> </v-col>
          <v-col class="door d-flex" style="background: #f5a2bb"> </v-col>
        </div>
      </div>
      <div style="width: 5%"></div>
    </div>
  </div> -->
  <div class="main-back" align="center">
    <div style="padding-top: 200px; height: 100vh">
      <span class="main-title" style="color: #93dfff"> Hear</span
      ><span class="main-title" style="color: #f5a2bb"> I</span
      ><span class="main-title" style="color: #93dfff"> Am </span>
      <br />
      <p>
        홈페이지에 대한 부가설명 홈페이지에 대한 부가설명 홈페이지에 대한
        부가설명
      </p>
      <p>
        홈페이지에 대한 부가설명 홈페이지에 대한 부가설명 홈페이지에 대한
        부가설명 홈페이지에 대한 부가설명
      </p>
      <p>
        홈페이지에 대한 부가설명 홈페이지에 대한 부가설명 홈페이지에 대한
        부가설명
      </p>
      <v-btn
        large
        elevation="4"
        @click="
          $vuetify.goTo('#login');
        "
        class="mt-5"
        style="
          font-weight: bold;
          height: 4rem;
          border-radius: 20px;
          font-size: 1.2rem;
          background: linear-gradient(to right, #93dfff, #f5a2bb);
        "
        >시작하기<v-icon>mdi-cursor-pointer</v-icon></v-btn
      >
    </div>
    <div id="login" style="height: 100vh" v-if="!show">
      <div class="d-flex justify-content-center" style="height: 100%">
        <!-- <div style="width: 5%"></div> -->
        <div style="height: 100%; width: 100%">
          <div
            class="d-flex justify-content-center"
            style="
              height: 100%;
              background: linear-gradient(to right, #93dfff, #f5a2bb);
            "
          >
            <v-col class="my-auto" align="center">
              <div
                data-sal="slide-up"
                data-sal-delay="300"
                data-sal-duration="600"
                data-sal-easing="ease"
              >
                <div class="justify-content-center mb-5" text-align="center">
                  <img
                    src="../assets/student.png"
                    style="width: 220px; height: 250px"
                  />
                  <img
                    src="../assets/student2.png"
                    style="width: 220px; height: 250px"
                  />
                </div>
                <p style="font-size: 3rem">청소년</p>
                <v-btn
                  style="
                    font-weight: bold;
                    height: 3rem;
                    border-radius: 20px;
                    font-size: 1.2rem;
                    background: #ffdc15;
                  "
                  @click="menteeClick = !menteeClick; show = !show" 
                  >로그인</v-btn
                >
              </div>
            </v-col>
            <v-col class="my-auto" align="center"
              ><div
                data-sal="slide-up"
                data-sal-delay="300"
                data-sal-duration="600"
                data-sal-easing="ease"
              >
                <img
                  class="mb-5"
                  src="../assets/consultant.png"
                  style="width: 260px; height: 250px"
                />
                <p style="font-size: 3rem">상담사</p>
                <v-btn
                  style="
                    font-weight: bold;
                    height: 3rem;
                    border-radius: 20px;
                    font-size: 1.2rem;
                    background: #ffdc15;
                  "
                  @click="mentorClick = !mentorClick;show = !show "
                  >로그인</v-btn
                >
              </div>
            </v-col>
          </div>
        </div>
        <!-- <div style="width: 5%"></div> -->
      </div>
    </div>
      <transition name="slide-fade">
        <div v-if="menteeClick" style="height: 100%; width: 100% ;  background: linear-gradient(to right, #93dfff, #f5a2bb);">
          
          <!-- background: linear-gradient(to right, #3D5469, #79B7D1); -->
          <div style="
          height: 100vh" class="d-flex justify-content-center">
             <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/stream_btnm.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
            <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/voice_btn.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
             <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/res_btn.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
          </div>
        </div>
      </transition>

      <transition name="slide-fade2">
        <div v-if="mentorClick" style="height: 100%; width: 100%; background: linear-gradient(to left, #93dfff, #f5a2bb);">
          <div style="
          height: 100vh" class="d-flex justify-content-center">
             <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/t_stream_btn.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
            <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/t_voice_btn.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
             <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/t_report_btn.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
          </div>
        </div>
      </transition>
  </div>
</template>

<script>
import sal from "sal.js";
export default {
  name: "HelloWorld",
  mounted() {
    sal();
  },
  data() {
    return {
      show: false,
      menteeClick: false,
      mentorClick: false,
    };
  },
};
</script>
<style>
@import "../../node_modules/sal.js/dist/sal.css";
/* .main-back {
  width: 100%;
  height: 100%;
  background: #f7d3de;
} */
/* .door {
  box-shadow: 5px 5px 5px grey; 
  border-top-left-radius:40px;
  border-bottom-right-radius:40px; 
} */
.main-back {
  /* background: linear-gradient(to right, #93dfff, #f5a2bb); */
  /* background: linear-gradient(to bottom, #f5e9b3, #f2d867); */
  /* background: #ffdc15 ; */
  width: 100%;
  height: 100%;
  font-family: "yg-jalnan";
}
.main-title {
  font-family: "Capriola", sans-serif;
  font-size: 7rem;
  text-shadow: 4px 2px 2px black;
  font-weight: 700;
}
</style>

<style scoped>
.slide-fade-enter-active {
  transition: all 1s ease;
}
.slide-fade2-enter-active {
  transition: all 1s ease;
}
.slide-fade-enter,
.slide-fade-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}
.slide-fade2-enter,
.slide-fade2-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
</style>