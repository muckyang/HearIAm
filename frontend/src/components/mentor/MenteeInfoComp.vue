<template>
  <div>
    <h1 class="text-center">{{ name }}의 상담 내역</h1>
    <br />
    <v-card>
      <v-tabs v-model="tab" background-color="deep-purple accent-4" center-active dark>
        <v-tab v-for="(item, index) in myMenteeInfoList" :key="index">{{ setDate(item.date) }}</v-tab>
      </v-tabs>
      <v-tabs-items v-model="tab">
        <v-tab-item v-for="(item, idx) in myMenteeInfoList" :key="idx">
          <CounselingInfoComp :numIndex="item.num" />
        </v-tab-item>
      </v-tabs-items>
    </v-card>
  </div>
</template>

<script>
import http from '@/util/http-common.js';
import { mapGetters } from 'vuex';
import CounselingInfoComp from '@/components/mentor/CounselingInfoComp.vue';

export default {
  name: 'MenteeInfoListComp',
  components: {
    CounselingInfoComp,
  },
  data() {
    return {
      myMenteeInfoList: [],
      name: '',
      tab: null,
    };
  },
  created() {
    http.get(`/counseling/myMenteeInfoList/${this.getUserNum}/${this.$route.params.num}`).then((res) => {
      this.myMenteeInfoList = res.data;
      console.log(this.myMenteeInfoList);
    });
    this.name = this.$route.params.name;
  },
  computed: {
    ...mapGetters(['isProfileLoaded', 'getRole', 'getQualification', 'getUserName', 'getUserNum', 'getUserID']),
  },
  methods: {
    setDate(date) {
      let time = Number(date.slice(2, 4)) + '.' + Number(date.slice(5, 7)) + '.' + Number(date.slice(8, 10));
      return time;
    },
  },
};
</script>

<style></style>
