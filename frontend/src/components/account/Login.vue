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

    <v-snackbar
      v-model="errorSnack"
      top
      flat
      color="error"
      rounded="pill"
      :timeout="2000"
    >
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
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
      errorSnack: false,
      dialog: false,
      altMsg: "",
    };
  },
  methods: {
    loginRequest() {
      if (this.userId == "" || this.password == "") {
        this.errorSnack = true;
        this.altMsg = "아이디와 비밀번호를 입력해주세요.";
      } else {
        http
          .get(`user/users/${this.userId}`)
          .then((res) => {
            const { userId, password } = this;
            this.$store
              .dispatch(AUTH_REQUEST, { userId, password })
              .then(() => {
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
              .catch((err) => {
                console.log(err);
                this.errorSnack = true;
                this.altMsg = "아이디와 비밀번호를 확인해주세요.";
              });
          })
          .catch((err) => {
            console.log(err);
            this.errorSnack = true;
            this.altMsg = "존재하지 않는 아이디 입니다.";
          });
      }
    },
    setDeviceId(userId) {
      //device id 가져옴.
      let devecieId = this.$store.getters["getDeviceID"];
      // console.log("store - " + devecieId);
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
  background: #262272 !important;
  /* font-weight: bold !important; */
  color: white !important;
  font-size: 1rem !important;
}
body {
  -ms-overflow-style: none;
}
::-webkit-scrollbar {
  display: none;
}
</style>
