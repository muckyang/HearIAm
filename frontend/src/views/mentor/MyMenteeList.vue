<template>
  <div
    style="
      height: 100%;
      width: 100%;
      background-color: #0f0d2d;
      padding-top: 64px;
    "
    align="center"
  >
    <div class="px-5 pt-3 content-box">
      <h1>나의 학생 일지 관리</h1>
      <v-divider class="mt-3 mb-3"></v-divider>
      <v-row>
        <v-col
          cols="12"
          sm="6"
          md="2"
          v-for="item in menteeList"
          :key="item.num"
        >
          <v-card
            :color="item.color"
            @click="menteeInfo(item.num, item.name)"
            height="200"
            style="overflow:hidden;"
          >
            <div class="one-box" align="center" style="height:100%;width:100%;">
              <div style="width: 80%; height: 75%">
              <v-img
                :src="getIcon(item.icon)"
                
              ></v-img>
              </div>
              <!-- <v-img :src="`../../assets/icons/${item.icon}`" ></v-img> -->
              <!-- <v-img src="../../assets/icons/bear.png" ></v-img> -->
              <div style="height:25%;display:table;">
              <p class="my-0" style="font-size:1.2rem;font-weight:bold;display:table-cell;vertical-align:middle;">
                {{ item.name }}
              </p>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<script>
import http from "@/util/http-common.js";
import { mapGetters } from "vuex";

export default {
  name: "MyMenteeList",
  data() {
    return {
      menteeList: [],
    };
  },
  mounted() {
    http.get(`/counseling/myMenteeList/${this.getUserNum}`).then((res) => {
      this.menteeList = res.data;
    });
  },
  computed: {
    ...mapGetters([
      "isProfileLoaded",
      "getRole",
      "getQualification",
      "getUserName",
      "getUserNum",
      "getUserID",
    ]),
  },
  methods: {
    menteeInfo(num, name) {
      this.$router.push(`/myMenteeInfo/${num}&${name}`);
    },
    getIcon(icon) {
      console.log(icon);
      return "../../../icons/" + icon;
    },
  },
};
</script>

<style scoped>
.one-box{
  transform: scale(1);
  transition: all 0.3s;
}
.one-box:hover{
  transform: scale(1.1);
  transition: all 0.3s;
}
</style>
