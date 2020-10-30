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
              <th class="text-center">현황</th>
              <th class="text-center">재상담현황</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in pagingList" :key="item.name">
              <td class="text-center">{{ setTime(item.date) }}</td>

              <td v-if="item.mentor == 1" class="text-center">-</td>
              <td v-else class="text-center">{{ findName(item.mentor) }}</td>
              
              <td v-if="item.recordDir != null" class="text-center">
                녹화 상담
              </td>
              <td v-else class="text-center">실시간 상담</td>

              <td v-if="item.recordDir != null && item.status == 'finish'">
                <v-btn @click="getAnswer(item)" small style="font-size: 0.9rem"
                  ><span style="vertical-align: middle; display: inline-flex"
                    ><v-icon small class="mr-1"
                      >mdi-message-text-outline</v-icon
                    >
                    답변</span
                  ></v-btn
                >
              </td>
              <td v-else-if="item.status == 'finish'" class="text-center">
                상담완료
              </td>
              <td v-else-if="item.status == 'progress'" class="text-center">
                진행중
              </td>
              <td v-if="item.status == 'reserveRequest'" class="text-center">
                예약중
              </td>
              <td v-else class="text-center">
                대기중
              </td>
              <td class="text-center">
                <v-btn
                  small
                  v-if="item.status == 'finish'"
                  :disabled="item.isreapply != 0"
                  @click="reapplyType(item)"
                  style="font-size: 0.9rem"
                  >재신청</v-btn
                >
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table> </v-card
    ><br />
    <v-pagination v-model="page" :length="pageLength" circle></v-pagination>
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
              <th class="text-center">예약취소</th>
              <th class="text-center">실시간 상담</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in myReservation" :key="item.name">
              <td class="text-center">{{ item.sdate }} {{ item.stime }}</td>
              <td class="text-center">{{ item.mentor }}</td>
              <td class="text-center">
                <v-btn @click="cancelD(item.num)">취소</v-btn>
              </td>
              <td>
                <v-btn v-if="item.sdate.slice(5,7) == todaytime.getMonth()+1 && item.sdate.slice(8,10) == todaytime.getDate() && item.stime.slice(0,2) == todaytime.getHours()"
                  small
                  color="orange lighten-4"
                  text-color="red"
                  @click="startCounseling(item)"
                  style="font-size:0.9rem;color:red"
                  >on-Air</v-btn
                >
                <v-btn v-else
                 disabled
                 style="font-size:0.9rem;color:black">기다리는 중</v-btn>
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card>

    <v-dialog v-model="answerDialog" persistent max-width="700">
      <v-card>
        <br />
        <v-card-subtitle>
          <span><h1>답변</h1></span>
        </v-card-subtitle>
        <v-card-text>
          <v-textarea
            rows="10"
            readonly
            auto-grow
            solo
            flat
            class="pa-3"
            :value="answer"
            style="white-space: pre-line"
          >
          </v-textarea>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="answerDialog = false">
            닫기
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="typeDialog" persistent max-width="500">
      <v-card
        ><br />
        <v-card-subtitle>
          <span><h1>상담 유형을 선택해주세요.</h1></span>
        </v-card-subtitle>
        <v-card-text>
          <div @click="reapply">실시간 상담</div>
          <div @click="reRecord">녹화 상담</div>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="typeDialog = false">
            취소
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog" persistent max-width="750">
      <v-card
        ><br />
        <v-card-subtitle>
          <span
            ><h1>
              {{ reMentor }} 상담사에게 실시간 상담을 재신청합니다.
            </h1></span
          >
        </v-card-subtitle>
        <v-card-text>
          원하시는 날짜와 시간을 선택해주세요.
          <span style="color: crimson"
            >재신청은 1회만 가능하므로 신중히 선택해주세요.</span
          >
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
    <v-dialog v-model="cancelDialog" persistent max-width="400">
      <v-card>
        <v-card-title style="font-size: 1.5rem">
          예약을 취소하시겠습니까?
        </v-card-title>
        <v-card-text></v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="cancelDialog = false">
            아니오
          </v-btn>
          <v-btn color="green darken-1" text @click="cancel"> 예 </v-btn>
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
      pagingList: [],
      pageLength: 0,
      userList: [],
      dialog: false,
      typeDialog: false,
      cancelDialog: false,
      reMentor: "",
      time: null,
      date: null,
      schedule: [],
      isReservation: false,
      myReservation: [],
      conRoomNum: null,
      page: 1,
      selitem: [],
      mentorNum: null,
      cancelNum: "",
      answerDialog: false,
      answer: "",
      todaytime: new Date()
    };
  },
  mounted() {
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/menteeMyList/${this.getUserNum}`).then((res) => {
      this.myList = res.data;
      this.pagingList = this.myList.slice(0, 9);
      if (this.myList.length % 10 == 0) {
        this.pageLength = this.myList.length / 10;
      } else {
        this.pageLength = parseInt(this.myList.length / 10) + 1;
      }
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
        date.slice(0, 4) +
        "-" +
        date.slice(5, 7) +
        "-" +
        date.slice(8, 10) +
        " " +
        date.slice(11, 13) +
        ":" +
        date.slice(14, 16);
      return time;
    },
    reapplyType(item) {
      this.mentorNum = item.mentor;
      this.selitem = item;
      this.typeDialog = true;
    },
    reapply() {
      this.typeDialog = false;
      this.conRoomNum = this.selitem.num;
      this.time = null;
      this.date = new Date().toISOString().substr(0, 10);
      this.dialog = true;
      this.reMentor = this.findName(this.selitem.mentor);
      var mentorId = this.findID(this.selitem.mentor);
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
            var today =
              date.getFullYear() +
              "-" +
              (date.getMonth() + 1) +
              "-" +
              date.getDate();
            if (today == this.date) {
              return val > hour;
            } else {
              return 1;
            }
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
              .post(`/schedule/reApply/${this.conRoomNum}`, {
                mentee: this.getUserID,
                scheNum: res.data,
              })
              .then((success) => {
                if (success.data == "success") {
                  http
                    .post(`/counseling/reserveRequest`, {
                      mentee: this.getUserNum,
                      mentor: this.mentorNum,
                      room: this.createRoomId(),
                      status: "reapply",
                      date: `${this.date}T${this.time}:00`
                    })
                  alert("재상담 신청 완료되었습니다.");
                  http
                    .get(`/counseling/menteeMyList/${this.getUserNum}`)
                    .then((res) => {
                      this.myList = res.data;
                      this.pagingList = this.myList.slice(0, 9);
                    });
                  http
                    .get(`/schedule/myReservation/${this.getUserID}`)
                    .then((data) => {
                      this.myReservation = data.data;
                    });
                  this.dialog = false;
                }
              });
          });
      } else {
        alert("날짜와 시간을 선택해주세요");
      }
    },
    cancel() {
      this.cancelDialog = false;
      http
        .delete(`/schedule/cancelReservation/${this.cancelNum}`)
        .then((res) => {
          if (res.data == "success") {
            alert("예약이 취소되었습니다.");
            http
              .get(`/schedule/myReservation/${this.getUserID}`)
              .then((data) => {
                this.myReservation = data.data;
              });
          }
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
    cancelD(num) {
      this.cancelNum = num;
      this.cancelDialog = true;
    },
    getAnswer(item) {
      console.log(item.answer);
      this.answer = item.answer
        .substring(1, item.answer.length - 1)
        .replaceAll("\r\n", "<br/>");
      this.answerDialog = true;
    },
    reRecord() {
      this.$router.push(`/recordConsult/${this.selitem.mentor}`);
    },
    startCounseling(){
      
    }
  },
  watch: {
    page(page) {
      var first = (page - 1) * 10;
      this.pagingList = this.myList.slice(first, first + 9);
    },
  },
};
</script>

<style>
</style>