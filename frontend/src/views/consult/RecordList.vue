<template>
  <div class="record-main container">
    <v-card style="height: 95%;">
      <v-tabs color="deep-purple accent-4" right v-model="nowPage">
        <v-tab v-for="i in page" :key="i" class="tab-menu-page">
          {{ i }}
        </v-tab>

        <v-tab-item v-for="n in 3" :key="n">
          <v-container>
            <v-row>
              <v-col v-for="(wait, index) in waitList" :key="index" cols="12" sm="6" md="3">
                <v-card class="mx-auto">
                  <v-img src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg" height="155px"></v-img>
                  <v-card-text>
                    <p class="mb-1 text-area" style="text-align: left;">#{{ wait.keyword1 }} #{{ wait.keyword2 }} #{{ wait.keyword3 }}</p>
                    <p class="text-area" style="font-size: 1rem; color: black; text-align: left;">
                      {{ wait.title }}
                    </p>
                    <v-btn class="m-2" color="orange lighten-2" @click="goDetail(wait.num)"> 상담하기 </v-btn>
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </v-container>
        </v-tab-item>
      </v-tabs>
    </v-card>
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
      page: 3,
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
}
.record-header {
  height: 19%;
}
.text-area {
  text-overflow:ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
</style>
