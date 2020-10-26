<template>
  <div class="container">
    <v-row class="row">
      <v-col class="col-md-12">
        <div>
          <WebRTC
            ref="webrtc"
            width="100%"
            :roomId="roomId"
            cameraHeight="500"
            @error="onError"
          />
        </div>
        <div class="row">
          <div class="col-md-12 my-3">
            <v-btn v-if="!isProgress" type="button" class="btn btn-primary" @click="onJoin">
              상담 요청
            </v-btn>
            <v-btn v-else type="button" class="btn btn-primary" @click="onLeave">
              상담 종료
            </v-btn>
          </div>
        </div>
        <div>
          <video
            id="videoTag"
            width="720"
            height="560"
            style="border: 1px solid black"
            muted
            hidden
            @playing="addEventListener()"
          ></video>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/Uwebrtc.vue";
import * as io from "socket.io-client";
import * as faceapi from "face-api.js";
window.io = io;
import { mapGetters } from "vuex";
import http from "@/util/http-common.js";

export default {
  name: "UWebRTCComp",
  components: {
    WebRTC,
  },
  data() {
    return {
      img: null,
      roomId: null,
      message: null,
      videoTag: [],
      isProgress: false,
    };
  },
  mounted() {
    if (this.getRole != `mentee`) {
      this.$router.push("/");
    }
    this.createRoomId();
    this.videoTag = document.getElementById("videoTag");
    Promise.all([
      faceapi.nets.tinyFaceDetector.loadFromUri("/models"),
      faceapi.nets.faceLandmark68Net.loadFromUri("/models"),
      faceapi.nets.faceRecognitionNet.loadFromUri("/models"),
      faceapi.nets.faceExpressionNet.loadFromUri("/models"),
    ]);
  },
  methods: {
    createRoomId(length = 20) {
      let text = "";
      const possible =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      Array.from(Array(length)).forEach(() => {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
      });
      this.roomId = text;
    },
    onJoin() {
      this.isProgress = true;
      http
        .post(`/counseling/liveRequest`, {
          mentee: this.getUserNum,
          mentor: 1,
          room: this.roomId,
          status: "liveRequest",
          date: new Date()
        })
        .then((res) => {
          if (res.data == `success`) {
            alert(`잠시 대기해주세요.`);
          }
        });
      this.$refs.webrtc.join();
      this.startVideo();
    },
    onLeave() {
      this.$refs.webrtc.leave();
      this.stopVideo();
    },
    onError(error, stream) {
      console.log("On Error Event", error, stream);
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
        const videoPlayer = document.getElementById("videoTag");
        videoPlayer.srcObject = stream;
        videoPlayer.play();
      });
    },
    stopVideo() {
      if (this.videoTag.srcObject != null && this.videoTag.srcObject != "") {
        const tracks = this.videoTag.srcObject.getTracks();
        tracks.forEach(function (track) {
          track.stop();
        });
        this.videoTag.srcObject = null;
        const picture = document.querySelector("canvas");
        const ctx = picture.getContext("2d");
        // 픽셀 정리
        ctx.clearRect(0, 0, picture.width, picture.height);
        // 컨텍스트 리셋
        ctx.beginPath();
      }
    },
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
};
</script>