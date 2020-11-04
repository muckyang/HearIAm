<template>
  <div
    style="
      height: 100%;
      width: 100%;
    "
  >
    <v-container>
      <v-col>
        <MyListComp />
      </v-col>
    </v-container>
  </div>
</template>

<script>
import MyListComp from "@/components/mentee/MyListComp.vue";

export default {
  name: "MenteeMypage",
  components: {
    MyListComp,
  },
};
</script>

<style>
</style>