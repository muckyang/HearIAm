<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background: linear-gradient(to left, #93dfff, #f5a2bb);
    "
  >
    <CounselingInfoComp />
  </div>
</template>

<script>
import CounselingInfoComp from "@/components/mentor/CounselingInfoComp.vue";
export default {
  name: "CounselingInfo",
  components: {
    CounselingInfoComp,
  },
};
</script>

<style>
</style>