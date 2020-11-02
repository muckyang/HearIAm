<template>
  <div>
    <div class="my-2">
      <v-btn color="error" fab small dark @click="move()"
        >{{ alert_num }}
      </v-btn>
    </div>
  </div>
</template>
<script>
import http from "@/util/http-common.js";
export default {
  data() {
    return {
      alert_num: 0,
    };
  },
  methods: {
    move() {
      this.$router.push("/alarmList");
    },
    getStart() {
      http.get(`/counseling/alarmListCnt`).then((res) => {
        this.alert_num = res.data;
      });
    },
  },
  mounted: function () {
    this.getStart();
  },
};
</script>