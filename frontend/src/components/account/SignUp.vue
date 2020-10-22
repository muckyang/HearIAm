<template>
  <div class="col-md-12 p-0">
    <v-container id="inspire" class="">
      <v-col class="pr-10 pl-10">
        <v-form ref="form" v-model="valid">
          <v-row justify="center">
            <v-col
              class="text-left pt-8 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              * 아이디
            </v-col>
            <v-col class="col-xl-8 col-lg-8 col-md-8 col-sm-8 col-12">
              <v-text-field
                v-model="id"
                :counter="15"
                :rules="idRules"
                color="#356859"
                placeholder="사용할 아이디를 입력해주세요."
                required
                clearable
              ></v-text-field>
            </v-col>
            <v-col
              class="text-right pt-6 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              <v-btn class="green-mbtn" outlined @click="idCheck()"
                >중복체크</v-btn
              >
            </v-col>
          </v-row>

          <v-row justify="center">
            <v-col
              class="text-left pt-8 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              * 비밀번호
            </v-col>
            <v-col class="col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12">
              <v-text-field
                v-model="password1"
                type="password"
                :counter="20"
                :rules="passwordRules"
                placeholder="비밀번호(영문자, 숫자 포함 8자 이상 입력해주세요.)"
                required
                clearable
                color="#356859"
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row justify="center">
            <v-col
              class="text-left pt-8 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              * 비밀번호 재입력
            </v-col>
            <v-col class="col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12">
              <v-text-field
                v-model="password2"
                type="password"
                :counter="20"
                :rules="passwordRules"
                placeholder="비밀번호를 재입력해주세요."
                required
                clearable
                color="#356859"
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row v-if="role == `mentor`" justify="center">
            <v-col
              class="text-left pt-8 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              * 이름
            </v-col>
            <v-col class="col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12">
              <v-text-field
                v-model="name"
                :counter="15"
                :rules="nameRules"
                placeholder="이름을 입력해주세요."
                color="#356859"
                required
                clearable
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row v-if="role == `mentee`" justify="center">
            <v-col
              class="text-left pt-8 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              * 별명
            </v-col>
            <v-col class="col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12">
              <v-text-field
                v-model="name"
                :counter="15"
                :rules="nameRules"
                placeholder="별명을 입력해주세요."
                color="#356859"
                required
                clearable
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row v-if="role == `mentor`" justify="center">
            <v-col
              class="text-left pt-8 col-xl-2 col-lg-2 col-md-2 col-sm-2 col-12"
            >
              * 상담자격인증
            </v-col>
            <v-col class="col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12">
              인증해주세요
            </v-col>
          </v-row>

          <v-btn
            class="pa-5 col-12"
            :disabled="!valid"
            color="#356859"
            dark
            width="100%"
            @click="validate"
            >회원가입</v-btn
          >
        </v-form>
      </v-col>
      <!-- alert -->
      <v-snackbar v-model="idFlag" top right flat color="error" :timeout="2000">
        <p class="snackText">존재하는 아이디입니다.</p>
      </v-snackbar>
      <v-snackbar v-model="idOk" top right flat color="#356859" :timeout="2000">
        <p class="snackText">사용 가능한 아이디입니다.</p>
      </v-snackbar>
      <v-snackbar v-model="noidOk" top right flat color="error" :timeout="2000">
        <p class="snackText">아이디 중복확인을 해주세요.</p>
      </v-snackbar>
      <v-snackbar v-model="pwdNo" top right flat color="error" :timeout="2000">
        <p class="snackText">비밀번호가 다릅니다.</p>
      </v-snackbar>
      <v-snackbar
        v-model="signOk"
        top
        right
        flat
        color="#356859"
        :timeout="2000"
      >
        <p class="snackText">가입이 완료되었습니다.</p>
      </v-snackbar>
      <v-snackbar
        v-model="pwdLength"
        top
        right
        flat
        color="error"
        :timeout="2000"
      >
        <p class="snackText">
          비밀번호는 숫자와 영문자 조합으로 8~20자리를 사용해야 합니다.
        </p>
      </v-snackbar>
      <v-snackbar
        v-model="pwdEngNum"
        top
        right
        flat
        color="error"
        :timeout="2000"
      >
        <p class="snackText">비밀번호는 숫자와 영문자를 혼용하여야 합니다.</p>
      </v-snackbar>
    </v-container>
  </div>
</template>

<script>
import http2 from "@/util/http-common2.js";
import http from "@/util/http-common.js";

export default {
  name: "SignUp",
  props: {
    role: { type: String },
  },
  data() {
    return {
      id: "",
      name: "",
      password1: "",
      password2: "",
      valid: false,
      nameRules: [
        (v) => !!v || "이름을 입력해주세요",
        (v) =>
          (v && v.length <= 15) ||
          "이름은 최대 15글자까지만 사용할 수 있습니다",
      ],
      idRules: [
        (v) => !!v || "아이디를 입력해주세요",
        (v) =>
          (v && v.length <= 15) ||
          "아이디는 최대 15글자까지만 사용할 수 있습니다",
      ],
      passwordRules: [
        (v) => !!v || "비밀번호를 입력해주세요",
        (v) =>
          (v && v.length <= 20) ||
          "비밀번호는 최대 20글자까지만 사용할 수 있습니다",
      ],
      idFlag: false,
      idOk: false,
      noidOk: false,
      pwdNo: false,
      idCk: false,
      signOk: false,
      pwdLength: false,
      pwdEngNum: false,
    };
  },
  methods: {
    validate() {
      this.$refs.form.validate();
      if (this.password1 == this.password2) {
        var chk_num = this.password1.search(/[0-9]/g);
        var chk_eng = this.password1.search(/[a-z]/gi);
        if (this.idCk == false) {
          this.noidOk = true;
        } else if (!/^[a-zA-Z0-9]{8,20}$/.test(this.password1)) {
          this.pwdLength = true;
        } else if (chk_num < 0 || chk_eng < 0) {
          this.pwdEngNum = true;
        } else {
          this.signUpRequest();
        }
      } else {
        this.pwdNo = true;
      }
    },
    signUpRequest() {
      http2
        .post("/signup", {
          id: this.id,
          name: this.name,
          password: this.password1,
          qualification: 0,
          role: this.role,
        })
        .then((response) => {
          if (
            response.data.success == true ||
            response.data.success == "true"
          ) {
            this.signOk = true;
            setTimeout(() => {
              this.$router.push("/");
            }, 1500);
          }
        })
        .catch((e) => {
          if (e.request.status === 404) {
            this.alertMsg = "회원가입에 실패하였습니다.";
            this.alert = true;
          } else {
            this.visablelogin = false;
            this.submitted = false;
            this.newCustomer();
            this.$router.push(`/apierror/${e.request.status}/`);
          }
          console.log(e.request.status);
        });
    },
    idCheck() {
      http.get(`user/checkId/${this.id}`).then((res) => {
        if (res.data != null) {
          this.idFlag = true;
          this.id = "";
        } else {
          this.idOk = true;
          this.idCk = true;
        }
      });
    },
  },
};
</script>

<style>
</style>
