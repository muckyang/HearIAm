<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background: linear-gradient(to left, #93dfff, #f5a2bb);
    "
  >
    <MenteeInfoComp />
  </div>
</template>

<script>
import MenteeInfoComp from "@/components/mentor/MenteeInfoComp.vue";

export default {
  name: "MyMenteeInfo",
  components: {
    MenteeInfoComp,
  },
};
</script>

<style>
</style>