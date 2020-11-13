<template>
  <div class="record-main">
    <video hidden id="videoTag" width="720" height="560" style="border: 1px solid black" muted @playing="addEventListener()"></video>

    <div v-if="!flag2 || !recFlag" class="record-header" data-sal="slide-right" data-sal-delay="300" data-sal-duration="600">
      <v-btn class="mx-2" icon color="white" style="position:absolute;left:60px;top:100px;" @click="backBtn()">
        <v-icon large>
          mdi-arrow-left-thick
        </v-icon>
      </v-btn>
    </div>

    <!-- S1 -->
    <div v-if="!flag1" class="record-first-ment" style="height:40%; padding-top:50px;">
      <h1 style="color:white;font-size:6vw;">당신의 고민은 무엇인가요?</h1>
    </div>

    <!-- S2 -->
    <transition name="slide-fade">
      <div v-if="flag2 && !recFlag" class="d-flex record-body pt-10" style="text-align: center">
        <div style="margin: 0 auto">
          <vue-record-audio id="audioRec" :mode="recordMode.audio" @stream="onStream" @result="onResult" />
        </div>
      </div>
    </transition>

    <!-- S3 -->
    <v-row v-if="flag2 && recFlag" class="fill-height" align="center" justify="center" style="padding: 0">
      <template v-for="(itm, i) in sitems">
        <v-col :key="i" cols="6" style="padding: 0">
          <v-hover v-slot="{ hover }">
            <v-expand-transition>
              <v-card :elevation="hover ? 12 : 2" :class="{ 'on-hover': hover }" height="100vh">
                <v-container fill-height>
                  <v-layout align-center justify-center>
                    <v-card-title class="white--text" style="opacity: 1;" @click="clickBtn(i)">
                      <div>
                        <p class="ma-0 text-center" style="font-size: 3.5vw;font-weight:bold;">
                          {{ itm.text }}
                        </p>
                        <v-divider class="my-10 white" style="opacity: 1"></v-divider>
                        <p class="text-center mx-10" style=" font-size: 1em">
                          {{ itm.subtext }}
                        </p>
                      </div>
                    </v-card-title>
                  </v-layout>
                </v-container>
              </v-card>
            </v-expand-transition>
          </v-hover>
        </v-col>
      </template>
    </v-row>

    <!-- Question1 -->
    <div v-if="alertFlag" class="record-alert mx-10" data-sal="slide-right" data-sal-delay="300" data-sal-duration="600">
      <v-alert id="f-alert" elevation="10" prominent dark color="#49358b" class="ma-0">
        <div class="d-flex mb-3">
          <v-icon class="mr-3">mdi-help-circle-outline</v-icon>
          <h3>고민을 선택해주세요.</h3>
        </div>
        <v-col cols="10" lg="6" class="pb-0" align="left">
          <v-select v-model="concern" class="ml-5" :items="items" light label="고민이 무엇인가요?" solo></v-select>
        </v-col>
        <v-col cols="12" lg="7" class="py-0">
          <v-text-field class="py-0 ml-5" v-if="writeFlag" v-model="myConcern" label="고민을 입력해주세요"></v-text-field>
          <div v-else style="height: 50px"></div>
        </v-col>
        <transition name="bounce">
          <v-btn
            v-if="(concern != '' && concern != '직접 입력') || myConcern != ''"
            icon
            style="float: left"
            @click="(flag1 = true), (flag2 = true), (alertFlag = false)"
            ><v-icon large>mdi-arrow-right-bold-outline</v-icon></v-btn
          >
        </transition>
      </v-alert>
    </div>

    <!-- Question2 -->
    <transition name="slide-fade">
      <div v-if="!alertFlag && !recFlag" class="record-alert mx-15">
        <v-alert id="f-alert" elevation="5" prominent dark color="#49358b">
          <div class="d-flex mb-3">
            <v-icon class="mr-3">mdi-information-outline</v-icon>
            <h3>녹음상담 사용방법</h3>
          </div>
          <div class="d-flex ml-8 mt-8">
            <v-img class="d-none d-sm-flex" max-height="50" max-width="50" src="../../assets/rec1.png"></v-img>
            <v-img class="d-flex d-sm-none" max-height="30" max-width="30" src="../../assets/rec1.png"></v-img>
            <h3 class="mt-1 ml-5" style="font-size:2vw;">버튼을 누르면 녹음이 시작됩니다.</h3>
          </div>
          <div class="d-flex ml-8 mt-8">
            <v-img class="d-none d-sm-flex" max-height="50" max-width="50" src="../../assets/rec2.png"></v-img>
            <v-img class="d-flex d-sm-none" max-height="30" max-width="30" src="../../assets/rec2.png"></v-img>
            <h3 class="mt-1 ml-5" style="font-size:2vw;">버튼을 누르면 녹음이 종료됩니다.</h3>
          </div>
        </v-alert>
      </div>
    </transition>

    <!-- progress -->
    <div class="text-center">
      <v-dialog v-model="dialog" hide-overlay persistent width="300">
        <v-card color="#ff7987" dark>
          <v-card-text>
            녹음 파일 분석중...
            <v-progress-linear indeterminate color="white" class="mb-0"></v-progress-linear>
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>

    <!-- 실패 alert -->
    <v-snackbar v-model="errorSnack" top flat color="error" rounded="pill" :timeout="2000">
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
    <!-- 성공 alert -->
    <v-snackbar v-model="succSnack" top flat color="success" rounded="pill" :timeout="2000">
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
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
      items: [
        '시험 성적 때문에 고민이에요.',
        '진로에 대한 고민이 있어요.',
        '괴롭힘을 당하고 있어요.',
        '친구문제로 상담을 받고 싶어요.',
        '기타',
        '직접 입력',
      ],
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
      cnum: '',
      writeFlag: false,
      myConcern: '',
      mentor: 1,
      dialog: false,
      errorSnack: false,
      succSnack: false,
      altMsg: '',
      sitems: [
        {
          text: '전송하기',
          subtext: '녹음 내용이 상담사에게 전송돼요.',
        },
        {
          text: '재녹음하기',
          subtext: '언제든지 다시 녹음 해보세요.',
        },
      ],
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
      this.dialog = true;
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
          this.errorSnack = true;
          this.altMsg = '녹음을 다시 해주세요.';
          this.dialog = false;
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
        if (this.recFlag) {
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
      var tempCon = this.concern;
      if (this.concern == '직접 입력') {
        tempCon = this.myConcern;
      }
      http
        .post('/record/regist', {
          mentor: this.mentor,
          title: tempCon,
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
          this.dialog = false;
          console.log(err);
        });
    },
    emotionUpload() {
      http
        .post('/record/emotion', {
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
          this.dialog = false;
          this.succSnack = true;
          this.altMsg = '전송되었습니다.';
          setTimeout(() => {
            this.$router.push('/menteeMypage');
          }, 2000);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    clickBtn(idx) {
      if (idx == 0) {
        this.fileUpload();
      } else if (idx == 1) {
        this.reRecord();
      }
    },
    backBtn() {
      this.$router.push('/MenteeMain');
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
      if (v == '직접 입력') {
        this.writeFlag = true;
      } else {
        this.writeFlag = false;
      }
    },
  },
  beforeDestroy() {
    this.stopVideo();
  },
};
</script>

<style scoped>
.record-main {
  /* background-color: #49358b; */
  /* background-image: linear-gradient(to bottom, #49358b, white) !important; */
  background-image: url('../../assets/recordMain.png');
  background-size: cover;
  height: 100vh;
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
.record-first-ment {
  -webkit-animation: focus-in-expand 2.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: focus-in-expand 2.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
@-webkit-keyframes focus-in-expand {
  0% {
    letter-spacing: -0.5em;
    -webkit-filter: blur(12px);
    filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-filter: blur(0px);
    filter: blur(0px);
    opacity: 1;
  }
}
@keyframes focus-in-expand {
  0% {
    letter-spacing: -0.5em;
    -webkit-filter: blur(12px);
    filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-filter: blur(0px);
    filter: blur(0px);
    opacity: 1;
  }
}
.v-card {
  transition: 0.4s ease-in;
  background-color: rgb(14, 1, 27, 0.2);
}

.v-card:not(.on-hover) {
  background-color: rgb(14, 1, 27, 0.8);
}
.title {
  cursor: pointer;
}
</style>
