<template>
  <div align="center" class="main-back" id="main-first" style="height:100vh;">
    <!-- 소개글 -->
    <div class="mb-0 first-text" id="fade-out">
      <h1 class="fade-in" style="font-size:5rem;color:white;margin-top:300px;">다 들어줄게, 난 항상 네 옆에 있어</h1>
    </div>

    <div v-show="mainFlag" style="padding-top:10%;">
      <span class="main-title">Hear I Am</span><br />
      <!-- <span class="main-title" style="font-size:2rem !important; font-family:S-CoreDream-5Medium !important"> 들어줄게, 너의 곁에 있어줄게</span> -->
      <br />
      <div class="swap-on-hover">
        <img class="swap-on-hover__front-image" src="../assets/lock.png" />
        <img class="swap-on-hover__back-image" src="../assets/unlock.png" @click="login_dialog = true" />
      </div>
    </div>

    <v-dialog v-model="login_dialog" max-width="500" min-height="700">
      <v-card rounded style="padding: 50px;">
        <v-card-title class="text-center justify-center p-8">
          <p style="font-family: 'Short Stack', cursive; font-size: 4vw; font-weight: 700">
            Hear I Am
          </p>
        </v-card-title>
        <Login :login_dialog="login_dialog" />
        <div align="right">
          <v-btn text class="px-0 mt-2 mr-2">비회원이용</v-btn>
          <v-btn text class="px-0 mt-2" @click="goSignUpModal()">회원가입</v-btn>
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
import sal from 'sal.js';
// import LoginModal from "@/components/account/LoginModal.vue";
import Login from '@/components/account/Login.vue';
import SignUpModal from '@/components/account/SignUpModal.vue';
import { mapGetters } from 'vuex';

export default {
  name: 'HelloWorld',
  mounted() {
    sal();
    if (this.getRole == `mentee`) {
      this.$router.push('/menteeMain');
    } else if (this.getRole == `mentor`) {
      this.$router.push('/mentorMain');
    } else {
      this.fadeOut();
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
      role: '',
      mainFlag: false,
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
    fadeOut() {
      setTimeout(() => {
        document.getElementById('fade-out').setAttribute('style', 'display:none');
        this.mainFlag = true;
      }, 4000);
    },
  },
  computed: {
    ...mapGetters(['isProfileLoaded', 'getRole', 'getQualification', 'getUserName', 'getUserNum', 'getUserID']),
  },
};
</script>
<style>
@import '../../node_modules/sal.js/dist/sal.css';
.main-back {
  background-image: url('../assets/mainStarLazy.png');
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
.swap-on-hover {
  position: relative;
  max-width: 250px;
  max-height: 250px;
}

.swap-on-hover img {
  position: absolute;
  left: 0;
  max-width: 250px;
  max-height: 250px;
}

.swap-on-hover .swap-on-hover__front-image,
.swap-on-hover__back-image {
  /* z-index: 9999; */
  transition: opacity 0.5s linear;
  cursor: url('../assets/unkey.png'), default;
}

.swap-on-hover:hover > .swap-on-hover__front-image {
  opacity: 0;
}

.swap-on-hover__back-image {
  opacity: 0;
}

.swap-on-hover:hover > .swap-on-hover__back-image {
  opacity: 100;
}
.fade-in {
  -webkit-animation: fadeIn 3s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: fadeIn 3s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
.first-text {
  position: absolute;
  background-size: cover;
  height: 97%;
  width: 100%;
  background-image: url('../assets/mainStarLazy.png');
  z-index: 1;
}
#main-first {
  cursor: url('../assets/key.png'), default;
}
@-webkit-keyframes fadeIn {
  0% {
    letter-spacing: -0.5em;
    -webkit-transform: translateZ(-800px);
    transform: translateZ(-800px);
    -webkit-filter: blur(12px);
    filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateZ(0);
    transform: translateZ(0);
    -webkit-filter: blur(0);
    filter: blur(0);
    opacity: 1;
  }
}
@keyframes fadeIn {
  0% {
    letter-spacing: -0.5em;
    -webkit-transform: translateZ(-800px);
    transform: translateZ(-800px);
    -webkit-filter: blur(12px);
    filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateZ(0);
    transform: translateZ(0);
    -webkit-filter: blur(0);
    filter: blur(0);
    opacity: 1;
  }
}
</style>
