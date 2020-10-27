<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background: linear-gradient(to left, #93dfff, #f5a2bb);
    "
  >
    <v-container>
      <h1>청소년 목록</h1>
      <br />
      <v-row>
        <v-col cols="2" v-for="item in menteeList" :key="item.num">
          <v-card @click="menteeInfo(item.num, item.name)" height="200px">
            {{ item.name }}
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapGetters } from "vuex";

export default {
  name: "MyMenteeList",
  data() {
    return {
      menteeList: [],
    };
  },
  mounted() {
    http.get(`/counseling/myMenteeList/${this.getUserNum}`).then((res) => {
      this.menteeList = res.data;
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
    menteeInfo(num, name) {
      this.$router.push(`/myMenteeInfo/${num}&${name}`);
    },
  },
};
</script>

<style>
</style>