<template>
  <div>
    <!-- <v-card class="py-3 px-3" id="report-card" style="border:none;"> -->
      <v-card-title width="500">
        <!-- 제목 -->
        <v-icon large color="#262272" class="mr-3"
          >mdi-subtitles-outline</v-icon
        >
        <h2>
          <v-text-field
            style="width: 300px"
            name="name"
            v-model="counseling.title"
          ></v-text-field>
        </h2>
      </v-card-title>

      <v-divider class="mx-4"></v-divider>

      <v-card-text>
        <v-row cols="12">
          <!-- 감정 그래프 -->
          <v-col cols="7">
            <div style="width: 28vw; height: 28vw; margin-left: 5%">
              <Doughnut :chartData="chartData" />
            </div>
          </v-col>
          <!-- 상담일, 키워드 -->
          <v-col cols="5" class="pl-15 pt-5" style="text-align: left">
            <v-card-title class="px-0" style="color:black;font-weight:bold;">
              <v-icon large color="#262272" class="mr-2"
                >mdi-calendar-range</v-icon
              >
              상담일자
            </v-card-title>
            <h2 class="mb-10">{{ date }}</h2>

            <v-card-title class="px-0" style="color:black;font-weight:bold;">
              <v-icon large color="#262272" class="mr-2"
                >mdi-tag-heart-outline</v-icon
              >
              키워드
            </v-card-title>

            <div class="d-flex">
              <h2 class="mt-6 mr-3">#</h2>
              <h2 class="mb-4">
                <v-text-field
                  name="name"
                  v-model="counseling.keyword1"
                ></v-text-field>
              </h2>
            </div>
            <div class="d-flex">
              <h2 class="mt-6 mr-3">#</h2>
              <h2 class="mb-4">
                <v-text-field
                  name="name"
                  v-model="counseling.keyword2"
                ></v-text-field>
              </h2>
            </div>
            <div class="d-flex">
              <h2 class="mt-6 mr-3">#</h2>
              <h2 class="mb-4">
                <v-text-field
                  name="name"
                  v-model="counseling.keyword3"
                ></v-text-field>
              </h2>
            </div>
          </v-col>
        </v-row>
      </v-card-text>

      <v-divider class="mx-4"></v-divider>

      <v-card-title style="font-weight:bold;">
        <v-icon large color="#262272" class="mr-2"
          >mdi-book-open-outline</v-icon
        >
        상담기록
      </v-card-title>
      <v-card-text class="pb-0">
        <v-textarea outlined no-resize v-model="counseling.memo"></v-textarea>
      </v-card-text>
      <v-card-title class="pt-0" style="font-weight:bold;">
        <v-icon large color="#262272" class="mr-1"
          >mdi-pencil-plus-outline</v-icon
        >
        상담결과
      </v-card-title>
      <v-card-text class="pb-0">
        <v-textarea
          outlined
          no-resize
          v-model="counseling.answer"
          style="white-space: pre-line"
        ></v-textarea>
      </v-card-text>
      <v-card-actions>
        <v-btn
          style="color: white; margin: 0 auto"
          class="mb-3 main-btn"
          @click="reportSave()"
        >
          저장하기
        </v-btn>
      </v-card-actions>
    <!-- </v-card> -->
    <v-snackbar v-model="successSnack" top flat color="success" rounded="pill" :timeout="2000">
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
  </div>
</template>

<script>
import Doughnut from "@/components/mentor/Doughnut.js";
import http from "@/util/http-common.js";

export default {
  name: "CounselingInfoComp",
  components: {
    Doughnut,
  },
  props: {
    numIndex: { type: Number },
  },
  data() {
    return {
      emotion: {},
      angry: 0,
      disgusted: 0,
      fearful: 0,
      happy: 0,
      neutral: 0,
      sad: 0,
      surprised: 0,
      chartData: null,
      counseling: {},
      date: null,
      nickname: "",
      successSnack:false,
      altMsg:'',

    };
  },
  created() {
    http.get(`/counseling/counseling/${this.numIndex}`).then((res) => {
      this.counseling = res.data;
      this.date = this.setTime(this.counseling.date);
      this.getMenteeName(this.counseling.mentee);
    });
    http.get(`/counseling/loadEmotion/${this.numIndex}`).then((res) => {
      this.emotion = res.data;
      if (this.emotion != "") {
        this.angry = this.avrage(this.emotion.angry.split(`|`).map(Number));
        this.disgusted = this.avrage(
          this.emotion.disgusted.split(`|`).map(Number)
        );
        this.fearful = this.avrage(this.emotion.fearful.split(`|`).map(Number));
        this.happy = this.avrage(this.emotion.happy.split(`|`).map(Number));
        this.neutral = this.avrage(this.emotion.neutral.split(`|`).map(Number));
        this.sad = this.avrage(this.emotion.sad.split(`|`).map(Number));
        this.surprised = this.avrage(
          this.emotion.surprised.split(`|`).map(Number)
        );
        this.fillData();
      }
    });
  },
  methods: {
    setTime(date) {
      let time =
        Number(date.slice(0, 4)) +
        "년 " +
        Number(date.slice(5, 7)) +
        "월 " +
        Number(date.slice(8, 10)) +
        "일 " +
        Number(date.slice(11, 13)) +
        "시 " +
        Number(date.slice(14, 16)) +
        "분";
      return time;
    },
    avrage(arr) {
      let result = 0;
      for (let i = 0; i < arr.length; i++) {
        result += arr[i];
      }
      return result / arr.length;
    },
    fillData() {
      this.chartData = {
        labels: [
          "화남",
          "역겨움",
          "두려움",
          "행복",
          "무표정",
          "슬픔",
          "놀람",
        ],
        datasets: [
          {
            borderColor: [
              "#031926",
              "#468189",
              "#77ACA2",
              "#9DBEBB",
              "#F4E9CD",
              "#E9D758",
              "#FF8552",
            ],
            backgroundColor: [
              "#031926",
              "#468189",
              "#77ACA2",
              "#9DBEBB",
              "#F4E9CD",
              "#E9D758",
              "#FF8552",
            ],
            data: [
              this.angry,
              this.disgusted,
              this.fearful,
              this.happy,
              this.neutral,
              this.sad,
              this.surprised,
            ],
          },
        ],
      };
    },
    getMenteeName(mentee) {
      http
        .get(`/user/menteeName/${mentee}`)
        .then((res) => {
          this.nickname = res.data;
          this.counseling.title = this.nickname + "님과의 상담내용";
        })
        .catch((err) => {
          console.log(err);
        });
    },
    reportSave() {
      http
        .put(`/counseling/update`, this.counseling)
        .then((res) => {
          console.log(res.data);
          setTimeout(() => {
            this.altMsg = "저장되었습니다.";
            this.successSnack = true;
            // this.$router.push("/myMenteeList");
          }, 1000);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style>
#jb-container {
  width: 940px;
  margin: 10px auto;
  padding: 20px;
  border: 1px solid #bcbcbc;
}
#jb-header {
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #bcbcbc;
}
#jb-content {
  width: 580px;
  padding: 20px;
  margin-bottom: 20px;
  float: left;
  border: 1px solid #bcbcbc;
}
#jb-sidebar {
  width: 260px;
  padding: 20px;
  margin-bottom: 20px;
  float: right;
  border: 1px solid #bcbcbc;
}
#jb-footer {
  clear: both;
  padding: 20px;
  border: 1px solid #bcbcbc;
}
#report-card {
  height: auto;
  width: 100%;
}
</style>
