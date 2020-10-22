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
                  @click="loginModal(`mentee`)"
                  >로그인</v-btn>
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
                  style="width: 300px; height: 250px"
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
                  @click="loginModal(`mentor`)"
                  >로그인</v-btn
                >
                <!-- @click="mentorClick = !mentorClick;show = !show " -->
              </div>
            </v-col>
          </div>
        </div>
      </div>
    </div>
      
    <v-dialog v-model="login_dialog" max-width="500" min-height="700">
      <v-card
        v-if="role == 'mentee'"
        style="padding: 50px; background: #93dfff"
      >
        <v-card-title class="text-center justify-center p-8">
          <p style="font-family: 'Capriola'; font-size: 3rem; font-weight: 700">
            Hear I Am
          </p>
        </v-card-title>
        <Login :role="role" />
        <div align="right">
      <v-btn text class="px-0 mt-2 mr-2">비회원이용</v-btn>
      <v-btn text class="px-0 mt-2" @click="goSignUpModal(role)">회원가입</v-btn>
      </div>
      </v-card>
      <v-card v-else style="padding: 50px; background: #f5a2bb">
        <v-card-title class="text-center justify-center p-8">
          <p style="font-family: 'Capriola'; font-size: 3rem; font-weight: 700">
            Hear I Am
          </p>
        </v-card-title>
        <Login :role="role" />
        <div align="right">
      <v-btn text class="px-0 mt-2 mr-2">비회원이용</v-btn>
      <v-btn text class="px-0 mt-2" @click="goSignUpModal(role)">회원가입</v-btn>
      </div>
      </v-card>
    </v-dialog>
    <v-dialog
        v-model="sign_dialog"
        max-width="800"
      >
        <v-card style="padding:50px;">
          <SignUpModal :role="role" />
        </v-card>
      </v-dialog>
  </div>
</template>

<script>
import sal from "sal.js";
// import LoginModal from "@/components/account/LoginModal.vue";
import Login from "@/components/account/Login.vue";
import SignUpModal from '@/components/account/SignUpModal.vue';

export default {
  name: "HelloWorld",
  mounted() {
    sal();
  },
  components: {
    // LoginModal,
    Login,
    SignUpModal,
  },
  data() {
    return {
      login_dialog: false,
      sign_dialog: false,
      role: "",
    };
  },
  methods: {
    loginModal(role) {
      this.role = role;
      this.login_dialog = true;
    },
    goSignUpModal(role){
      this.login_dialog=false;
      this.role = role;
      this.sign_dialog = true;
    }
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
