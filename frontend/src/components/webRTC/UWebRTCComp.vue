<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 my-3">
        <h2>Room</h2>
        <v-text-field v-model="roomId" />
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div>
          <WebRTC
            ref="webrtc"
            width="100%"
            :roomId="roomId"
            cameraHeight="500"
            v-on:joined-room="logEvent"
            v-on:left-room="logEvent"
            v-on:opened-room="logEvent"
            v-on:share-started="logEvent"
            v-on:share-stopped="logEvent"
            @error="onError"
          />
        </div>
        <div class="row">
          <div class="col-md-12 my-3">
            <v-btn type="button" class="btn btn-primary" @click="onJoin">
              Join
            </v-btn>
            <v-btn type="button" class="btn btn-primary" @click="onLeave">
              Leave
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
      </div>
    </div>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/webrtc.vue";
import * as io from "socket.io-client";
import * as faceapi from 'face-api.js';
window.io = io;

export default {
  name: "UWebRTCComp",
  components: {
    WebRTC,
  },
  data() {
    return {
      img: null,
      roomId: "public-room",
      message: null,
      videoTag: [],
    };
  },
  mounted() {
    this.videoTag = document.getElementById('videoTag');
    Promise.all([
      faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
      faceapi.nets.faceLandmark68Net.loadFromUri('/models'),
      faceapi.nets.faceRecognitionNet.loadFromUri('/models'),
      faceapi.nets.faceExpressionNet.loadFromUri('/models'),
    ]);
  },
  methods: {
    onJoin() {
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
    logEvent(event) {
      console.log("Event : ", event);
    },
    sendMessage(data) {
      this.$refs.webrtc.sendMessage(data);
    },
    addEventListener() {
      const canvas = faceapi.createCanvasFromMedia(this.videoTag);
      // document.body.append(canvas);
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

        // detections
        // const resizedDetections = faceapi.resizeResults(detections, displaySize);
        // canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
        // faceapi.draw.drawDetections(canvas, resizedDetections);
        // faceapi.draw.drawFaceLandmarks(canvas, resizedDetections);
        // faceapi.draw.drawFaceExpressions(canvas, resizedDetections);

        if (detections.length > 0) {
          this.sendMessage(detections[0].expressions);
          // console.dir(detections[0].expressions);
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
        const picture = document.querySelector('canvas');
        const ctx = picture.getContext('2d');
        // 픽셀 정리
        ctx.clearRect(0, 0, picture.width, picture.height);
        // 컨텍스트 리셋
        ctx.beginPath();
      }
    },
  },
};
</script>