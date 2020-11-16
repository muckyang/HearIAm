<template>
  <div
    style="
      height:100%;
      width:100%;
      padding-top:70px;
      background-color: #0f0d2d;
    "
    align="center"
  >
    <div class="px-5 pt-3 content-box">
      <h1 style="">청소년 녹음 상담 현황</h1>
      <RecordListComp />
      <div style="position:absolute; bottom:2.3rem;">
        <span class="icon-line" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>메인으로</span>
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
