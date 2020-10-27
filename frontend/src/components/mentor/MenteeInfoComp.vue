<template>
  <div>
    <v-card>
      <h1 class="text-center">{{ name }}</h1>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-center">날짜</th>
              <th class="text-center">종류</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in myMenteeInfoList" :key="item.name" @click="goInfo(item.num)">
              <td class="text-center">{{ setTime(item.date) }}</td>
              <td v-if="item.status == `record`" class="text-center">
                녹화 상담
              </td>
              <td v-else class="text-center">실시간 상담</td>
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
  name: "MenteeInfoListComp",
  data() {
    return {
      myMenteeInfoList: [],
      name: "",
    };
  },
  created() {
    http
      .get(
        `/counseling/myMenteeInfoList/${this.getUserNum}/${this.$route.params.num}`
      )
      .then((res) => {
        this.myMenteeInfoList = res.data;
      });
    this.name = this.$route.params.name;
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
    setTime(date) {
      let time =
        Number(date.slice(0, 4)) +
        "년 " +
        Number(date.slice(5, 7) - 1) +
        "월 " +
        Number(date.slice(8, 10)) +
        "일 " +
        Number(date.slice(11, 13)) +
        "시 " +
        Number(date.slice(14, 16)) +
        "분";
      return time;
    },
    goInfo(num){
      this.$router.push(`/counselingInfo/${num}`);
    }
  },
};
</script>

<style>
</style>