<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background: linear-gradient(to right, #93dfff, #f5a2bb);
    "
  >
  <MyListComp />
  </div>
</template>

<script>
import MyListComp from "@/components/mentee/MyListComp.vue";

export default {
    name : "MenteeMypage",
    components:{
        MyListComp
    }
};
</script>

<style>
</style>