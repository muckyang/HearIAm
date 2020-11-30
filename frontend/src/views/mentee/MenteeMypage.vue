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
.content-box2{
  width:90%; 
  background-color:white;
  /* border:7px solid#f22fdc; */
  /* border-image: radial-gradient(ellipse at 0% 0%, #f00, #00f); */
  /* border-image: linear-gradient(to right, red 20%, green 20%, green 40%, blue 40%, blue 60%, maroon 60%, maroon 80%, chocolate 80%); */
  /* #f22fdc #a23bbe #49358b */
  border: 10px solid transparent;
  border-radius:20px; 
  /* -moz-border-image: -moz-linear-gradient(top left, #3acfd5 0%, #3a4ed5 100%);
  -webkit-border-image: -webkit-linear-gradient(top left, #3acfd5 0%, #3a4ed5 100%); */
  border-image: linear-gradient(to bottom right, #f22fdc 0%, #49358b 100%);
  border-image-slice: 1;
}

</style>