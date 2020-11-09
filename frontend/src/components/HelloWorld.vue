<template>
  <div align="center" class="main-back" style="height:100vh">
    <div style="padding-top:10%;">
      <span class="main-title"> Hear I Am</span><br>
      <!-- <span class="main-title" style="font-size:2rem !important; font-family:S-CoreDream-5Medium !important"> 들어줄게, 너의 곁에 있어줄게</span> -->
      <br>
      <br>
      <br>

      <v-btn
      id="lock"
        large
        @click="login_dialog = true"
        class="mt-5"
        text
        icon
        style="
          height: 4rem;
          font-size: 5rem;
          color:white;
        "
        ><v-icon style="font-size:7rem;">mdi-lock</v-icon></v-btn
      >
    </div>

    <v-dialog v-model="login_dialog" max-width="500" min-height="700">
      <v-card rounded style="padding: 50px;" >
        <v-card-title class="text-center justify-center p-8">
          <p style="font-family: 'Capriola'; font-size: 3rem; font-weight: 700">
            Hear I Am
          </p>
        </v-card-title>
        <Login :login_dialog="login_dialog" />
        <div align="right">
          <v-btn text class="px-0 mt-2 mr-2">비회원이용</v-btn>
          <v-btn text class="px-0 mt-2" @click="goSignUpModal()"
            >회원가입</v-btn
          >
        </div>
      </v-card>
    </v-dialog>
    <v-dialog v-model="sign_dialog" max-width="600">
      <v-card style="padding: 40px 20px 20px 20px" class="student-dialog">
        <SignUpModal :sign_dialog="sign_dialog" />
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import sal from "sal.js";
// import LoginModal from "@/components/account/LoginModal.vue";
import Login from "@/components/account/Login.vue";
import SignUpModal from "@/components/account/SignUpModal.vue";
import { mapGetters } from "vuex";

export default {
  name: "HelloWorld",
  mounted() {
    sal();
    if (this.getRole == `mentee`) {
      this.$router.push("/menteeMain");
    } else if (this.getRole == `mentor`) {
      this.$router.push("/mentorMain");
    }
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
    loginModal() {
      this.login_dialog = true;
    },
    goSignUpModal() {
      this.login_dialog = false;
      this.sign_dialog = true;
    },
  },
  computed: {
    ...mapGetters([
      "isProfileLoaded",
      "getRole",
      "getQualification",
      "getUserName",
      "getUserNum",
      "getUserID",
    ]),
  },
};
</script>
<style>
@import "../../node_modules/sal.js/dist/sal.css";
.main-back {
  background-image: url("../assets/mainStar.png");
  /* background-size: 100% 100%; */
  background-size: cover;
  /* object-fit:fill; */
  /* background: linear-gradient(to right, #93dfff, #f5a2bb); */
  /* background: linear-gradient(to bottom, #f5e9b3, #f2d867); */
  /* background: #ffdc15 ; */
  /* font-family: "yg-jalnan"; */
}
.main-title {
  /* font-family: "Capriola", sans-serif; */
  font-family: 'Short Stack', cursive;
  font-size: 8rem;
  color: white;
  text-shadow: 1px 1px 1px black, 0 0 0.2em white, 0 0 0.1em white;
  font-weight: 600;
}

</style>