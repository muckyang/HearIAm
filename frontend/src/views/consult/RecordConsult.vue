<template>
  <div class="record-main">
    
    <video hidden id="videoTag" width="720" height="560" style="border:1px solid black" muted @playing="addEventListener()"></video>

    <!-- S1 -->
    <div class="record-header"></div>
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
          <v-img
            class="mt-3"
            style="margin-right:60px;"
            id="mic-image"
            max-height="120"
            max-width="120"
            src="../../assets/submit.png"
            @click="fileUpload()"
          ></v-img>
          <v-img
            id="mic-image"
            style="margin-left:60px;"
            max-height="120"
            max-width="120"
            src="../../assets/replay.png"
            @click="reRecord()"
          ></v-img>
        </div>
      </div>
    </transition>

    <!-- Question1 -->
    <div v-if="alertFlag" class="record-alert mx-15" data-sal="slide-right" data-sal-delay="300" data-sal-duration="600">
      <v-alert id="f-alert" elevation="5" prominent dark color="#f5a2bb">
        <div class="d-flex mb-3">
          <v-icon class="mr-3">mdi-help-circle-outline</v-icon>
          <h2>당신의 고민은 무엇인가요??</h2>
        </div>
        <v-col cols="12" sm="7" class="pb-0">
          <v-select v-model="concern" class="ml-5" :items="items" light label="고민이 무엇인가요?" solo></v-select>
        </v-col>
        <v-col cols="7" class="py-0">
          <v-text-field class="py-0 ml-5" v-if="writeFlag" v-model="myConcern" label="고민을 입력해주세요"></v-text-field>
          <div v-else style="height:50px;"></div>
        </v-col>
        <transition name="bounce">
          <v-btn v-if="concern != '' && concern != '직접 입력' || myConcern != ''" icon style="float:right;" @click="(flag1 = true), (flag2 = true), (alertFlag = false)"
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
import http3 from '@/util/http-common3.js';
import * as faceapi from 'face-api.js';
import { mapGetters, mapState } from 'vuex';

export default {
  name: 'Record',
  mounted() {
    sal();
    this.videoTag = document.getElementById('videoTag');
    Promise.all([
      faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
      faceapi.nets.faceLandmark68Net.loadFromUri('/models'),
      faceapi.nets.faceRecognitionNet.loadFromUri('/models'),
      faceapi.nets.faceExpressionNet.loadFromUri('/models'),
    ]);
  },
  created() {
    this.mentor = this.$route.params.num;
  },
  data() {
    return {
      flag1: false,
      flag2: false,
      flag3: false,
      alertFlag: true,
      concern: '',
      items: ['시험 성적 때문에 고민이에요..', '진로에 대한 고민이 있어요.', '괴롭힘을 당하고 있어요...', '친구문제로 상담을 받고 싶어요.', '기타', '직접 입력'],
      recordMode: {
        audio: 'press',
      },
      recordings: [],
      file: '',
      recFlag: false,
      log: '',
      videoTag: [],
      emotion: [0, 0, 0, 0, 0, 0, 0],
      allEmotion: {
        angry: '',
        disgusted: '',
        fearful: '',
        happy: '',
        neutral: '',
        sad: '',
        surprised: '',
      },
      recordInfo: [],
      cnum: "",
      writeFlag: false,
      myConcern: "",
      mentor: 1,
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
      this.startVideo();
    },
    onResult(data) {
      this.log = data;
      this.recFlag = true;
      this.stopVideo();
      // this.file = this.blobToFile(data, "my-record.wav");
      data.lastModifiedDate = new Date();
      this.file = new File([data], 'record.wav');
      this.recordings.push({
        src: window.URL.createObjectURL(data),
      });
    },
    fileUpload() {
      var formData = new FormData();
      formData.append('file', this.file);
      http3
        .post('/record/test', formData)
        .then((res) => {
          this.log = res;
          this.recordInfo = res.data;
          this.recordUpload();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    reRecord() {
      this.recFlag = false;
      this.flag2 = true;
      this.recordings = [];
      this.file = '';
    },
    addEventListener() {
      setInterval(async () => {
        const detections = await faceapi
          .detectAllFaces(this.videoTag, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceExpressions();

        if (detections.length > 0) {
          this.emotion = [
            Math.floor(detections[0].expressions.angry * 100),
            Math.floor(detections[0].expressions.disgusted * 100),
            Math.floor(detections[0].expressions.fearful * 100),
            Math.floor(detections[0].expressions.happy * 100),
            Math.floor(detections[0].expressions.neutral * 100),
            Math.floor(detections[0].expressions.sad * 100),
            Math.floor(detections[0].expressions.surprised * 100),
          ];
          this.allEmotion.angry = this.allEmotion.angry.concat(this.emotion[0], `|`);
          this.allEmotion.disgusted = this.allEmotion.disgusted.concat(this.emotion[1], `|`);
          this.allEmotion.fearful = this.allEmotion.fearful.concat(this.emotion[2], `|`);
          this.allEmotion.happy = this.allEmotion.happy.concat(this.emotion[3], `|`);
          this.allEmotion.neutral = this.allEmotion.neutral.concat(this.emotion[4], `|`);
          this.allEmotion.sad = this.allEmotion.sad.concat(this.emotion[5], `|`);
          this.allEmotion.surprised = this.allEmotion.surprised.concat(this.emotion[6], `|`);
        }
        if(this.recFlag) {
          clearInterval();
          return;
        }
      }, 1000);
    },
    startVideo() {
      navigator.mediaDevices.getUserMedia({ video: true }).then((stream) => {
        const videoPlayer = document.getElementById('videoTag');
        videoPlayer.srcObject = stream;
        videoPlayer.play();
      });
    },
    stopVideo() {
      if (this.videoTag.srcObject != null && this.videoTag.srcObject != '') {
        const tracks = this.videoTag.srcObject.getTracks();
        tracks.forEach(function(track) {
          track.stop();
        });
        this.videoTag.srcObject = null;
      }
    },
    recordUpload() {
      http
        .post('/record/regist', {
          mentor: this.mentor,
          title: this.concern,
          recordDir: this.recordInfo[0],
          wordcloudImg: this.recordInfo[1],
          keyword1: this.recordInfo[2],
          keyword2: this.recordInfo[3],
          keyword3: this.recordInfo[4],
          mentee: this.getUserNum,
        })
        .then((res) => {
          this.cnum = res.data;
          this.emotionUpload();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    emotionUpload() {
      http
        .post("/record/emotion", {
          num: this.cnum,
          angry: this.allEmotion.angry,
          disgusted: this.allEmotion.disgusted,
          fearful: this.allEmotion.fearful,
          happy: this.allEmotion.happy,
          neutral: this.allEmotion.neutral,
          sad: this.allEmotion.sad,
          surprised: this.allEmotion.surprised,
        })
        .then((res) => {
          this.log = res;
          setTimeout(() => {
            this.$router.push('/menteeMain');
          }, 1500);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  computed: {
    ...mapGetters(['getUserNum']),
    ...mapState({
      userNum: (state) => `${state.user.getUserNum}`,
    }),
  },
  watch: {
    concern(v) {
      if(v == "직접 입력") {
        this.writeFlag = true;
      } else {
        this.writeFlag = false;
      }
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
  height: 45%;
  margin: 0 auto;
}
.record-alert {
  height: 35%;
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
