<template>
  <div class="record-main container">
      <v-tabs v-if="page > 0" color="#262272" right v-model="nowPage">
        <v-tab v-for="i in page" :key="i" class="tab-menu-page">
          {{ i }}
        </v-tab>
        <v-tab-item v-for="n in page" :key="n">
          <v-container>
            <v-row>
              <v-col v-for="(wait, index) in waitList" :key="index" cols="12" sm="6" md="3">
                <v-card class="mx-auto">
                  <div style="overflow:hidden; height:140px;">
                  <img id="rcard" :src="getImg(wait)" style="height:140px; width:80%;">
                  <!-- <img id="rcard" src="../../../public/wordcloud/keyword1604473900392.png" style="height:140px; width:80%;"> -->
                  </div>
                  <v-card-text class="pt-1 pb-2">
                    <p class="mb-1 text-area" style="text-align: left;">#{{ wait.keyword1 }} #{{ wait.keyword2 }} #{{ wait.keyword3 }}</p>
                    <p class="mb-2 text-area" style="font-size: 1rem; color: black; text-align: left;">
                      {{ wait.title }}
                    </p>
                    <v-btn class="m-2" color="#262272" small @click="goDetail(wait.num)" style="color:white;"> 상담하기 </v-btn>
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </v-container>
        </v-tab-item>
      </v-tabs>
      <div v-if="this.waitList.length <= 0">
          <h1 style="margin-top:250px;">등록된 녹음 상담이 없습니다.</h1>
      </div>
  </div>
</template>

<script>
import http from '@/util/http-common.js';
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'RecordList',
  data() {
    return {
      waitList: [],
      page: -1,
      nowPage: 0,
      total: 0,
    };
  },
  created() {
    this.getWaitList();
    this.getTotal();
  },
  methods: {
    getWaitList() {
      http
        .get('record/getRecordPage/' + this.nowPage)
        .then((res) => {
          this.waitList = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goDetail(num) {
      http
        .post(`record/getRecordConsult/${num}`, this.getUserNum)
        .then(() => {
          this.$router.push(`/recordDetail/${num}`);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getTotal() {
      http
        .get('/record/count')
        .then((res) => {
          this.total = res.data;
          if(this.total % 8 == 0) {
            this.page = Math.floor(this.total / 8);
          } else {
            this.page = Math.floor(this.total / 8 + 1);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getImg(item){
      return "https://k3b202.p.ssafy.io/wordcloud/" + item.wordcloudImg
    }
  },
  computed: {
    ...mapGetters(['getUserNum', 'getUserID']),
    ...mapState({
      userNum: (state) => `${state.user.getUserNum}`,
      userID: (state) => `${state.user.getUserID}`,
    }),
  },
  watch: {
    nowPage() {
      http
        .get('record/getRecordPage/' + this.nowPage)
        .then((res) => {
          this.waitList = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.record-main {
  height: 100%;
  overflow: scroll;
}
.record-header {
  height: 19%;
}
.text-area {
  text-overflow:ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
#rcard{
  transform: scale(1);
  transition: all 0.3s;
}
#rcard:hover{
  transform: scale(1.1);
  transition: all 0.3s;
}
</style>