<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background: linear-gradient(to left, #93dfff, #f5a2bb);
    "
  >
    <v-card class="mx-auto" max-width="300" tile>
      <v-list dense>
        <v-subheader>LiveList</v-subheader>
        <v-list-item-group color="primary">
          <v-list-item v-for="(item, index) in liveList" :key="index">
            <LiveListComp
              :num="item.num"
              :mentor="item.mentor"
              :mentee="item.mentee"
              :room="item.room"
              :date="item.date"
              :status="item.status"
            />
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-card>

    <v-snackbar
      v-model="errorSnack"
      top
      flat
      color="error"
      rounded="pill"
      :timeout="2000"
    >
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
  </div>
</template>

<script>
import LiveListComp from "@/components/webRTC/LiveListComp.vue";
import http from "@/util/http-common.js";

export default {
  name: "LiveList",
  components: {
    LiveListComp,
  },
  data() {
    return {
      liveList: [],
      errorSnack: false,
      altMsg: "",
    };
  },
  created() {
    http.get(`/counseling/liveList`).then((res) => {
      if (res.data != null) {
        this.liveList = res.data;
      } else {
        this.errorSnack = true;
        this.altMsg = "대기중인 상담 요청이 없습니다.";
      }
    });
  },
};
</script>

<style>
</style>