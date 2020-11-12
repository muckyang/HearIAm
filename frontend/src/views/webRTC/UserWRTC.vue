<template>
  <div
    id="student-con"
    style="
        height: 100%;
        padding-top: 70px;
      "
  >
    <WebRTCComp />
  </div>
</template>

<script>
import WebRTCComp from '@/components/webRTC/UWebRTCComp.vue';
export default {
  name: 'UserWRTC',
  components: {
    WebRTCComp,
  },
};
</script>

<style scoped>
#student-con {
  background-image: url('../../assets/recordMain.png');
  background-size: cover;
}
</style>
