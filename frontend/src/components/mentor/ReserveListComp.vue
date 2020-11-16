<template>
  <div>
    <v-card-text
      v-if="conList.length > 0"
      style="padding-right: 70px; padding-left: 70px"
    >
      <v-timeline align-top dense>
        <v-timeline-item
          color="#262272"
          small
          v-for="(item, index) in conList"
          :key="index"
        >
          <v-row class="pt-1">
            <v-col class="col-lg-4 col-md-4 col-sm-3 col-xs-10">
              <span style="color:black; font-size:1.1rem; !important">{{
                setTime(item.date)
              }}</span>
            </v-col>
            <v-col class="col-lg-4 col-md-4 col-sm-3 col-xs-10">
              <span style="color:black; font-size:1.1rem; !important">
                <span style="font-size: 1.2rem; color: #262272; font-weight:bold;">{{
                  findName(item.mentee)
                }}</span>
                님
              </span>
              <!-- <v-btn small disabled style="font-size:0.9rem;">{{ findName(item.mentee) }}</v-btn> -->
              <div class="caption">
                <!-- <v-btn
                  v-if="
                    item.date.slice(5, 7) == todaytime.getMonth() + 1 &&
                    item.date.slice(8, 10) == todaytime.getDate() &&
                    item.date.slice(11, 13) == todaytime.getHours()
                  "
                  small
                  text
                  text-color="red"
                  @click="startCounseling(item)"
                  style="font-size: 1.1rem; color: red; font-weight: bold"
                  >ON-AIR</v-btn
                > -->

                <v-tooltip
                  right
                  v-if="
                    item.date.slice(5, 7) == todaytime.getMonth() + 1 &&
                    item.date.slice(8, 10) == todaytime.getDate() &&
                    item.date.slice(11, 13) == todaytime.getHours()
                  "
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn
                      text
                      class="px-2 mt-1"
                      @click="startCounseling(item)"
                      style="font-size: 1.1rem; color: white;background-color:crimson; font-weight: bold; border:2px solid; border-radius:20px;height:30px;"
                      v-bind="attrs"
                      v-on="on"
                      >ON-AIR</v-btn
                    >
                  </template>
                  <span>클릭하면 상담 시작!</span>
                </v-tooltip>

                <v-btn
                  v-else
                  disabled
                  text
                  style="font-size: 1.1rem; color: black"
                  >on-Air</v-btn
                >
              </div>
            </v-col>
            <v-col class="col-lg-4 col-md-4 col-sm-3 col-xs-10">
              <v-btn v-if ="item.status == 'reapply'" 
                small
                @click="MenteeHistory(item)"
                style="font-size: 0.9rem; height: 3.3rem; width:50%;border-radius:5px;"
                class="main-btn"
                >이전 상담내역<br>GO</v-btn
              >
              <p v-else class="mt-3" style="font-size : 18px"  
             
                >이전 상담내역 없음 </p>
            </v-col>
          </v-row>
        </v-timeline-item>
      </v-timeline>
    </v-card-text>
    <div v-else class="mx-auto mt-7" max-width="100%">
      <br />
      <p>예약 내역이 없습니다.</p>
      <br />
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common.js"; // ~~~ 서버주소/api
import { mapGetters } from "vuex";
export default {
  name: "ReserveListComp",
  data() {
    return {
      pagingList: [],
      pageLength: 0,
      page: 1,
      userList: [],
      conList: [],
      todaytime: new Date(),
    };
  },
  mounted() {
    // console.log(this.todaytime.getHours());
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/ReserveList/${this.getUserNum}`).then((res) => {
      this.conList = res.data;
      // console.log(this.conList)
      // this.pagingList = this.conList.slice(0, 9);
      // if (this.conList.length % 10 == 0) {
      //   this.pageLength = this.conList.length / 10;
      // } else {
      //   this.pageLength = parseInt(this.conList.length / 10) + 1;
      // }
    });
  },
  computed: {
    ...mapGetters([
      "getUserNum",
      //   "isProfileLoaded",
      //   "getRole",
      //   "getQualification",
      //   "getUserName",
      //   "getUserID",
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
        "  " +
        date.slice(11, 13) +
        ":" +
        date.slice(14, 16);
      return time;
    },
    startCounseling(item) {
      this.$router.push(`/counselorWRTC/${item.room}&${item.num}`);
    },
    MenteeHistory(item) {
      let menteeName = "";
      for (let index = 0; index < this.userList.length; index++) {
        if (this.userList[index].num == item.mentee) {
          menteeName = this.userList[index].name;
        }
      }
      this.$router.push(`/myMenteeInfo/${item.mentee}&${menteeName}`);
    },
    getKeyword(item) {
      let str = "";
      if (item.keyword1 != "none" && item.keyword1 != null) {
        str += "#" + item.keyword1 + " ";
      }

      if (item.keyword2 != "none" && item.keyword2 != null) {
        str += "#" + item.keyword2 + " ";
      }

      if (item.keyword3 != "none" && item.keyword3 != null) {
        str += "#" + item.keyword3 + " ";
      }

      return str;
    },
    menteeInfo(num, name) {
      this.$router.push(`/myMenteeInfo/${num}&${name}`);
    },
  },
  watch: {
    // page(page) {
    //   var first = (page - 1) * 10;
    //   this.pagingList = this.myList.slice(first, first + 9);
    // },
  },
};
</script>
