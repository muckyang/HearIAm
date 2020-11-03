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
            <v-btn
              v-if="!isProgress"
              type="button"
              class="btn btn-primary"
              @click="createRoom"
            >
              상담 요청
            </v-btn>
            <v-btn
              v-else-if="failMatching"
              type="button"
              class="btn btn-primary"
              @click="mathing()"
            >
              매칭 실패 돌아가기
            </v-btn>
            <v-btn
              v-else
              type="button"
              class="btn btn-primary"
              @click="onLeave"
            >
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
    <v-dialog v-model="dialog" hide-overlay persistent width="300">
      <v-card color="primary" dark>
        <v-card-text>
          Please stand by
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          ></v-progress-linear>
        </v-card-text>
        <v-btn @click="dialogCancel()">취소</v-btn>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/Uwebrtc.vue";
import * as io from "socket.io-client";
import * as faceapi from "face-api.js";
window.io = io;
import { mapGetters } from "vuex";
import http from "@/util/http-common.js";
import axios from "axios";
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
      url: "https://fcm.googleapis.com/fcm/send",
      devecieId: this.getDeviceID,
      topic: "streaming",
      dialog: false,
      failMatching: false,
      roomNum : null,
    };
  },
  mounted() {
    if (this.getRole != `mentee`) {
      this.$router.push("/");
    }
    this.createRoomId();
    if (this.$route.params.room) {
      this.roomId = this.$route.params.room;
    }
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
    createRoom() {
      http
        .post(`/counseling/liveRequest`, {
          mentee: this.getUserNum,
          mentor: 1,
          room: this.roomId,
          status: "liveRequest",
          date: new Date(),
        })
        .then((res) => {
          if (res.data > 0) {
            this.dialog = true;
          }
          this.checkCnt(res);
          // this.sendDM(res);
          this.roomNum = res.data;
        });
      this.onJoin();
    },
    checkCnt(data){
      http.get(`/getMenteeMSGCnt`).then((res)=>{
        if(res.data == 1){
          this.sendDM(data);
        }
      });
    },
    sendDM(res) {
      const message = {
        data: {
          body: "상담을 하고 싶어해요~",
          title: "학생 클릭 함",
          icon: "favicon.ico",
          room: this.roomId,
          room_num: res.data,
        },
        to: "/topics/streaming",
      };
      const config = {
        headers: {
          "Content-type": "application/json",
          Accept: "application/json",
          Authorization:
            "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd",
        },
      };

      axios
        .post(this.url, message, config)
        .then((response) => {
          if (response.status < 200 || response.status >= 400) {
            throw (
              "Error subscribing to topic: " +
              response.status +
              " - " +
              response.text()
            );
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    onJoin() {
      this.isProgress = true;
      this.$refs.webrtc.join();
      this.startVideo();
    },
    onLeave() {
      this.$store.commit("changeIsRemote", false);
      this.$refs.webrtc.leave();
      this.stopVideo();
      this.$router.push("/");
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
      this.mathing();
    },
    mathing() {
      http
        .delete(
          `/counseling/deleteReadyMentor/${this.getUserNum}}`
        )
        .then(() => {});
      this.onLeave();
    },
    deleteDB() {
      http
        .delete(`/counseling/deleteReadyMentee/${this.roomNum}`)
        .then(() => {});
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
      "getIsRemote",
      "getDeviceID",
    ]),
  },
  watch: {
    getIsRemote(val) {
      console.log("isremote + " + val);
      if (val) {
        console.log("remote 들어옴");
        alert("상담가가 들어옵니다. ");
        this.dialog = false;
        this.deleteDB();
      }
    },
    dialog(val) {
      if (!val) {
        return;
      }
      console.log("isremote " + this.getIsRemote);
      setTimeout(() => {
        this.dialog = false;
        this.failMatching = true;
        this.deleteDB();
      }, 60000);
      //1분
    },
  },
};
</script>
