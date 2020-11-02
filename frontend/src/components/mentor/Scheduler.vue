<template>
  <div style="position: relative" :style="rootCssVar">
    <div class="vws-rule-custom" style="user-select: none">
      <div class="vws-rule-row">
        <div class="vws-table-rule">
          <div class="vws-table-rule-heading">
            <div class="text-center week-rule"></div>
            <div
              class="text-center"
              v-for="(day, daynum) in dayTable"
              :key="daynum"
            >
              {{ day }}
            </div>
          </div>
          <div class="vws-table-rule-body" id="schelude" ref="draggableArea">
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
                {{ t }}
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
                <span>{{ day }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <v-btn v-if="isModify" @click="saveTime">완료</v-btn>
    <v-btn v-if="!isModify" @click="modifyTime">일정수정</v-btn>
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
    };
  },
  created() {
    this.uid = this.getUserID;
    http
      .get(`/schedule/checkUpdate/${this.getUserID}`)
      .then((res) => {
        console.log(res.data);
        if (res.data == 1) {
          this.monDialog = false;
        }else if(res.data==0){
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
      alert("일정이 수정되었습니다.");
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
            alert("이미 예약된 시간입니다.");
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
    ...mapGetters(["getUserID"]),
    ...mapState({
      userID: (state) => `${state.user.getUserID}`,
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