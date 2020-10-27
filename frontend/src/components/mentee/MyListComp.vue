<template>
  <div>
    <h1>나의 상담 내역</h1><br/>
    <v-card>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-center">날짜</th>
              <th class="text-center">상담사</th>
              <th class="text-center">종류</th>
              <th class="text-center">재신청</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in myList" :key="item.name">
              <td class="text-center">{{ setTime(item.date) }}</td>
              <td class="text-center">{{ findName(item.mentor) }}</td>
              <td v-if="item.status == `record`" class="text-center">
                녹화 상담
              </td>
              <td v-else class="text-center">실시간 상담</td>
              <td class="text-center"><v-btn>재신청</v-btn></td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card>
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
    };
  },
  mounted() {
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/menteeMyList/${this.getUserNum}`).then((res) => {
      this.myList = res.data;
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
    setTime(date) {
      let time =
        Number(date.slice(0, 4)) +
        "년 " +
        Number(date.slice(5, 7)) +
        "월 " +
        Number(date.slice(8, 10)) +
        "일 " +
        Number(date.slice(11, 13)) +
        "시 " +
        Number(date.slice(14, 16)) +
        "분";
      return time;
    },
  },
};
</script>

<style>
</style>