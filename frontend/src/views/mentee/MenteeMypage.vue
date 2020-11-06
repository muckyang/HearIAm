<template>
  <div
    style="
      height: 100%;
      width: 100%;
      padding-top: 64px;
    "
    class="back"
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
.back{
  background-image: url('../../assets/starBack.png');
  background-size: cover;
}
</style>