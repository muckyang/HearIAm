<template>
  <div class="record-main d-flex justify-content-center p-5">
    <div
      v-for="(wait, index) in waitList"
      :key="index"
      class="col-12 col-sm-6 col-md-3"
    >
      <v-card class="mx-auto">
        <v-img
          src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
          height="200px"
        ></v-img>

        <v-card-text>
          <p class="mb-1" style="text-align: left">
            #{{ wait.keyword1 }} #{{ wait.keyword2 }} #{{ wait.keyword3 }}
          </p>

          <p
            style="font-size: 1rem; color: black; text-align: left"
          >
            {{ wait.title }}
          </p>
          <v-btn class="m-2" color="orange lighten-2" @click="goDetail(wait.num)"> 상담하기 </v-btn>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common.js";

export default {
  name: "RecordList",
  data() {
    return {
      waitList: [],
    };
  },
  created() {
    this.getWaitList();
  },
  methods: {
    getWaitList() {
      http
        .get("record/getRecord")
        .then((res) => {
          this.waitList = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goDetail(num){
        this.$router.push(`/recordDetail/${num}`);
    }
  },
};
</script>

<style scoped>
.record-main {
  background-image: linear-gradient(to bottom, #93dfff, #e0f6ff);
  height: 100%;
}
.record-header {
  height: 19%;
}
</style>
