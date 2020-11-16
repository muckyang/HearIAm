<template>
  <div>
      <v-tabs v-if="page > 0" color="#262272" right v-model="nowPage">
        <v-tab v-for="i in page" :key="i" class="tab-menu-page">
          {{ i }}
        </v-tab>
        <v-tab-item v-for="n in page" :key="n">
          <v-container class="pt-0">
            <v-row>
              <v-col v-for="(wait, index) in waitList" :key="index" cols="12" sm="6" md="3">
                <v-card class="mx-auto">
                  <div style="overflow:hidden; height:130px;">
                  <img id="rcard" :src="getImg(wait)" style="height:130px; width:70%;">
                  <!-- <img id="rcard" src="../../../public/wordcloud/keyword1604473900392.png" style="height:130px; width:70%;"> -->
                  </div>
                  <v-card-text class="pt-1 pb-2">
                    <p class="mb-1 text-area" style="text-align: left;">#{{ wait.keyword1 }} #{{ wait.keyword2 }} #{{ wait.keyword3 }}</p>
                    <p class="mb-2 text-area" style="font-size: 1rem; color: black; text-align: left;">
                      {{ wait.title }}
                    </p>
                    <v-btn class="m-2" color="#262272"  @click="setNum(wait.num)" style="color:white;border-radius:20px;"> 상담하기 </v-btn>
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </v-container>
        </v-tab-item>
      </v-tabs>
      <div v-if="this.waitList.length <= 0">
          <div style="margin-top:130px;font-size:1.5rem">
            
            <img src="@/assets/noResult3.png" style="height:200px; width:200px;">
            <br>
            등록된 녹음 상담이 없습니다.</div>
      </div>
      <v-dialog v-model="getDialog" persistent max-width="400">
        <v-card>
          <v-card-title style="font-size: 1.5rem">
            상담을 담당하시겠습니까?
          </v-card-title>
          <v-card-text align="left">한 번 담당한 내역은 마이페이지에 저장됩니다.</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="#262272" text @click="getDialog = false">
              아니오
            </v-btn>
            <v-btn color="#262272" text @click="goDetail(selNum)"> 예 </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
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
      selNum:0,
      getDialog : false,
    };
  },
  created() {
    this.getWaitList();
    this.getTotal();
  },
  methods: {
    setNum(num){
      this.getDialog = true;
      this.selNum = num;
    },
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
      return "../../../wordcloud/" + item.wordcloudImg
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