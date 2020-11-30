<template>
  <div>
    <video id="videoTag" width="720" height="560" style="border:1px solid black" muted @playing="addEventListener()"></video>
    <v-btn color="success" @click="startVideo()">start</v-btn>
    <v-btn color="error" @click="stopVideo()">stop</v-btn>
  </div>
</template>

<script>
// npm install --save face-api.js
import * as faceapi from 'face-api.js';

export default {
  mounted() {
    this.videoTag = document.getElementById('videoTag');
    Promise.all([
      faceapi.nets.tinyFaceDetector.loadFromUri('/models'),
      faceapi.nets.faceLandmark68Net.loadFromUri('/models'),
      faceapi.nets.faceRecognitionNet.loadFromUri('/models'),
      faceapi.nets.faceExpressionNet.loadFromUri('/models'),
    ]);
  },
  data() {
    return {
      videoTag: [],
    };
  },
  created() {
  },
  methods: {
    addEventListener() {
      const canvas = faceapi.createCanvasFromMedia(this.videoTag);
      document.body.append(canvas);
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
        const resizedDetections = faceapi.resizeResults(detections, displaySize);
        canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
        faceapi.draw.drawDetections(canvas, resizedDetections);
        faceapi.draw.drawFaceLandmarks(canvas, resizedDetections);
        faceapi.draw.drawFaceExpressions(canvas, resizedDetections);

        if (detections.length > 0) {
          console.log(detections[0].expressions);
        }
      }, 300);
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

<style>
body {
  margin: 0;
  padding: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

canvas {
  position: absolute;
}
</style>
