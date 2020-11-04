<template>
  <div>
    <v-dialog v-model="dialogRTC" persistent max-width="1000">
      <v-card height="650" dark>
        <v-container>
          <v-row v-if="!isProgress">
            <v-col>
              <v-btn
                type="button"
                class="btn btn-primary"
                @click="createRoom"
              >
                상담 요청
              </v-btn>
            </v-col>
          </v-row>
          <v-row v-if="dialog" style="padding-top:200px">
            <v-col>
              <div>
                <h4 v-if="mentorName">{{mentorName}} 상담사와 연결중입니다.</h4>
                <br />
                <v-progress-circular
                  :size="100"
                  :width="7"
                  color="#0a7a78"
                  indeterminate
                >
                  <v-btn text @click="dialogCancel()">취소</v-btn>
                </v-progress-circular>
              </div>
            </v-col>
          </v-row>
          <v-row style="padding-top:0px">
            <v-col>
              <h1>상담사와 상담 중입니다.</h1><br/>
              <WebRTC
                ref="webrtc"
                width="100%"
                :roomId="roomId"
                cameraHeight="450"
                @error="onError"
              />
            </v-col>
          </v-row>
          <v-row>
            <v-spacer></v-spacer>
            <v-col>
              <v-btn
                v-if="endBtn"
                type="button"
                class="btn btn-primary"
                @click="onLeave"
              >
                상담 종료
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-dialog>
    <video id="videoTag" muted hidden @playing="addEventListener()"></video>
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
      // failMatching: false,
      roomNum: null,
      dialogRTC: true,
      endBtn: false,
      mentorName: null,
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
          this.checkCnt(res);
          // this.sendDM(res);
          this.roomNum = res.data;
        });
      this.dialog = true;
      this.onJoin();
    },
    checkCnt(data) {
      console.log(" checkCnt ");
      http.get(`/counseling/getMenteeMSGCnt`).then((res) => {
        console.log(" " + res.data);
        if (res.data == 1) {
          console.log(res.data);
          this.sendDM(data);
        }
      });
    },
    sendDM(res) {
      console.log("sendDM");
      console.dir(res);
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
      this.deleteDB();
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
        this.endBtn = true;
        http
        .get(`/counseling/liveMentorInfo/${this.roomId}`)
        .then((res) => {
          this.mentorName = res.data.name;
        });
        // this.onLeave();
      }
    },
    dialog(val) {
      if (!val) {
        return;
      }
      console.log("isremote " + this.getIsRemote);
      setTimeout(() => {
        this.dialog = false;
        // this.failMatching = true;
        this.onLeave();
      }, 60000);
      //1분
    },
  },
};
</script>
