<template>
  <div class="pt-7">
    <!-- <v-sheet class="mx-auto" elevation="5" max-width="100%"> -->
      <v-slide-group v-model="model" class="pa-4" show-arrows center-active>
        <v-slide-item v-for="(n, idx) in conList" :key="idx" v-slot="{ active, toggle }">
          <v-card class="ma-4" height="200" width="200" @click="toggle" :disabled="active?true:false">
            <v-img v-if="!active" :src="getImg(n)" height="100%" width="100%"></v-img>
            <v-img v-else :src="getImg(n)" height="100%" width="100%" style="filter: brightness(50%);"></v-img>
          </v-card>
        </v-slide-item>
      </v-slide-group>

      <v-expand-transition>
        <v-sheet v-if="conList.length != 0" height="200" tile class="pt-5">
          <div>
            <p style="font-size:1.3rem;">{{ getKeyword(conList[model]) }}</p>
          </div>
          <div>
            <p>
              <v-tooltip left>
                <template v-slot:activator="{ on, attrs }">
                  <span style="color:#262272;font-weight:bold;font-size:1.3rem;" v-bind="attrs" v-on="on" id="mentee-name-text" @click="MenteeHistory(conList[model])">
                    {{ findName(conList[model].mentee) }}
                  </span>
                </template>
                <span>클릭하여 상담내역 보기</span>
              </v-tooltip>
              <span>님의 음성 상담입니다.</span>
            </p>
          </div>
          <div>
            <p style="font-size:1rem;"> 등록일자 : {{ setTime(conList[model].date) }} </p>
          </div>
          <div>
            <v-btn @click="startCounseling(conList[model])" class="main-btn mt-3" style="height:3rem">상담시작</v-btn>
          </div>
        </v-sheet>
        <v-sheet v-else >
           <div>
            <p> 녹화 상담 리스트가 없습니다!</p>
            <br/>
          </div>
        </v-sheet>
      </v-expand-transition>
    <!-- </v-sheet> -->
    <!-- <v-timeline align-top :dense="$vuetify.breakpoint.smAndDown">
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
                    <span style="color:pink" v-bind="attrs" v-on="on" id="mentee-name-text" @click="MenteeHistory(item)">{{
                      findName(item.mentee)
                    }}</span>
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
    </v-timeline> -->
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
      model: null,
    };
  },
  created() {
    http.get(`/user/userName`).then((res) => {
      this.userList = res.data;
    });
    http.get(`/counseling/RecordList/${this.getUserNum}`).then((res) => {
      this.conList = res.data;
      this.model = 0;
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

      // console.log(item)
      let str = '';
      
      if (item.keyword1 != null && item.keyword1 != 'none') {
        str += '#' + item.keyword1 + ' ';
      }

      if ( item.keyword2 != null &&item.keyword2 != 'none') {
        str += '#' + item.keyword2 + ' ';
      }

      if (item.keyword3 != null && item.keyword3 != 'none' ) {
        str += '#' + item.keyword3 + ' ';
      }

      return str;
    },
    getImg(item){
      // console.log(item)
      return "../../wordcloud/"+item.wordcloudImg  
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
