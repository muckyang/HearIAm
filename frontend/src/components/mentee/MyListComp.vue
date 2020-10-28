<template>
  <div>
    <h1>나의 상담 내역</h1>
    <br />
    <v-card>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-center">상담 날짜</th>
              <th class="text-center">상담사</th>
              <th class="text-center">종류</th>
              <th class="text-center">상태</th>
              <th class="text-center">재상담현황</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in myList" :key="item.name">
              <td class="text-center">{{ setTime(item.date) }}</td>
              
              <td v-if="item.mentor == 1 " class="text-center"> - </td>

              <td v-else class="text-center">{{ findName(item.mentor) }}</td>
              <td v-if="item.recordDir != null " class="text-center">
                녹화 상담
              </td>
              <td v-else class="text-center">실시간 상담</td>
                <td v-if ="item.status == 'finish' " class="text-center" >상담완료</td>
                <td v-if ="item.status == 'waiting' " class="text-center" >대기중</td>
                <td v-if ="item.status == 'progress' " class="text-center" >진행중</td>
               <td class="text-center">
                <v-btn v-if ="item.status == 'finish' " @click="reapply(item)">재신청</v-btn>
             
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card>
    <br />
    <h1>나의 예약 내역</h1>
    <br />
    <v-card>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-center">예약 날짜</th>
              <th class="text-center">상담사</th>
              <th class="text-center">취소</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in myReservation" :key="item.name">
              <td class="text-center">{{ item.sdate }} {{ item.stime }}</td>
              <td class="text-center">{{ item.mentor }}</td>
              <td class="text-center">
                <v-btn @click="cancel(item.num)">취소</v-btn>
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card>
    <v-dialog v-model="dialog" persistent max-width="750">
      <v-card
        ><br />
        <v-card-subtitle>
          <span
            ><h1>{{ reMentor }} 상담사에게 재상담을 원하십니까?</h1></span
          >
        </v-card-subtitle>
        <v-card-text>
          원하시는 날짜와 시간을 선택해주세요.
          <br />
          (상담 가능한 날짜와 시간만 표시됩니다.)
        </v-card-text>
        <v-container>
          <v-row>
            <v-col>
              <v-date-picker
                v-model="date"
                :allowed-dates="allowedDates"
                color="#93dfff"
              ></v-date-picker>
            </v-col>
            <v-col>
              <v-time-picker
                v-model="time"
                ampm-in-title
                format="24hr"
                color="#f5a2bb"
                :allowed-hours="allowedHours"
                :allowed-minutes="allowedMinutes"
              ></v-time-picker>
            </v-col>
          </v-row>
        </v-container>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">
            취소
          </v-btn>
          <v-btn color="blue darken-1" text @click="reqReapply()"> 신청 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapGetters } from "vuex";

export default {
  name: "MyListComp",
  data() {
    return {
      myList: [],
      userList: [],
      dialog: false,
      reMentor: "",
      time: null,
      date: null,
      schedule: [],
      isReservation: false,
      myReservation: [],
    };
  },
  mounted() {
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/menteeMyList/${this.getUserNum}`).then((res) => {
      this.myList = res.data;
    });
    http.get(`/schedule/isReservation/${this.getUserID}`).then((res) => {
      this.isReservation = res.data;
    });
    http.get(`/schedule/myReservation/${this.getUserID}`).then((res) => {
      this.myReservation = res.data;
    });
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
  methods: {
    findName(userNum) {
      for (let index = 0; index < this.userList.length; index++) {
        if (this.userList[index].num == userNum) {
          return this.userList[index].name;
        }
      }
    },
    findID(userNum) {
      for (let index = 0; index < this.userList.length; index++) {
        if (this.userList[index].num == userNum) {
          return this.userList[index].id;
        }
      }
    },
    setTime(date) {
      let time =
        Number(date.slice(0, 4)) +
        "-" +
        Number(date.slice(5, 7)) +
        "-" +
        Number(date.slice(8, 10)) +
        " " +
        date.slice(11, 13) +
        ":" +
        Number(date.slice(14, 16));
      return time;
    },
    reapply(item) {
      // if(this.isReservation == true){
      //   alert("예약 내역이 존재합니다.")
      //   return;
      // }
      this.time = null;
      this.date = new Date().toISOString().substr(0, 10);
      this.dialog = true;
      this.reMentor = this.findName(item.mentor);
      var mentorId = this.findID(item.mentor);
      http.get(`/schedule/allowSchedule/${mentorId}`).then((res) => {
        this.schedule = res.data;
      });
    },
    allowedDates(val) {
      var date = new Date();
      date.setDate(date.getDate() - 1);

      for (const schedule of this.schedule) {
        if (val == schedule.sdate) {
          return new Date(val) >= date;
        }
      }
      return 0;
    },
    allowedHours(val) {
      var date = new Date();
      var hour = date.getHours();
      for (const schedule of this.schedule) {
        if (this.date == schedule.sdate) {
          if (val == schedule.stime.substr(0, 2)) {
            return val > hour;
          }
        }
      }
      return 0;
    },
    allowedMinutes(val) {
      return val == 0;
    },
    reqReapply() {
      if (this.date != null && this.time != null) {
        http
          .get(`/schedule/findScheduleNum/${this.date}/${this.time}`)
          .then((res) => {
            http
              .post(`/schedule/reApply`, {
                mentee: this.getUserID,
                scheNum: res.data,
              })
              .then((success) => {
                if (success.data == "success") {
                  alert("재상담 신청 완료되었습니다.");
                  this.dialog = false;
                }
              });
          });
      } else {
        alert("날짜와 시간을 선택해주세요");
      }
    },
    cancel(num) {
      http.delete(`/schedule/cancelReservation/${num}`).then((res) => {
        if (res.data == "success") {
          alert("에약이 취소되었습니다.");
          http.get(`/schedule/myReservation/${this.getUserID}`).then((data) => {
            this.myReservation = data.data;
          });
        }
      });
    },
  },
};
</script>

<style>
</style>