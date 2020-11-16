<template>
  <div>
    <v-sheet class="container">
      <!-- <v-btn class="main-btn mr-2" @click="closeDialog">취소</v-btn> -->
      
      <v-row class="d-flex justify-content-center" justify="center">
        <v-col cols="12" sm="10" md="10" class="reserve-data py-1 px-0">
          <v-dialog
            ref="dialogDate"
            v-model="dateModal"
            :return-value.sync="date"
            persistent
            width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <div class="d-flex">
                <v-icon class="mr-3" large color="black">mdi-calendar</v-icon>
                <v-text-field
                  class="pt-7"
                  v-model="date"
                  label="날짜를 선택해주세요."
                  readonly
                  solo
                  v-bind="attrs"
                  v-on="on"
                  hint="일주일 단위로 예약이 가능합니다."
                  persistent-hint
                ></v-text-field>
              </div>
            </template>
            <v-date-picker
              v-model="date"
              scrollable
              :min="nowDate"
              :max="getEndDate"
            >
              <v-spacer></v-spacer>
              <v-btn text color="#262272" @click="dateModal = false">
                닫기
              </v-btn>
              <v-btn text color="#262272" @click="setDate"> 선택 </v-btn>
            </v-date-picker>
          </v-dialog>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" sm="10" md="10" class="reserve-data py-1 px-0">
          <div class="d-flex">
            <v-icon class="mr-3" large color="black"
              >mdi-clock-time-four-outline</v-icon
            >
            <v-select
              class="mt-7"
              v-model="time"
              :items="timeItems"
              :label="selLabel"
              hint="예약 가능한 시간만 보여집니다."
              solo
              persistent-hint
            ></v-select>
          </div>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" sm="10" md="10" class="reserve-data py-1 px-0">
          <div class="d-flex">
            <v-icon class="mr-3" large color="black"
              >mdi-help-circle-outline</v-icon
            >
            <v-select
              class="mt-7"
              v-model="concern"
              :items="items"
              label="고민이 무엇인가요?"
              solo
              hint="대략적인 고민을 입력해주세요."
              persistent-hint
            ></v-select>
          </div>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" sm="10" md="10" class="reserve-data py-1 px-0">
          <div class="d-flex">
            <v-icon class="mr-3" large color="white"
              >mdi-help-circle-outline</v-icon
            >
            <v-text-field
              v-if="isSelf"
              v-model="concern2"
              hint="자유롭게 고민을 입력해주세요."
              persistent-hint
            ></v-text-field>
          </div>
        </v-col>
      </v-row>
      <!-- <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <div class="d-flex">
              <v-icon class="mr-3" large color="black">mdi-account-check-outline</v-icon>
              <v-text-field class="mt-7" v-model="consultant" label="원하는 상담사가 있나요?" solo></v-text-field>
            </div>
          </v-col>
        </v-row> -->
      <v-row class="mt-4" justify="center">
        <!-- <v-col class="pt-5 px-0 d-flex" align="center" justify="center"> -->
        
        <v-btn class="main-btn" large @click="reserveD" style="border-radius:20px;">예약하기</v-btn>
        <!-- </v-col> -->
      </v-row>
    </v-sheet>
    <v-dialog v-model="reserDialog" persistent max-width="400">
      <v-card>
        <v-card-title style="font-size: 1.5rem">
          아래 정보로 예약하시겠습니까?
        </v-card-title>
        <v-card-text class="pt-3" style="text-align: left"
          >날짜 : {{ this.date }} <br />시간 : {{ this.time }}</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="reserDialog = false">
            아니오
          </v-btn>
          <v-btn color="green darken-1" text @click="reserve"> 예 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

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

    <v-snackbar
      v-model="successSnack"
      top
      flat
      color="success"
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
import http from "@/util/http-common.js";
import { mapGetters, mapState } from "vuex";
export default {
  props: {
    reser_dialog: { type: Boolean },
  },
  data() {
    return {
      date: new Date().toISOString().substr(0, 10),
      dateModal: false,
      timeModal: false,
      time: "",
      concern: "",
      // consultant: '',
      items: [
        "시험 성적 때문에 고민이에요.",
        "진로에 대한 고민이 있어요.",
        "괴롭힘을 당하고 있어요.",
        "친구문제로 상담을 받고 싶어요.",
        "직접 입력",
      ],
      timeItems: [],
      nowDate: new Date().toISOString().substr(0, 10),
      nDate: new Date(),
      isSelf: false,
      mentorNum: null,
      concern2: "",
      reserDialog: false,
      errorSnack: false,
      successSnack: false,
      altMsg: "",
      selLabel: "시간을 선택해주세요.",
    };
  },
  computed: {
    getEndDate() {
      var endDate = new Date();
      endDate.setDate(this.nDate.getDate() + 7);
      return endDate.toISOString().slice(0, 10);
    },
    ...mapGetters(["getUserID", "getUserNum"]),
    ...mapState({
      userID: (state) => `${state.user.getUserID}`,
    }),
  },
  watch: {
    concern(v) {
      if (v == "직접 입력") {
        this.isSelf = true;
      } else {
        this.isSelf = false;
      }
    },
    reser_dialog(v) {
      if (!v) {
        this.date = new Date().toISOString().substr(0, 10);
        this.time = "";
        this.timeItems = [];
        http
      .get(`/schedule/getTimeByDate/${this.nowDate}`)
      .then((res) => {
        if (res.data.length == 0) {
          this.timeItems.push("예약 가능한 시간이 없습니다.");
          this.selLabel = "예약 가능한 시간이 없습니다.";
        } else {
          this.timeItems = res.data;
          this.selLabel = "시간을 선택해주세요.";
        }
      })
      .catch((e) => {
        console.log(e);
      });
        this.concern = "";
      }
    },
  },
  created() {
    http
      .get(`/schedule/getTimeByDate/${this.nowDate}`)
      .then((res) => {
        if (res.data.length == 0) {
          this.timeItems.push("예약 가능한 시간이 없습니다.");
          this.selLabel = "예약 가능한 시간이 없습니다.";
        } else {
          this.timeItems = res.data;
          this.selLabel = "시간을 선택해주세요.";
        }
      })
      .catch((e) => {
        console.log(e);
      });
  },
  methods: {
    setDate() {
      this.$refs.dialogDate.save(this.date);
      this.time = "";
      this.timeItems = [];
      http
        .get(`/schedule/getTimeByDate/${this.date}`)
        .then((res) => {
          if (res.data.length == 0) {
            this.timeItems.push("예약 가능한 시간이 없습니다.");
          } else {
            // this.timeItems = res.data;
            for (const stime of res.data) {
              if (this.date.split("-")[2] == new Date().getDate().toString()) {
                if (stime.substr(0, 2) > new Date().getHours()) {
                  this.timeItems.push(stime);
                }
              } else {
                this.timeItems.push(stime);
              }
            }
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    reserve() {
      http
        .post(
          `/schedule/reservation/${this.getUserID}/${this.date}/${this.time}/${this.concern}`
        )
        .then((res) => {
          http.get(`/user/users/${res.data.mentor}`).then((res1) => {
            this.mentorNum = res1.data.num;
            console.log(res1.data);
            console.log(res.data);
            console.log(this.getUserNum);
            http.post(`/counseling/reserveRequest`, {
              mentee: this.getUserNum,
              mentor: this.mentorNum,
              room: this.createRoomId(),
              status: "reserve",
              date: `${res.data.sdate}T${res.data.stime}:00`,
            });
          });
          this.successSnack = true;
          this.altMsg = "예약이 완료되었습니다.";
          setTimeout(() => {
            this.reserDialog = false;
            this.$emit("reserve", false);
          },2000);
        })
        .catch((e) => {
          console.log(e);
        });
    },
    createRoomId(length = 20) {
      let text = "";
      const possible =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      Array.from(Array(length)).forEach(() => {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
      });
      return text;
    },
    reserveD() {
      if (this.concern == "직접 입력") {
        this.concern = this.concern2;
      }
      if (this.date == "" || this.time == "" || this.concern == "") {
        this.errorSnack = true;
        this.altMsg = "예약 정보를 모두 입력해주세요.";
        return;
      } else if (this.time == "예약 가능한 시간이 없습니다.") {
        this.errorSnack = true;
        this.altMsg = "예약 시간을 다시 확인해주세요.";
        return;
      } else {
        this.reserDialog = true;
      }
    },
    closeDialog() {
      this.$emit("reserve", false);
    },
  },
};
</script>

<style scoped>
.reserve-body {
  height: 100%;
  padding: 2%;
}
.sheet-body {
  /* background-image: linear-gradient(to right, #93dfff, white, #f5a2bb); */
  padding: 50px;
  height: 100%;
  text-align: center;
}
.v-picker__title {
  height: 100px;
}
</style>
