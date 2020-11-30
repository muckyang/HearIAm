<template>
  <div>
    <v-tabs v-model="tab" background-color="" color="black" grow>
      <v-tab style="font-size: 1.3rem"><v-icon class="mr-1" style="font-size: 1.5rem"
            >mdi-book-open-outline</v-icon
          >상담 내역</v-tab>
      <v-tab style="font-size: 1.3rem"><v-icon class="mr-1" style="font-size: 1.5rem"
            >mdi-clock-time-four-outline</v-icon
          >예약 내역</v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <!-- 상담 내역 -->
      <v-tab-item>

        <v-col v-if="cpagingList == 0"
          ><div style="margin-top:130px;font-size:1.5rem">
            <img src="@/assets/noResult3.png" style="height:200px; width:200px;">
            <br>
            상담 내역이 없습니다.</div></v-col
        >
        <v-simple-table v-if="cpagingList != 0" class="py-5">
          <template v-slot:default>
            <thead>
              <tr>
                <th class="text-center" style="width: 20%">상담 일자</th>
                <th class="text-center" style="width: 20%">상담사</th>
                <th class="text-center" style="width: 20%">상담 종류</th>
                <th class="text-center" style="width: 20%">현황</th>
                <th class="text-center" style="width: 20%">재상담 신청</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in cpagingList" :key="item.name">
                <td class="text-center">{{ setTime(item.date) }}</td>

                <td v-if="item.mentor == 1" class="text-center">-</td>
                <td v-else class="text-center">{{ findName(item.mentor) }}</td>

                <td v-if="item.recordDir != null" class="text-center">
                  녹화 상담
                </td>
                <td v-else class="text-center">실시간 상담</td>

                <td
                  v-if="item.recordDir != null && item.answer != null"
                  align="center"
                >
                  <v-btn
                    text
                    @click="getAnswer(item)"
                    small
                    style="font-size: 0.9rem"
                    ><span
                      style="
                        vertical-align: middle;
                        display: inline-flex;
                        color: crimson;
                      "
                      ><v-icon small class="mr-1"
                        >mdi-message-text-outline</v-icon
                      >
                      답변보기</span
                    ></v-btn
                  >
                </td>
                <td v-else-if="item.recordDir == null" class="text-center">
                  상담 완료
                </td>
                <td v-else class="text-center">대기중</td>

                <td class="text-center">
                  <v-btn
                    small
                    v-if="item.status == 'finish' && item.isreapply == 0"
                    :disabled="item.isreapply != 0"
                    @click="reapplyType(item)"
                    style="font-size: 0.9rem; color: white"
                    color="#262272"
                    >신청</v-btn
                  >
                </td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
        <v-pagination
          v-model="cpage"
          v-if="cpagingList != 0"
          :length="cpageLength"
          circle
          class="pb-3"
          color="#262272"
          :total-visible="9"
        ></v-pagination>
      </v-tab-item>

      <!-- 예약 내역 -->
      <v-tab-item>
        <v-sheet class="mx-auto pa-5" max-width="100%">
          <v-row>
            <v-col v-if="rpagingList.length == 0"
              ><div style="margin-top:130px;font-size:1.5rem">
            
            <img src="@/assets/noResult3.png" style="height:200px; width:200px;">
            <br>
            예약 내역이 없습니다.</div></v-col
            >
            <v-col
              v-for="(item, index) in rpagingList"
              :key="index"
              cols="12"
              sm="6"
              md="3"
            >
              <v-card class="mx-auto pa-3" height="195">
                <div align="left" class="d-flex">
                  <v-col class="pb-0">
                    <v-chip small color="#262272" label text-color="white">
                      {{ getDday(item.date) }}
                    </v-chip>
                  </v-col>
                  <v-col class="pt-2 pb-0">
                    <v-btn
                    text
                    class="px-2 mt-1"
                      v-if="
                        item.date.slice(5, 7) == todaytime.getMonth() + 1 &&
                        item.date.slice(8, 10) == todaytime.getDate() &&
                        item.date.slice(11, 13) == todaytime.getHours()
                      "
                      @click="startCounseling(item.room)"
                      style="font-size: 1rem; color: white;background-color:crimson; font-weight: bold; border:2px solid; border-radius:20px;height:30px;"
                      
                      >ON-AIR</v-btn
                    >
                    <v-btn
                      v-else
                      disabled
                      style="font-size: 1rem; color: black"
                      text
                      >on-Air</v-btn
                    >
                  </v-col>
                </div>
                <div align="left" class="mt-1 pl-3">
                  <h4>{{ item.date.slice(0,10) }}</h4>
                </div>
                <div>
                  <h1>{{ item.date.slice(11,16) }}</h1>
                </div>
                <div align="right">
                  {{ findName(item.mentor) }} 상담사
                </div>
                <v-btn large icon color="red" @click="cancelD(item.num)"
                  ><v-icon>mdi-delete-forever-outline</v-icon></v-btn
                >
              </v-card>
            </v-col>
          </v-row>
          <v-pagination
            v-model="rpage"
            v-if="rpagingList.length != 0"
            :length="rpageLength"
            circle
            class="pb-3 mt-5"
            color="#262272"
          ></v-pagination>
        </v-sheet>
      </v-tab-item>
    </v-tabs-items>

    <v-card> </v-card>

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

    <v-dialog
      v-model="typeDialog"
      max-width="500"
      min-height="500"
      style="height: 500px"
    >
      <v-card>
        <v-card-title class="justify-center pt-3">
          <span style="font-size: 2rem">상담 유형을 선택하세요</span>
        </v-card-title>
        <v-divider></v-divider>
        <v-card-text class="pt-5 pb-1">
          <v-row class="justify-center">
            <v-col cols="5" @click="reapply" class="mr-5 type-box"
              ><div class="type-title">실시간 상담</div></v-col
            >
            <v-col cols="5" @click="reRecord" class="type-box"
              ><div class="type-title">녹화 상담</div></v-col
            >
          </v-row>
        </v-card-text>

        <v-card-actions class="px-0 py-0">
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="typeDialog = false">
            닫기
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog" persistent max-width="750">
      <v-card
        ><br />
        <v-card-subtitle>
          <span><h1 style="color: black">실시간 상담 재신청</h1></span>
          <!-- {{ reMentor }}-->
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
            <v-col class="pb-0">
              <v-date-picker
                v-model="date"
                :allowed-dates="allowedDates"
                color="#a23bbe"
              ></v-date-picker>
            </v-col>
            <v-col class="pb-0">
              <div class="px-7" style="height: 100%">
                <div style="height: 20%">
                  <v-select
                    v-model="time"
                    :items="timeItems"
                    :label="selLabel"
                    solo
                  ></v-select>
                </div>
                <div style="height: 60%"></div>
                <div align="right" style="height: 20%">
                  <v-btn color="red darken-1" text @click="dialog = false">
                    취소
                  </v-btn>
                  <v-btn color="#262272" text @click="reqReapply()">
                    신청
                  </v-btn>
                </div>
              </div>
            </v-col>
          </v-row>
        </v-container>
        <!-- <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">
            취소
          </v-btn>
          <v-btn color="blue darken-1" text @click="reqReapply()"> 신청 </v-btn>
        </v-card-actions> -->
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
          <v-btn color="#262272" text @click="cancelDialog = false">
            아니오
          </v-btn>
          <v-btn color="#262272" text @click="cancel"> 예 </v-btn>
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
import { mapGetters } from "vuex";
import moment from "moment";

export default {
  name: "MyListComp",
  data() {
    return {
      myList: [],
      cpagingList: [],
      cpageLength: 0,
      rpagingList: [],
      rpageLength: 0,
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
      cpage: 1,
      rpage: 1,
      selitem: [],
      mentorNum: null,
      cancelNum: "",
      answerDialog: false,
      answer: "",
      todaytime: new Date(),
      tab: null,
      timeItems: [],
      selLabel: "시간을 선택해주세요.",
      model: null,
      errorSnack: false,
      successSnack: false,
      altMsg: "",
    };
  },
  mounted() {
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/menteeMyList/${this.getUserNum}`).then((res) => {
      this.myList = res.data;
      this.cpagingList = this.myList.slice(0, 8);
      if (this.myList.length % 8 == 0) {
        this.cpageLength = this.myList.length / 8;
      } else {
        this.cpageLength = parseInt(this.myList.length / 8) + 1;
      }
    });
    http.get(`/schedule/isReservation/${this.getUserID}`).then((res) => {
      this.isReservation = res.data;
    });
    http.get(`/schedule/myReservation/${this.getUserID}`).then((res) => {
      this.myReservation = res.data;
      // console.log(res.data)
      this.rpagingList = this.myReservation.slice(0, 8);
      // console.log(this.rpagingList)
      if (this.myReservation.length % 8 == 0) {
        this.rpageLength = this.myReservation.length / 8;
      } else {
        this.rpageLength = parseInt(this.myReservation.length / 8) + 1;
      }
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
    findNameById(userid) {
      for (let index = 0; index < this.userList.length; index++) {
        if (this.userList[index].id == userid) {
          return this.userList[index].name;
        }
      }
    },
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
      var momentz = moment(new Date(date));
      require("moment-timezone");
      return momentz.tz("Asia/Seoul").format("YYYY-MM-DD HH:mm");
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
        // console.log(this.schedule);
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
      var mentorId = this.findID(this.selitem.mentor);
      if (this.date != null && this.time != null) {
        http
          .get(
            `/schedule/findScheduleNum/${this.date}/${this.time.substr(
              0,
              5
            )}/${mentorId}`
          )
          .then((res) => {
            http
              .post(`/schedule/reApply/${this.conRoomNum}`, {
                mentee: this.getUserID,
                scheNum: res.data,
              })
              .then((success) => {
                if (success.data == "success") {
                  http.post(`/counseling/reserveRequest`, {
                    mentee: this.getUserNum,
                    mentor: this.mentorNum,
                    room: this.createRoomId(),
                    status: "reapply",
                    date: `${this.date}T${this.time.substr(0, 5)}:00`,
                  });
                  this.successSnack = true;
                  this.altMsg = "재상담 신청 완료되었습니다.";
                  http
                    .get(`/counseling/menteeMyList/${this.getUserNum}`)
                    .then((res) => {
                      this.myList = res.data;
                      this.cpagingList = this.myList.slice(0, 9);
                    });
                  http
                    .get(`/schedule/myReservation/${this.getUserID}`)
                    .then((data) => {
                      this.myReservation = data.data;
                      console.log(this.myReservation)
                      this.rpagingList = this.myReservation.slice(0, 8);
                      if (this.myReservation.length % 8 == 0) {
                        this.rpageLength = this.myReservation.length / 8;
                      } else {
                        this.rpageLength =
                          parseInt(this.myReservation.length / 8) + 1;
                      }
                    });
                  this.dialog = false;
                }
              });
          });
      } else {
        this.errorSnack = true;
        this.altMsg = "날짜와 시간을 선택해주세요.";
      }
    },
    cancel() {
      this.cancelDialog = false;
      http
        .delete(`/schedule/cancelReservation/${this.cancelNum}`)
        .then((res) => {
          if (res.data == "success") {
            this.successSnack = true;
            this.altMsg = "예약이 취소되었습니다.";
            http
              .get(`/schedule/myReservation/${this.getUserID}`)
              .then((data) => {
                this.myReservation = data.data;
                      console.log(this.myReservation)

                this.rpagingList = this.myReservation.slice(0, 8);
                if (this.myReservation.length % 8 == 0) {
                  this.rpageLength = this.myReservation.length / 8;
                } else {
                  this.rpageLength =
                    parseInt(this.myReservation.length / 8) + 1;
                }
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
      // console.log(item.answer);
      this.answer = item.answer
        .substring(0, item.answer.length)
        .replaceAll("\r\n", "<br/>");
      this.answerDialog = true;
    },
    reRecord() {
      this.$router.push(`/recordConsult/${this.selitem.mentor}`);
    },
    startCounseling(room) {
      this.$router.push(`/userWRTC/${room}`);
    },
    getDday(day) {
      var year = day.slice(0, 4);
      var month = day.slice(5, 7);
      var d = day.slice(8, 10);
      var Dday = new Date(year, month - 1, d);
      var now = new Date();
      var result = now.getDate() - Dday.getDate();

      if (result > 0) {
        result = "D+" + result;
      } else if (result == 0) {
        result = "D-Day";
      } else {
        result = "D" + result;
      }

      return result;
    },
  },
  watch: {
    cpage(page) {
      var first = (page - 1) * 8;
      this.cpagingList = this.myList.slice(first, first + 8);
    },
    rpage(page) {
      var first = (page - 1) * 8;
      this.rpagingList = this.myReservation.slice(first, first + 8);
    },
    date(v) {
      var mentorId = this.findID(this.selitem.mentor);
      var hour = new Date().getHours();
      this.timeItems = [];
      http.get(`/schedule/allowScheduleTime/${mentorId}/${v}`).then((res) => {
        for (const stime of res.data) {
          if (v.split("-")[2] == new Date().getDate().toString()) {
            if (stime.substr(0, 2) > hour) {
              this.timeItems.push(stime);
            }
          } else {
            this.timeItems.push(stime);
          }
        }
        if (this.timeItems.length == 0) {
          this.selLabel = "예약 가능한 시간이 없습니다.";
          this.timeItems.push("예약 가능한 시간이 없습니다.");
        } else {
          this.selLabel = "시간을 선택해주세요.";
        }
      });
    },
  },
};
</script>

<style scoped>
.type-box {
  border: 3px solid #262272;
  font-size: 1.3rem;
  cursor: pointer;
  height: 70px;
  border-radius: 30px;
  background: #262272;
}
.type-title {
  color: white;
  width: 100%;
  height: 100%;
  padding-top: 5%;
  transform: scale(1);
  transition: all 0.3s;
}
.type-title:hover {
  transform: scale(1.2);
  transition: all 0.3s;
  color: #d9d6ff;
}
</style>
