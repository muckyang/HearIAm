<template>
  <div
    style="
      height: 100%;
      width: 100%;
    "
  >
    <WebRTCComp />
  </div>
</template>

<script>
import WebRTCComp from "@/components/webRTC/CWebRTCComp.vue";

export default {
  name: "CounselorWRTC",
  components: {
    WebRTCComp,
  },
};
</script>

<style>
</style>