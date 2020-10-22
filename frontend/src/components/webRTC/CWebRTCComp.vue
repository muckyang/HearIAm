<template>
  <div class="container">
    <v-container>
      <v-row>
        <v-col>
          <v-row>
            <v-col>
              <v-btn type="button" class="btn btn-primary" @click="onJoin">
                Join
              </v-btn>
              <v-btn type="button" class="btn btn-primary" @click="onLeave">
                Leave
              </v-btn>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="5">
              <Bar :chartData="chartData" :options="options" />
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="6">
              <WebRTC
                ref="webrtc"
                width="100%"
                :roomId="roomId"
                cameraHeight="400"
                @error="onError"
                @childs-event="parentsMethod"
              />
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/Cwebrtc.vue";
import Bar from "@/components/webRTC/Bar.js";
import * as io from "socket.io-client";
window.io = io;
import { mapGetters } from "vuex";
import http from "@/util/http-common.js";

export default {
  name: "CWebRTCComp",
  components: {
    WebRTC,
    Bar,
  },
  data() {
    return {
      img: null,
      roomId: null,
      message: null,
      chartData: null,
      emotion: [0, 0, 0, 0, 0, 0, 0],
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                max: 100,
                min: 0,
              },
            },
          ],
        },
      },
    };
  },
  mounted() {
    if (this.getRole != `mentor`) {
      this.$router.push("/");
    }
    this.roomId = this.$route.params.room;
    this.fillData();
  },
  methods: {
    onJoin() {
      http.put(`/counseling/joinLive/${this.$route.params.num}/${this.getUserNum}`);
      this.$refs.webrtc.join();
    },
    onLeave() {
      http.put(`/counseling/finishLive/${this.$route.params.num}`);
      this.$refs.webrtc.leave();
    },
    onError(error, stream) {
      console.log("On Error Event", error, stream);
    },
    sendMessage() {
      this.$refs.webrtc.sendMessage(this.message);
    },
    parentsMethod: function (message) {
      this.emotion = [
        Math.floor(message.angry * 100),
        Math.floor(message.disgusted * 100),
        Math.floor(message.fearful * 100),
        Math.floor(message.happy * 100),
        Math.floor(message.neutral * 100),
        Math.floor(message.sad * 100),
        Math.floor(message.surprised * 100),
      ];
      this.fillData();
    },
    fillData() {
      this.chartData = {
        labels: [
          "angry",
          "disgusted",
          "fearful",
          "happy",
          "neutral",
          "sad",
          "surprised",
        ],
        datasets: [
          {
            label: "Emotion",
            backgroundColor: "#f87979",
            data: this.emotion,
          },
        ],
      };
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