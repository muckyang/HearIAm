<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background: linear-gradient(to left, #93dfff, #f5a2bb);
      padding-top: 64px;
    "
  >
  <v-container>
    <v-col>
    <MenteeInfoComp />
    </v-col>
  </v-container>
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