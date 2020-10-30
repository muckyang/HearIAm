<template>
  <div class="reserve-main">
    <div class="reserve-body">
      <v-sheet class="sheet-body container" rounded="xl" elevation="7">
        <h1 class="mb-5">실시간 상담 예약하기</h1>
        <v-row class="d-flex justify-content-center">
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
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
                <v-btn text color="primary" @click="dateModal = false">
                  Cancel
                </v-btn>
                <v-btn text color="primary" @click="setDate"> OK </v-btn>
              </v-date-picker>
            </v-dialog>
          </v-col>
        </v-row>
        <!-- <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <v-dialog ref="dialogTime" v-model="timeModal" :return-value.sync="time" persistent width="290px">
              <template v-slot:activator="{ on, attrs }">
                <div class="d-flex">
                  <v-icon class="mr-3" large color="black">mdi-clock-time-four-outline</v-icon>
                  <v-text-field
                    class="mt-7"
                    v-model="time"
                    label="시간을 선택해주세요."
                    readonly
                    solo
                    v-bind="attrs"
                    v-on="on"
                  ></v-text-field>
                </div>
              </template>
              <v-time-picker v-if="timeModal" v-model="time" full-width>
                <v-spacer></v-spacer>
                <v-btn text color="primary" @click="timeModal = false">
                  Cancel
                </v-btn>
                <v-btn text color="primary" @click="$refs.dialogTime.save(time)">
                  OK
                </v-btn>
              </v-time-picker>
            </v-dialog>
          </v-col>
        </v-row> -->
        <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <div class="d-flex">
              <v-icon class="mr-3" large color="black"
                >mdi-clock-time-four-outline</v-icon
              >
              <v-select
                class="mt-7"
                v-model="time"
                :items="timeItems"
                label="시간을 선택해주세요."
                hint="예약 가능한 시간만 보여집니다."
                solo
                persistent-hint
              ></v-select>
            </div>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
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
        <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
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
        <v-row>
          <v-col cols="2" class="reserve-data pt-10 px-0">
            <v-btn color="#ffdc15" @click="reserveD">예약하기</v-btn>
          </v-col>
        </v-row>
      </v-sheet>
    </div>
      <v-dialog v-model="reserDialog" persistent max-width="400">
      <v-card>
        <v-card-title style="font-size:1.5rem;">
          아래 정보로 예약하시겠습니까?
        </v-card-title>
        <v-card-text class="pt-3" style="text-align:left;">날짜 : {{this.date}} <br>시간 : {{this.time}}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            @click="reserDialog = false"
          >
            아니오
          </v-btn>
          <v-btn
            color="green darken-1"
            text
            @click="reserve"
          >
            예
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapGetters, mapState } from "vuex";
export default {
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
      reserDialog:false,
    };
  },
  computed: {
    getEndDate() {
      var endDate = new Date();
      endDate.setDate(this.nDate.getDate() + 7);
      return endDate.toISOString().slice(0, 10);
    },
    ...mapGetters(["getUserID","getUserNum"]),
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
  },
  created() {
    http
      .get(`/schedule/getTimeByDate/${this.nowDate}`)
      .then((res) => {
        if (res.data.length == 0) {
          this.timeItems.push("예약 가능한 시간이 없습니다.");
        } else {
          this.timeItems = res.data;
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
            this.timeItems = res.data;
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
          http.get(`/user/users/${res.data.mentor}`)
            .then((res1) => {
              this.mentorNum = res1.data.num;
              http
                .post(`/counseling/reserveRequest`, {
                  mentee: this.getUserNum,
                  mentor: this.mentorNum,
                  room: this.createRoomId(),
                  status: "reserve",
                  date: `${res.data.sdate}T${res.data.stime}:00`
                });
            });
          alert("예약이 완료되었습니다.");
          this.$router.push("/menteeMain").catch(() => {});
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
        alert("예약 정보를 모두 입력해주세요.");
        return;
      } else if (this.time == "예약 가능한 시간이 없습니다.") {
        alert("예약 시간을 다시 확인해주세요.");
        return;
      } else {
        this.reserDialog = true;
      }
    },
  },
};
</script>

<style scoped>
.reserve-main {
  background-image: linear-gradient(to right, #93dfff, #f5a2bb);
  height: 100%;
}
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
.reserve-data {
  margin: 0 auto;
}
</style>
