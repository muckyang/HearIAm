<template>
  <div
      style="
        height: 100%;
        width: 100%;
        background: linear-gradient(to right, #93dfff, #f5a2bb);
      "
    >
    <WebRTCComp />
  </div>
</template>

<script>
import WebRTCComp from "@/components/webRTC/UWebRTCComp.vue";
export default {
  name: "UserWRTC",
  components: {
    WebRTCComp,
  },
};
</script>

<style>
</style>