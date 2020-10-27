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
    <v-btn @click="saveTime">등록</v-btn>
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
      dayTable: ["월", "화", "수", "목", "금", "토", "일"],
    };
  },
  created(){
      this.uid = this.getUserID;
  },
  methods: {
    saveTime() {
      http
        .post(`/schedule/saveTime/${this.timetable}/${this.getUserID}`)
        .then(() => {})
        .catch((e) => {
          console.log(e);
        });
    },
    toggleDay(day, time) {
      let indexDay = this.timetable[day].findIndex((el) => el == time);
      if (indexDay != -1) {
        this.timetable[day].splice(indexDay, 1);
      } else {
        this.timetable[day].push(time);
      }
      this.$emit("input", this.timetable);
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
          times[i - 9] = ("0" + hh).slice(-2) + "." + ("0" + mm).slice(-2); // pushing data in array in [00:00 - 12:00 AM/PM format]
        }
        tt = tt + this.steps;
      }
      vm.timeArray = times;
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
</style>