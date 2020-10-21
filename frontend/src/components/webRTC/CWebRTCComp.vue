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
            @childs-event="parentsMethod"
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
        <Bar :chartData="chartData" :options="options" />
      </div>
    </div>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/webrtc.vue";
import Bar from "@/components/webRTC/Bar.js";
import * as io from "socket.io-client";
window.io = io;

export default {
  name: "CWebRTCComp",
  components: {
    WebRTC,
    Bar,
  },
  data() {
    return {
      img: null,
      roomId: "public-room",
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
    this.fillData();
  },
  methods: {
    onJoin() {
      this.$refs.webrtc.join();
    },
    onLeave() {
      this.$refs.webrtc.leave();
    },
    onError(error, stream) {
      console.log("On Error Event", error, stream);
    },
    logEvent(event) {
      console.log("Event : ", event);
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
};
</script>