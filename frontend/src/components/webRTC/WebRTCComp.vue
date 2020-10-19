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
          <vue-webrtc
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
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import { WebRTC } from "vue-webrtc";
import * as io from 'socket.io-client'
window.io = io
Vue.component(WebRTC.name, WebRTC);

export default {
  name: "WebRTCComp",
  data() {
    return {
      img: null,
      roomId: "public-room",
    };
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
  },
};
</script>