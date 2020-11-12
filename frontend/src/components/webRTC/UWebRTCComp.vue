<template>
  <div>
    <!-- 설명서 -->
    <v-dialog v-model="dialogRTC" width="75%"  >
      <div style="width:100%; height:100%;border:3px solid white; border-radius:10px;">
      <div style="margin:50px 50px; color:white">
        <div class="d-flex mb-7">
          <v-img max-height="100" max-width="100" src="../../assets/face2.png"></v-img>
          <h2 class="mt-7 ml-7 step-text">HEAR I AM 은 얼굴이 공개되지 않는 서비스로 익명성이 보장됩니다.</h2>
        </div>
        <div class="d-flex mb-7">
          <v-img max-height="100" max-width="100" src="../../assets/face1.png"></v-img>
          <h2 class="mt-7 ml-7 step-text">카메라는 상담사에게 표정을 보여줄 용도이므로 안심하셔도 됩니다.</h2>
        </div>
        <div class="d-flex mb-7">
          <v-img max-height="100" max-width="100" src="../../assets/face3.png"></v-img>
          <h2 class="mt-7 ml-7 step-text">시작 버튼을 누르면 상담사가 매칭됩니다.</h2>
        </div>
        <div v-if="!dialog">
          <v-btn class="heartbeat mt-3" dark x-large style="font-size:1.2rem;border-radius:30px;" color="#ff7987 " @click="dialog = true, dialogRTC2 = true, createRoom()">상담시작</v-btn>
        </div>
      </div>
      </div>
    </v-dialog>
    
    <!-- RTC -->
    <v-overlay :value="dialogRTC2">
        <div style="padding-top:40px">
          <v-col>
            <h1 style="font-size:3vw;color:white;" class="mb-7" v-if="mentorName">{{ mentorName }} 상담사와 상담중입니다.</h1>
            <WebRTC ref="webrtc" cameraHeight="400" :roomId="roomId" @error="onError"  class="mb-7" @isLeave="finishLive()"/>
          </v-col>
        </div>
        <div>
          <v-btn v-if="endBtn" type="button" class="btn btn-primary" @click="onLeave">
            상담 종료
          </v-btn>
        </div>
        <div v-if="dialog">
          <h2 class="mb-5" style="color:white;">상담사와 연결중입니다.</h2>
          <v-progress-circular :size="100" :width="5" color="white" indeterminate>
            <v-btn text @click="dialogCancel()" style="color:white;">취소</v-btn>
          </v-progress-circular>
        </div>
    </v-overlay>

    <video id="videoTag" muted hidden @playing="addEventListener()"></video>

    <v-snackbar v-model="successSnack" top flat color="success" rounded="pill" :timeout="2000">
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
    <v-snackbar v-model="errorSnack" top flat color="error" rounded="pill" :timeout="2000">
      <span class="snackText">
        {{ altMsg }}
      </span>
    </v-snackbar>
  </div>
</template>

<script>
import WebRTC from '@/components/webRTC/Uwebrtc.vue';
import * as io from 'socket.io-client';
import * as faceapi from 'face-api.js';
window.io = io;
import { mapGetters } from 'vuex';
import http from '@/util/http-common.js';
import axios from 'axios';
export default {
  name: 'UWebRTCComp',
  components: {
    WebRTC,
  },
  data() {
    return {
      e1: 1,
      img: null,
      roomId: null,
      message: null,
      videoTag: [],
      isProgress: false,
      url: 'https://fcm.googleapis.com/fcm/send',
      devecieId: this.getDeviceID,
      topic: 'streaming1',
      dialog: false,
      roomNum: null,
      dialogRTC: true,
      dialogRTC2: false,
      endBtn: false,
      mentorName: null,
      successSnack: false,
      errorSnack: false,
      altMsg: '',
      startFlag: true,
    };
  },
  mounted() {
    if (this.getRole != `mentee`) {
      this.$router.push('/');
    }
    this.createRoomId();
    if (this.$route.params.room) {
      this.roomId = this.$route.params.room;
    }
    this.videoTag = document.getElementById('videoTag');
    Promise.all([
      faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
      faceapi.nets.faceLandmark68Net.loadFromUri('/models'),
      faceapi.nets.faceRecognitionNet.loadFromUri('/models'),
      faceapi.nets.faceExpressionNet.loadFromUri('/models'),
    ]);
  },
  methods: {
    createRoomId(length = 20) {
      let text = '';
      const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      Array.from(Array(length)).forEach(() => {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
      });
      this.roomId = text;
    },
    createRoom() {
      this.e1 = 3;
      console.log('create Room :::::: ' + this.getUserNum);
      if (this.getUserNum == null) {
        this.$router.go();
      } else {
        http
          .post(`/counseling/liveRequest`, {
            mentee: this.getUserNum,
            mentor: 1,
            room: this.roomId,
            status: 'liveRequest',
            date: new Date(),
          })
          .then((res) => {
            this.checkCnt(res);
            // this.sendDM(res);
            this.roomNum = res.data;
          });
        // this.dialog = true;
        this.onJoin();
      }
    },
    checkCnt(data) {
      console.log(' checkCnt ');
      http.get(`/counseling/getMenteeMSGCnt`).then((res) => {
        console.log(' ' + res.data);
        if (res.data == 1) {
          console.log(res.data);
          this.sendDM(data);
        }
      });
    },
    sendDM(res) {
      console.log('sendDM');
      console.dir(res);
      const message = {
        data: {
          body: '상담을 하고 싶어해요~',
          title: '학생 클릭 함',
          icon: 'favicon.ico',
          room: this.roomId,
          room_num: res.data,
        },
        to: '/topics/streaming1',
      };
      const config = {
        headers: {
          'Content-type': 'application/json',
          Accept: 'application/json',
          Authorization:
            'key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd',
        },
      };
      axios
        .post(this.url, message, config)
        .then((response) => {
          if (response.status < 200 || response.status >= 400) {
            throw 'Error subscribing to topic: ' + response.status + ' - ' + response.text();
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    onJoin() {
      this.isProgress = true;
      this.dialogRTC = false;
      setTimeout(() => {
        this.$refs.webrtc.join();
      }, 1000);
      this.startVideo();
    },
    onLeave() {
      this.$store.commit('changeIsRemote', false);
      if(this.$refs.webrtc){
        this.$refs.webrtc.leave();
      }
      this.stopVideo();
      this.deleteDB();
      http.delete(`/counseling/liveList`); //conroom 삭제
      this.$router.push('/');
    },
    onError(error, stream) {
      console.log('On Error Event', error, stream);
    },
    sendMessage(data) {
      this.$refs.webrtc.sendMessage(data);
    },
    addEventListener() {
      const canvas = faceapi.createCanvasFromMedia(this.videoTag);
      const displaySize = {
        width: this.videoTag.width,
        height: this.videoTag.height,
      };
      faceapi.matchDimensions(canvas, displaySize);
      setInterval(async () => {
        const detections = await faceapi
          .detectAllFaces(this.videoTag, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceExpressions();

        if (detections.length > 0) {
          this.sendMessage(detections[0].expressions);
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
        // const picture = document.querySelector("canvas");
        // const ctx = picture.getContext("2d");
        // // 픽셀 정리
        // ctx.clearRect(0, 0, picture.width, picture.height);
        // // 컨텍스트 리셋
        // ctx.beginPath();
      }
    },
    dialogCancel() {
      this.dialog = !this.dialog;
      this.onLeave();
    },
    deleteDB() {
      http.delete(`/counseling/deleteReadyMentee/${this.roomNum}`).then(() => {});
    },
    finishLive() {
      this.successSnack = true;
      this.altMsg = '상담이 종료되었습니다.';
      setTimeout(() => {
        this.onLeave();
      }, 2000);
    }
  },
  computed: {
    ...mapGetters([
      'isProfileLoaded',
      'getRole',
      'getQualification',
      'getUserName',
      'getUserNum',
      'getUserID',
      'getIsRemote',
      'getDeviceID',
    ]),
  },
  watch: {
    getIsRemote(val) {
      console.log('isremote + ' + val);
      if (val) {
        console.log('remote 들어옴');
        this.successSnack = true;
        this.altMsg = '상담사가 들어옵니다.';
        this.dialog = false;
        this.endBtn = true;
        http.get(`/counseling/liveMentorInfo/${this.roomId}`).then((res) => {
          this.mentorName = res.data.name;
        });
        // this.onLeave();
      }
    },
    dialog(val) {
      if (!val) {
        return;
      }
      console.log('isremote ' + this.getIsRemote);
      setTimeout(() => {
        if(!this.getIsRemote){
          this.dialog = false;
          this.errorSnack = true;
          this.altMsg = '매칭에 실패했습니다.';
          setTimeout(() => {
            this.onLeave();
          }, 2000);
        }
      }, 60000);
    },
  },
};
</script>

<style scoped>
#step-alert {
  margin: 0 auto;
  background-color: #ff7987;
  height: 550px;
}
.step-text {
  font-size: 2vw;
}
.heartbeat {
  -webkit-animation: heartbeat 2s ease-in-out infinite both;
  animation: heartbeat 2s ease-in-out infinite both;
}
@-webkit-keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
@keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}

</style>
