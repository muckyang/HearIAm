<template>
  <div>
    <h1>내가 담당한 녹화 상담 리스트</h1>
    <br />
    <v-card>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-center">등록일시</th>
              <th class="text-center">닉네임</th>
              <th class="text-center">키워드</th>
              <th class="text-center">종류</th>
              <th class="text-center">비고</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in pagingList" :key="item.name">
              <td class="text-center">
                {{ setTime(item.date) }}</td>

              <td v-if="item.mentor == 1" class="text-center">-</td>

              <td v-else class="text-center">
                <v-btn
                  small
                  @click="MenteeHistory(item)"
                  style="font-size:0.9rem;"
                  >{{ findName(item.mentee) }}</v-btn
                ></td>
                  <td  class="text-center">
                {{ getKeyword(item) }}
              </td>
              <td  class="text-center">
                녹화 상담
              </td>
              <td class="text-center">
                <v-btn
                  small
                  @click="startCounseling(item)"
                  style="font-size:0.9rem;"
                  >상담시작</v-btn
                >
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table> </v-card
    ><br />
    <v-pagination v-model="page" :length="pageLength" circle></v-pagination>
  </div>
</template>

<script>
import http from "@/util/http-common.js";// ~~~ 서버주소/api
import { mapGetters } from "vuex";
export default {
  name: "ReRecordListComp",
  data() {
    return {
      pagingList: [],
      pageLength: 0,
      page: 1,
      userList: [],
      conList:[]
    };
  },
  mounted() {
      http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/RecordList/${this.getUserNum}`).then((res) => {
      this.conList = res.data;
      this.pagingList = this.conList.slice(0, 9);
      if (this.conList.length % 10 == 0) {
        this.pageLength = this.conList.length / 10;
      } else {
        this.pageLength = parseInt(this.conList.length / 10) + 1;
      }
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
        " " +
        date.slice(11, 13) +
        ":" +
        date.slice(14, 16);
      return time;
    },
    startCounseling(item){
        //상담시작
        console.log(item.mentee + " 청소년에 대한 ")
        console.log(this.getUserNum + " 상담사의 상담 시작!")
    },
    MenteeHistory(item){
      let menteeName = "";
       for (let index = 0; index < this.userList.length; index++) {
        if (this.userList[index].num == item.mentee) {
          menteeName = this.userList[index].name;
        }
      }
      this.$router.push(`/myMenteeInfo/${item.mentee}&${menteeName}`)
    },
    getKeyword(item){
      let str = ""
      if(item.keyword1 != "none" && item.keyword1 !=null){
        str += "#" + item.keyword1 +" "
      }
      
      if(item.keyword2 != "none" && item.keyword2 !=null){
        str += "#" + item.keyword2 +" "
      }
      
      if(item.keyword3 != "none" && item.keyword3 !=null){
        str += "#" + item.keyword3 +" "
      }

      return str;
    },
    menteeInfo(num, name) {
      this.$router.push(`/myMenteeInfo/${num}&${name}`);
    },
  },
  watch: {
    page(page) {
      var first = (page - 1) * 10;
      this.pagingList = this.myList.slice(first, first + 9);
    },
  },
};
</script>
