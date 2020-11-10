<template>
  <div
    style="
      height: 100%;
      width: 100%;
      padding-top: 70px;
    "
    class="back"
    align="center"
  >
    <!-- <v-container> -->
      <div class="px-5 pt-5 content-box">
        <MyListComp />
        <div style="position:absolute; bottom:2.3rem;"><span class="icon-line" @click="goBack()"><v-icon style="color:crimson;">mdi-arrow-left-thick</v-icon>뒤로</span></div>
      </div>
    <!-- </v-container> -->
  </div>
</template>

<script>
import MyListComp from "@/components/mentee/MyListComp.vue";

export default {
  name: "MenteeMypage",
  components: {
    MyListComp,
  },
  methods:{
    goBack(){
      window.history.back();
    }
  }
};
</script>

<style>
.back{
  /* background-image: url('../../assets/starBack.png'); */
  background-color:#0f0d2d;
}
.content-box{
  width:90%;
  height:98%;
  background: -moz-linear-gradient(top left, #ff7987, #a23bbe);
  background: -webkit-linear-gradient(top left, #ff7987, #a23bbe);
  -moz-background-origin: border;
  background-origin: border-box;
  border: 10px solid transparent;
  border-radius: 25px;
  box-shadow:
    inset 0 0 12px white, /* Inset shadow */
    0 0 12px white, /* Outset shadow */
    inset -1500px 0 0 white; /* The background color */
}

.icon-line{
  display:inline-flex;
  vertical-align:middle;
  color:crimson;
  cursor:pointer;
}
</style>