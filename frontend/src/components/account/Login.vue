<template>
  <div class="col-md-12 p-0">
    <v-form ref="form">
      <v-text-field
        class="mb-1"
        v-model="userId"
        hide-details
        outlined
        required
        placeholder="아이디를 입력하세요."
        @keypress.enter="loginRequest"
      />

      <v-text-field
        v-model="password"
        type="password"
        hide-details
        outlined
        placeholder="비밀번호를 입력하세요."
        required
        class="mb-7"
        @keypress.enter="loginRequest"
      />

      <v-btn
        class="main-btn"
        width="100%"
        @click="loginRequest"
        style="height: 3rem"
        >로그인</v-btn
      >
    </v-form>

    <v-snackbar v-model="match" top right flat color="error" :timeout="2000"
      ><p
        style="
          margin-bottom: 0;
          font-weight: bold;
          font-size: 1rem;
          word-spacing: 2px;
          letter-spacing: 2px;
        "
      >
        아이디와 비밀번호를 확인해주세요.
      </p></v-snackbar
    >
  </div>
</template>

<script>
import { AUTH_REQUEST } from "@/store/actions/auth";
import http from "@/util/http-common.js";

export default {
  name: "Login",
  props: {
    login_dialog: { type: Boolean },
  },
  components: {},
  data() {
    return {
      userId: "",
      password: "",
      match: false,
      dialog: false,
    };
  },
  methods: {
    loginRequest() {
      http.get(`user/users/${this.userId}`).then((res) => {
        // if (res.data.role != this.role) {
        //   this.match = true;
        //   this.userId = "";
        //   this.password = "";
        // } else {
        const { userId, password } = this;
        this.$store
          .dispatch(AUTH_REQUEST, { userId, password })
          .then((res) => {
            this.setDeviceId(this.userId);

            this.userId = "";
            this.password = "";
            this.nowlogin = !this.nowlogin;

            if (res.data.role == `mentee`) {
              //청소년 페이지로 가주세요
              this.$router.push("/menteeMain");
            } else {
              //상담사 페이지로 가주세요
              this.$router.push("/mentorMain");
            }
          })
          .catch(() => {
            this.match = true;
            this.userId = "";
            this.password = "";
            this.$router.push;
          });
        // }
      })
    },
    setDeviceId(userId) {
      //device id 가져옴.
      let devecieId = this.$store.getters["getDeviceID"];
      console.log("store - " + devecieId);
      http
        .put(`/user/userDId/${userId}/${devecieId}`)
        .then((res) => {
          console.dir(res);
          console.log("res.id : " + res.data);
          localStorage.setItem("deviceID", res.data);
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
  watch: {
    login_dialog(v) {
      if (!v) {
        this.userId = "";
        this.password = "";
      }
    },
  },
};
</script>

<style>
.main-btn {
  background: #bbcfe9 !important;
  font-weight: bold !important;
  color: white !important;
  font-size: 1.1rem !important;
}
body {
  -ms-overflow-style: none;
}
::-webkit-scrollbar {
  display: none;
}
</style>
