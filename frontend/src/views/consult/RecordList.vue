<template>
  <div
    style="
      height:100%;
      width:100%;
      padding-top:70px;
    "
    class="back"
    align="center"
  >
    <div class="content-box">
      <RecordListComp />
      <div style="position:absolute; bottom:2.3rem;">
        <span class="icon-line ml-2" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span>
      </div>
    </div>
  </div>
</template>

<script>
import RecordListComp from "@/components/mentor/RecordListComp.vue";

export default {
  components: {
    RecordListComp,
  },
  methods:{
   goBack(){
      this.$router.push("/")
    },
  },
};
</script>

<style></style>
