<template>
  <div
    style="
      width: 100%;
      background-color: #0f0d2d;
      padding-top: 70px;
      padding-bottom:30px;
    "
    align="center"
  >
  <div class="px-5 pt-3 content-box">
    <v-col>
    <MenteeInfoComp />
    </v-col>
  </div>
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