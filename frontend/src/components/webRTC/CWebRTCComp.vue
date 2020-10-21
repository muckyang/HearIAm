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
      </div>
    </div>
  </div>
</template>

<script>
import WebRTC from "@/components/webRTC/webrtc.vue";
import * as io from 'socket.io-client'
window.io = io

export default {
  name: "CWebRTCComp",
  components:{
      WebRTC
  },
  data() {
    return {
      img: null,
      roomId: "public-room",
      message: null,
      emotion: null,
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
    sendMessage() {
      this.$refs.webrtc.sendMessage(this.message);
    },
    parentsMethod: function(message) {
      this.emotion = message;
    },
  },
};
</script>