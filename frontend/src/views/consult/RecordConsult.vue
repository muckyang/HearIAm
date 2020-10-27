<template>
  <div class="record-main">
    <!-- S1 -->
    <div class="record-header"></div>
    <!-- 물음표 이미지 -->
    <div v-if="!flag1" class="record-body pt-10" data-sal="slide-down" data-sal-delay="900" data-sal-duration="600">
      <v-img max-height="300" max-width="200" style="margin:0 auto" src="../../assets/unicorn.png"></v-img>
    </div>

    <!-- S2 -->
    <transition name="slide-fade">
      <div v-if="flag2 && !recFlag" class="d-flex record-body pt-10" style="text-align: center;">
        <div style="margin:0 auto;">
          <vue-record-audio id="audioRec" :mode="recordMode.audio" @stream="onStream" @result="onResult" />
        </div>
      </div>
    </transition>

    <!-- S3 -->
    <transition name="slide-fade">
      <div v-if="flag2 && recFlag" class="d-flex record-body pt-10">
        <div style="margin: 0 auto 0 auto;" class="d-flex">
          <v-img class="mt-3" style="margin-right:60px;" id="mic-image"  max-height="120" max-width="120" src="../../assets/submit.png" @click="fileUpload()"></v-img>
          <v-img id="mic-image" style="margin-left:60px;" max-height="120" max-width="120" src="../../assets/replay.png" @click="reRecord()"></v-img>
        </div>
      </div>
    </transition>

    <!-- 마이크 이미지 -->
    <!-- <transition name="slide-fade">
      <div v-if="flag2" class="record-body pt-10">
        <v-img
          @click="(flag2 = false), (flag3 = true), start()"
          id="mic-image"
          max-height="300"
          max-width="200"
          style="margin:0 auto"
          src="../../assets/mic.png"
        >
        </v-img>
      </div>
    </transition> -->

    <!-- 녹음중 이미지 -->
    <!-- <transition name="slide-fade">
      <div v-if="flag3" class="record-body pt-10">
        <v-img
          @click="(flag3 = false), (flag2 = true)"
          id="mic-image"
          max-height="300"
          max-width="200"
          style="margin:0 auto"
          src="../../assets/micOn.gif"
        ></v-img>
        <v-img
          v-if="flag3"
          @click="(flag3 = false), (flag2 = true)"
          id="mic-image"
          max-height="100"
          max-width="100"
          style="margin:0 auto"
          src="../../assets/record.png"
        ></v-img>
      </div>
    </transition> -->

    <!-- Question1 -->
    <div v-if="alertFlag" class="record-alert mx-15" data-sal="slide-right" data-sal-delay="300" data-sal-duration="600">
      <v-alert id="f-alert" elevation="5" prominent dark color="#f5a2bb">
        <div class="d-flex mb-3">
          <v-icon class="mr-3">mdi-help-circle-outline</v-icon>
          <h2>당신의 고민은 무엇인가요??</h2>
        </div>
        <v-col class="d-flex" cols="12" sm="6">
          <v-select v-model="concern" class="ml-5" :items="items" light label="고민이 무엇인가요?" solo></v-select>
        </v-col>
        <transition name="bounce">
          <v-btn v-if="concern != ''" icon style="float:right;" @click="(flag1 = true), (flag2 = true), (alertFlag = false)"
            ><v-icon large>mdi-arrow-right-bold-outline</v-icon></v-btn
          >
        </transition>
      </v-alert>
    </div>

    <!-- Question2 -->
    <transition name="slide-fade">
      <div v-if="!alertFlag && !recFlag" class="record-alert mx-15">
        <v-alert id="f-alert" elevation="5" prominent dark color="#f5a2bb">
          <div class="d-flex mb-3">
            <v-icon class="mr-3">mdi-information-outline</v-icon>
            <h2>녹음상담 사용방법</h2>
          </div>
          <div class="d-flex ml-8 mt-5">
            <v-img max-height="50" max-width="50" src="../../assets/rec1.png"></v-img>
            <h3 class="mt-2 ml-5">버튼을 누르면 녹음이 시작됩니다.</h3>
          </div>
          <div class="d-flex ml-8 mt-5">
            <v-img max-height="50" max-width="50" src="../../assets/rec2.png"></v-img>
            <h3 class="mt-2 ml-5">버튼을 누르면 녹음이 종료됩니다.</h3>
          </div>
        </v-alert>
      </div>
    </transition>

    <!-- Question3 -->
    <transition name="slide-fade">
      <div v-if="flag2 && recFlag" class="record-alert mx-15">
        {{recordings}}
        {{file}}
        <v-alert id="f-alert" elevation="5" prominent dark color="#f5a2bb">
          <div class="d-flex mb-3">
            <v-icon class="mr-3">mdi-antenna</v-icon>
            <h2>녹음을 완료했습니다.</h2>
          </div>
          <div class="d-flex ml-8 mt-5">
            <v-img max-height="45" max-width="45" src="../../assets/submit.png"></v-img>
            <h3 class="mt-2 ml-5">버튼을 누르면 제출이 완료됩니다.</h3>
          </div>
          <div class="d-flex ml-8 mt-5">
            <v-img max-height="45" max-width="45" src="../../assets/replay.png"></v-img>
            <h3 class="mt-2 ml-5">버튼을 누르면 재녹음 할 수 있습니다.</h3>
          </div>
        </v-alert>
      </div>
    </transition>
  </div>
</template>

<script>
import sal from 'sal.js';
import http from '@/util/http-common.js';

export default {
  name: 'Record',
  mounted() {
    sal();
  },
  data() {
    return {
      flag1: false,
      flag2: false,
      flag3: false,
      alertFlag: true,
      concern: '',
      items: ['시험 성적 때문에 고민이에요..', '진로에 대한 고민이 있어요.', '괴롭힘을 당하고 있어요...', '친구문제로 상담을 받고 싶어요.'],
      recordMode: {
        audio: 'press',
      },
      recordings: [],
      file: '',
      recFlag: false,
      log: "",
    };
  },
  methods: {
    callback(data) {
      this.log = data;
    },
    removeRecord(index) {
      this.recordings.splice(index, 1);
    },
    onStream(stream) {
      this.log = stream;
    },
    onResult(data) {
      this.log = data
      this.recFlag = true;
      // this.file = this.blobToFile(data, "my-record.wav");
      data.lastModifiedDate = new Date();
      this.file = new File([data], 'record.wav');
      this.recordings.push({
        src: window.URL.createObjectURL(data),
      });
    },
    fileUpload() {
      console.log('upload');
      console.log(this.file);
      var formData = new FormData();
      formData.append('file', this.file);
      http
        .post(`/record/test`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then(res => {
          setTimeout(() => {
            this.$router.push("/menteeMain");
          }, 1500);
          this.log = res;
        })
        .catch(err => {
          console.log(err);
        });
    },
    reRecord() {
      this.recFlag = false;
      this.flag2 = true;
      this.recordings = [];
      this.file = '';
    },
  },
};
</script>

<style scoped>
.record-main {
  background-image: linear-gradient(to bottom, #93dfff, white);
  height: 100%;
}
.record-header {
  height: 19%;
}
.record-body {
  height: 49%;
  margin: 0 auto;
}
.record-alert {
  height: 29%;
  bottom: 0px;
}
#mic-image :hover {
  cursor: pointer;
}
#f-alert {
  bottom: 20px;
  height: 100%;
  padding-left: 30px;
  padding-right: 30px;
}
.bounce-enter-active {
  animation: bounce-in 0.8s;
}
.bounce-leave-active {
  animation: bounce-in 0.8s reverse;
}
@keyframes bounce-in {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.5);
  }
  100% {
    transform: scale(1);
  }
}
.slide-fade-enter-active {
  transition: all 1s ease;
}
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active below version 2.1.8 */ {
  transform: translateX(-40px);
  opacity: 0;
}
</style>
