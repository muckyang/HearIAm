<template>
  <div style="position: relative" :style="rootCssVar">
    <div align="center">
      <div style="width: 750px">
        <div align="right" class="mb-2">
        <v-btn v-if="isModify" @click="saveTime" class="main-btn">완료</v-btn>
        <v-btn v-if="!isModify" @click="modifyTime"  style="font-size:1rem;" class="main-btn"
          >일정 수정하기</v-btn
        >
      </div>
        <div class="vws-rule-custom mb-5" style="user-select: none">
          <div class="vws-rule-row">
            <div class="vws-table-rule">
              <div class="vws-table-rule-heading">
                <div class="text-center week-rule"></div>
                <div
                  class="text-center"
                  v-for="(day, daynum) in dayTable"
                  :key="daynum"
                >
                  <span style="font-size: 1rem">
                    {{ day }}
                  </span>
                </div>
              </div>
              <div
                class="vws-table-rule-body"
                id="schelude"
                ref="draggableArea"
              >
                <div
                  ref="ruleTime"
                  class="vws-rule-time"
                  v-for="(t, idx) in timeArray"
                  :key="idx"
                >
                  <div
                    ref="ruleTimeTime"
                    class="vws-time-list vws-rule-time-time vws-time-rule"
                    :data-val="t"
                  >
                    <span style="font-size: 0.8rem">
                      {{ t }}
                    </span>
                  </div>
                  <div
                    class="day-div"
                    v-for="(day, daynum) in dayTable"
                    :key="daynum"
                    ref="ruleTimeItem"
                    :class="{
                      'vws-time-list vws-rule-time-item': true,
                      active:
                        timetable[daynum].find((el) => el == idx) != undefined
                          ? true
                          : false,
                    }"
                    @click="toggleDay(daynum, idx)"
                  >
                    <span style="font-size: 0.8rem; color:white;">{{ day }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div align="center" style="font-size: 0.9rem">
        * 이번 주 상담 가능한 시간을 관리해보세요. 이미 예약이 잡힌 시간은 수정할 수 없습니다.
    </div>
      </div>
    </div>
    <v-dialog v-model="monDialog" persistent max-width="400">
      <v-card>
        <v-card-title style="font-size: 1.5rem">
          일정을 변경하시겠습니까?
        </v-card-title>
        <v-card-text class="mt-3" style="text-align: left"
          >새로운 주가 시작되었습니다. <br />이번 주 일정을 새로 설정하거나,
          <br />지난주와 동일하게 유지할 수 있습니다.</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="reset"> 초기화 </v-btn>
          <v-btn color="green darken-1" text @click="maintain"> 유지 </v-btn>
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
    value: {
      type: Object,
    },
    steps: {
      type: Number,
      default: 60,
    },
    bg: {
      type: String,
      default: "#223642",
    },
    bgHover: {
      type: String,
      default: "#84dafc7a",
    },
    bgActive: {
      type: String,
      default: "#84c9fc",
    },
    textColor: {
      type: String,
      default: "#000",
    },
    // dayTable:{
    //   type: Array,
    //   default: ['So','Mo','Tu','We','Th','Fr','Sa'],
    //   validator: val => val.length == 7
    // },
    strWeek: {
      type: String,
      default: "Week",
    },
    strTime: {
      type: String,
      default: "Time",
    },
    strDay: {
      type: String,
      default: "Day",
    },
    disableDaySelect: {
      type: Boolean,
      default: false,
    },
    disableWeekSelect: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      uid: "",
      timeArray: [],
      timetable: {
        0: [],
        1: [],
        2: [],
        3: [],
        4: [],
        5: [],
        6: [],
      },
      dayTable: ["일", "월", "화", "수", "목", "금", "토"],
      isModify: false,
      monDialog: false,
      errorSnack: false,
      successSnack: false,
      altMsg: "",
    };
  },
  created() {
    this.uid = this.getUserID;
    http
      .get(`/schedule/checkUpdate/${this.getUserID}`)
      .then((res) => {
        // console.log(res.data);
        if (res.data == 1) {
          this.monDialog = false;
        } else if (res.data == 0) {
          this.monDialog = true;
        }
      })
      .catch((err) => {
        console.log(err);
      });
    this.getTime();
  },
  methods: {
    getTime() {
      http
        .get(`/schedule/getTime/${this.getUserID}`)
        .then((res) => {
          this.timetable = res.data;
        })
        .catch((e) => {
          console.log(e);
        });
      this.isModify = false;
    },
    saveTime() {
      this.successSnack = true;
      this.altMsg = "일정이 수정되었습니다.";
      this.isModify = false;
      var daydiv_arr = document.getElementsByClassName("day-div");
      for (var i = 0; i < daydiv_arr.length; i++) {
        document.getElementsByClassName("day-div")[i].style.pointerEvents =
          "none";
      }
    },
    modifyTime() {
      this.isModify = true;
      var daydiv_arr = document.getElementsByClassName("day-div");
      for (var i = 0; i < daydiv_arr.length; i++) {
        document.getElementsByClassName("day-div")[i].style.pointerEvents =
          "auto";
      }
    },
    toggleDay(day, time) {
      http
        .get(`/schedule/checkTime/${this.getUserID}/${day}/${time}`)
        .then((res) => {
          if (res.data == 0) {
            this.errorSnack = true;
            this.altMsg = "이미 예약된 시간입니다.";
            return;
          } else {
            let indexDay = this.timetable[day].findIndex((el) => el == time);
            if (indexDay != -1) {
              this.timetable[day].splice(indexDay, 1);
            } else {
              this.timetable[day].push(time);
            }
            this.$emit("input", this.timetable);
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    setupCustom() {
      const vm = this;
      var times = []; // time array
      var tt = 0; // start time
      //var ap = ['AM', 'PM']; // AM-PM
      for (var i = 0; tt < 24 * 60; i++) {
        var hh = Math.floor(tt / 60); // getting hours of day in 0-24 format
        var mm = tt % 60; // getting minutes of the hour in 0-55 format
        if (hh >= 9) {
          times[i - 9] = ("0" + hh).slice(-2) + ":" + ("0" + mm).slice(-2); // pushing data in array in [00:00 - 12:00 AM/PM format]
        }
        tt = tt + this.steps;
      }
      vm.timeArray = times;
    },
    reset() {
      http
        .delete(`/schedule/reset/${this.getUserID}`)
        .then((res) => {
          this.timetable = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      this.monDialog = false;
    },
    maintain() {
      http
        .put(`/schedule/maintain/${this.getUserID}`)
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
      this.monDialog = false;
    },
  },
  mounted() {
    this.setupCustom();
    if (this.value) {
      this.timetable = this.value;
    }
    window.addEventListener("mouseup", this.stopDrag);
  },
  watch: {
    value(val) {
      if (val) {
        this.timetable = val;
      }
    },
  },
  computed: {
    rootCssVar() {
      return {
        "--vws-bg": this.bg,
        "--vws-bgActive": this.bgActive,
        "--vws-bgHover": this.bgHover,
        "--vws-text": this.textColor,
      };
    },
    ...mapGetters(["getUserID", "getUserName"]),
    ...mapState({
      userID: (state) => `${state.user.getUserID}`,
      userName: (state) => `${state.user.getUserName}`,
    }),
  },
};
</script>

<style scoped>
@import url("../../assets/schedule_style.css");
.day-div {
  pointer-events: none;
}
</style>