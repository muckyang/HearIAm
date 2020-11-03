<template>
  <div>
    <v-timeline align-top :dense="$vuetify.breakpoint.smAndDown">
      <v-timeline-item v-for="(item, index) in conList" :key="index">
        <v-card color="#bbcfe9">
          <v-card-title class="title py-0" style="height:50px;">
            <h2 style="font-size:1rem;" class="mb-0">
              {{ item.title }}
            </h2>
          </v-card-title>
          <v-card-text class="white text--primary pt-4">
            <div>
              <p>
                {{ getKeyword(item) }}
              </p>
              <p>
                <v-tooltip left>
                  <template v-slot:activator="{ on, attrs }">
                    <span style="color:pink" v-bind="attrs" v-on="on" id="mentee-name-text" @click="MenteeHistory(item)">{{ findName(item.mentee) }}</span>
                  </template>
                  <span>클릭하여 상담내역 보기</span>
                </v-tooltip>
                <span>님의 음성 상담입니다.</span>
              </p>
              <p>
                {{ setTime(item.date) }}
              </p>
            </div>
            <v-btn small @click="startCounseling(item)" style="font-size:0.9rem;" color="#bbcfe9">상담시작</v-btn>
          </v-card-text>
        </v-card>
      </v-timeline-item>
    </v-timeline>
  </div>
</template>

<script>
import http from '@/util/http-common.js'; // ~~~ 서버주소/api
import { mapGetters } from 'vuex';
export default {
  name: 'ReRecordListComp',
  data() {
    return {
      userList: [],
      conList: [],
    };
  },
  mounted() {
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/RecordList/${this.getUserNum}`).then((res) => {
      this.conList = res.data;
    });
  },
  computed: {
    ...mapGetters([
      'getUserNum',
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
      let time = date.slice(0, 4) + '-' + date.slice(5, 7) + '-' + date.slice(8, 10);
      return time;
    },
    startCounseling(item) {
      //상담시작
      this.$router.push(`/recordDetail/${item.num}`);
    },
    MenteeHistory(item) {
      let menteeName = '';
      for (let index = 0; index < this.userList.length; index++) {
        if (this.userList[index].num == item.mentee) {
          menteeName = this.userList[index].name;
        }
      }
      this.$router.push(`/myMenteeInfo/${item.mentee}&${menteeName}`);
    },
    getKeyword(item) {
      let str = '';
      if (item.keyword1 != 'none' && item.keyword1 != null) {
        str += '#' + item.keyword1 + ' ';
      }

      if (item.keyword2 != 'none' && item.keyword2 != null) {
        str += '#' + item.keyword2 + ' ';
      }

      if (item.keyword3 != 'none' && item.keyword3 != null) {
        str += '#' + item.keyword3 + ' ';
      }

      return str;
    },
    menteeInfo(num, name) {
      this.$router.push(`/myMenteeInfo/${num}&${name}`);
    },
  },
  watch: {},
};
</script>

<style scoped>
#mentee-name-text {
  cursor: pointer;
}
</style>
