<template>
  <div>
    <v-card-title class="text-center justify-center py-0">
      <!-- <h1 class="font-weight-bold display-3 basil--text">{{ userRole }}</h1> -->
      <p
        style="
          font-size: 2rem;
          font-weight: 700;
          word-spacing: 4px;
          letter-spacing: 4px;
        "
        class="mb-2"
      >
        회원가입
      </p>
    </v-card-title>
    <div class="col-md-12 py-0">
      <v-container id="inspire" class="py-0">
        <v-col>
          <v-form ref="form" v-model="valid">
            <v-row justify="center">
              <v-switch
                v-model="type"
                inset
                hide-details
                :label="'저는 상담사입니다.'"
              ></v-switch>
            </v-row>
            <v-row justify="center">
              <v-col
                class="d-flex col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12"
              >
                <v-icon large color="black" class="mr-2"
                  >mdi-account-outline</v-icon
                >
                <v-text-field
                  v-model="id"
                  hide-details
                  outlined
                  color="#a2c9d0"
                  placeholder="아이디를 입력해주세요."
                  required
                ></v-text-field>
                <v-icon
                  v-if="!idchkFlag"
                  class="ml-3"
                  color="red"
                  large
                  icon
                  @click="idCheck()"
                  >mdi-check-outline</v-icon
                >
                <v-icon
                  v-if="idchkFlag"
                  class="ml-3"
                  color="blue"
                  icon
                  large
                  @click="idCheck()"
                  >mdi-check-bold</v-icon
                >
              </v-col>
            </v-row>

            <v-row v-if="type" justify="center">
              <v-col
                class="d-flex col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12"
              >
                <v-icon large color="black" class="mr-2"
                  >mdi-account-circle-outline</v-icon
                >
                <v-text-field
                  v-model="name"
                  hide-details
                  placeholder="이름을 입력해주세요."
                  color="#a2c9d0"
                  required
                  outlined
                ></v-text-field>
              </v-col>
            </v-row>

            <v-row justify="center">
              <v-col
                class="d-flex col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12"
              >
                <v-icon large color="black" class="mr-2"
                  >mdi-lock-outline</v-icon
                >
                <v-text-field
                  v-model="password1"
                  type="password"
                  hide-details
                  placeholder="비밀번호(영문자, 숫자 포함 8자 이상)를 입력해주세요."
                  required
                  color="#a2c9d0"
                  outlined
                ></v-text-field>
              </v-col>
            </v-row>

            <v-row justify="center">
              <v-col
                class="d-flex col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12"
              >
                <v-icon large color="black" class="mr-2">mdi-lock</v-icon>
                <v-text-field
                  v-model="password2"
                  type="password"
                  hide-details
                  placeholder="비밀번호를 다시 입력해주세요."
                  required
                  color="#a2c9d0"
                  outlined
                ></v-text-field>
              </v-col>
            </v-row>

            <v-row v-if="!type" justify="center">
              <v-col
                class="d-flex col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12"
              >
                <v-icon large color="black" class="mr-2"
                  >mdi-shield-account-outline</v-icon
                >
                <v-text-field
                  v-model="name"
                  hide-details
                  placeholder="닉네임을 입력해주세요."
                  color="#a2c9d0"
                  required
                  outlined
                ></v-text-field>
              </v-col>
            </v-row>

            <v-row v-if="type" justify="center">
              <v-col
                class="d-flex col-xl-10 col-lg-10 col-md-10 col-sm-10 col-12"
              >
                <v-icon large color="black" class="mr-2"
                  >mdi-badge-account-horizontal-outline</v-icon
                >
                <v-file-input
                  accept="image/png, image/jpeg, image/bmp"
                  prepend-icon
                  small-chips
                  clearable
                  outlined
                  hide-details
                  color="#a2c9d0"
                  @change="uploadFile()"
                  label="자격증 사진을 등록해주세요."
                  v-model="files"
                ></v-file-input>
              </v-col>
            </v-row>

            <v-row>
              <v-col cols="10" style="margin: 0 auto">
                <v-btn
                  class="main-btn"
                  width="100%"
                  style="height: 3rem"
                  @click="validate"
                  >등록</v-btn
                >
              </v-col>
            </v-row>
          </v-form>
        </v-col>
        <!-- alert -->
        <v-snackbar
          v-model="idFlag"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">존재하는 아이디입니다.</span>
        </v-snackbar>
        <v-snackbar
          v-model="idOk"
          top
          flat
          color="success"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">사용 가능한 아이디입니다.</span>
        </v-snackbar>
        <v-snackbar
          v-model="noidOk"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">아이디를 체크 해주세요.</span>
        </v-snackbar>
        <v-snackbar
          v-model="pwdNo"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">비밀번호가 다릅니다.</span>
        </v-snackbar>
        <v-snackbar
          v-model="signOk"
          top
          flat
          color="success"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">가입이 완료되었습니다.</span>
        </v-snackbar>
        <v-snackbar
          v-model="pwdLength"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="1000"
        >
          <span class="snackText">
            비밀번호는 숫자와 영문자 조합으로 8~20자리를 사용해야 합니다.
          </span>
        </v-snackbar>
        <v-snackbar
          v-model="pwdEngNum"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText"
            >비밀번호는 숫자와 영문자를 혼용하여야 합니다.</span
          >
        </v-snackbar>
        <v-snackbar
          v-model="noid"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">아이디를 입력해주세요.</span>
        </v-snackbar>
        <v-snackbar
          v-model="imgOk"
          top
          flat
          color="success"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">인증 완료 했습니다.</span>
        </v-snackbar>
        <v-snackbar
          v-model="noimgOk"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">인증 실패 했습니다. 다른 사진을 등록해주세요.</span>
        </v-snackbar>
        <v-snackbar
          v-model="noimgChk"
          top
          flat
          color="error"
          rounded="pill"
          :timeout="2000"
        >
          <span class="snackText">자격증 확인이 필요합니다.</span>
        </v-snackbar>

      </v-container>
    </div>
    <v-dialog v-model="img_dialog" max-width="600" min-height="600">
      <div class="px-5 pt-5 reser-back">
        <h2>자격증 확인 중 입니다. <br> 조금만 기다려 주세요.</h2><br>
        <v-progress-circular 
          :size="50"
          :width="7"
          color="#49358b"
          indeterminate
          class="mb-5"
        >
        </v-progress-circular>
        <br>
        
      </div>
    </v-dialog>
  </div>
</template>

<script>
import http3 from "@/util/http-common3.js";
import http2 from "@/util/http-common2.js";
import http from "@/util/http-common.js";

export default {
  name: "SignUpModal",
  props: {
    sign_dialog: { type: Boolean },
  },
  data() {
    return {
      type: false,
      role: "mentee",
      tab: null,
      userRole: this.role,
      id: "",
      name: "",
      password1: "",
      password2: "",
      valid: false,
      // nameRules: [(v) => !!v || '이름을 입력해주세요', (v) => (v && v.length <= 15) || '이름은 최대 15글자까지만 사용할 수 있습니다'],
      // nickNameRules: [(v) => !!v || '별명을 입력해주세요', (v) => (v && v.length <= 15) || '별명은 최대 15글자까지만 사용할 수 있습니다'],
      // idRules: [(v) => !!v || '아이디를 입력해주세요', (v) => (v && v.length <= 15) || '아이디는 최대 15글자까지만 사용할 수 있습니다'],
      // passwordRules: [
      //   (v) => !!v || '비밀번호를 입력해주세요',
      //   (v) => (v && v.length <= 20) || '비밀번호는 최대 20글자까지만 사용할 수 있습니다',
      // ],
      idFlag: false,
      idOk: false,
      noidOk: false,
      pwdNo: false,
      idCk: false,
      signOk: false,
      pwdLength: false,
      pwdEngNum: false,
      noid: false,
      idchkFlag: false,
      files: null,
      img_dialog: false,
      imgCheck:false,
      imgOk:false,
      noimgOk:false,
      noimgChk:false,
    };
  },
  mounted() {
    if (this.role == `mentee`) {
      this.userRole = `청소년`;
    } else {
      this.userRole = `상담사`;
    }
  },
  watch: {
    type(val) {
      if (val) {
        this.role = `mentor`;
      } else {
        this.role = `mentee`;
      }
    },
    role(val) {
      if (val == `mentee`) {
        this.userRole = `청소년`;
      } else {
        this.userRole = `상담사`;
      }
    },
    sign_dialog(v) {
      if (!v) {
        this.tab = null;
        this.id = "";
        this.userRole = this.role;
        this.name = "";
        this.password1 = "";
        this.password2 = "";
        this.valid = false;
        this.idFlag = false;
        this.idOk = false;
        this.noidOk = false;
        this.pwdNo = false;
        this.idCk = false;
        this.signOk = false;
        this.pwdLength = false;
        this.pwdEngNum = false;
        this.noid = false;
        this.idchkFlag = false;
        this.imgCheck = false;
        this.noimgChk = false;
        this.imgOk=false;
      }
    },
    files(val){
      if(val != null){
        this.img_dialog=true;
      }
    },

  },
  methods: {
    uploadFile() {
      if (this.files) {
        let formData = new FormData();
        formData.append("file", this.files);
        http3
          .post(`/cert/imgCheck`, formData)
          .then((res) => {
            console.log(res);
            if (res.data == "眞(정상적으로 발급된 자격증입니다.)") {
              this.imgCheck = true;
              this.imgOk=true;
            } else {
              this.noimgOk=true;
            }
              this.img_dialog=false;
          })
          .catch((e) => console.log(e));
      }
    },
    validate() {
      this.$refs.form.validate();

      if (this.type) {
        if (!this.imgCheck) {
          this.noimgChk = true;
          return;
        }
      }

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
      if(this.name == ""){
        this.name = "청소년";
      }
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
              this.$router.go();
            }, 500);
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
      if (this.id == "") {
        this.noid = true;
      } else {
        http.get(`user/checkId/${this.id}`).then((res) => {
          if (res.data != null) {
            this.idFlag = true;
            this.id = "";
          } else {
            this.idOk = true;
            this.idCk = true;
            this.idchkFlag = true;
          }
        });
      }
    },
  },
};
</script>

<style>
.snackText {
  margin-bottom: 0;
  font-weight: bold;
  font-size: 1rem;
  word-spacing: 2px;
  letter-spacing: 2px;
  display: inline-block;
  width: 100%;
  text-align: center;
}
</style>
