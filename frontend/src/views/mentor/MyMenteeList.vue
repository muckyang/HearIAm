<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background-color: #0f0d2d;
      padding-top: 70px;
    "
    align="center"
  >
    <div class="px-5 pt-3 content-box">
      <h1 class="mb-1">청소년 일지 관리</h1>
      <span>내가 상담한 청소년의 상담 일지를 날짜별로 확인할 수 있습니다.</span>
      <!-- <v-divider class="mt-3 mb-3"></v-divider> -->
      <div v-if="menteeList == ''">
        <div style="margin-top:130px;font-size:1.5rem">
          <img src="@/assets/noResult3.png" style="height:200px; width:200px;" />
          <br />
          상담한 청소년이 없습니다.
        </div>
      </div>
      <v-row class="mt-5" style="padding-left:50px;padding-right:50px">
        <v-col cols="12" sm="6" md="2" v-for="item in menteeList" :key="item.num">
          <v-card :color="item.color" @click="menteeInfo(item.num, item.name)" height="200" style="overflow:hidden;" elevation="4">
            <div class="one-box" align="center" style="height:100%;width:100%;">
              <div style="width: 50%; height: 50%" class="pt-5">
                <v-img :src="getIcon(item.icon)"></v-img>
              </div>
              <!-- <v-img :src="`../../assets/icons/${item.icon}`" ></v-img> -->
              <!-- <v-img src="../../assets/icons/bear.png" ></v-img> -->
              <div style="height:50%;">
                <p class="my-0 pt-12" style="font-size:1.2rem;font-weight:bold;display:table-cell;vertical-align:middle;">
                  {{ item.name }}
                </p>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>
      <div style="position:absolute; bottom:2.3rem;">
        <span class="icon-line" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span>
      </div>
    </div>
  </div>
</template>

<script>
import http from '@/util/http-common.js';
import { mapGetters } from 'vuex';

export default {
  name: 'MyMenteeList',
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
    ...mapGetters(['isProfileLoaded', 'getRole', 'getQualification', 'getUserName', 'getUserNum', 'getUserID']),
  },
  methods: {
    menteeInfo(num, name) {
      this.$router.push(`/myMenteeInfo/${num}&${name}`);
    },
    getIcon(icon) {
      // console.log(icon);
      return '../../../icons/' + icon;
    },
    goBack() {
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
.one-box {
  transform: scale(1);
  transition: all 0.3s;
}
.one-box:hover {
  transform: scale(1.1);
  transition: all 0.3s;
}
</style>
