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

<style scoped>
.content-box2{
  width:90%;
  height:98%;
  background: -moz-linear-gradient(top left, #ff7987, #a23bbe);
  background: -webkit-linear-gradient(top left, #ff7987, #a23bbe);
  -moz-background-origin: border;
  background-origin: border-box;
  border: 10px solid transparent;
  border-radius: 25px;
  box-shadow:
    0 0 12px white, /* Outset shadow */
    inset 0 0 0 white; /* The background color */
  }
</style>