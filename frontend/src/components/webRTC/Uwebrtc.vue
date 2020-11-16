<template>
  <div class="video-list">
    <div
      v-for="item in videoList"
      v-bind:video="item"
      v-bind:key="item.id"
      class="video-item"
    >
      <video
        v-if="item.visibility == `hidden`"
        autoplay
        playsinline
        ref="videos"
        :height="cameraHeight"
        :muted="item.muted"
        :id="item.id"
        visibility: hidden
      ></video>
      <video
        v-else
        autoplay
        playsinline
        ref="videos"
        :height="cameraHeight"
        :muted="item.muted"
        :id="item.id"
      ></video>
    </div>
  </div>
</template>

<script>
import RTCMultiConnection from "rtcmulticonnection";
require("adapterjs");
export default {
  name: "vue-webrtc",
  data() {
    return {
      rtcmConnection: null,
      localVideo: null,
      videoList: [],
      canvas: null,
      isLeave: false,
    };
  },
  props: {
    roomId: {
      type: String,
      default: "public-room",
    },
    socketURL: {
      type: String,
      default: "https://rtcmulticonnection.herokuapp.com:443/",
    },
    cameraHeight: {
      type: [Number, String],
      default: 160,
    },
    autoplay: {
      type: Boolean,
      default: true,
    },
    screenshotFormat: {
      type: String,
      default: "image/jpeg",
    },
    enableAudio: {
      type: Boolean,
      default: true,
    },
    enableVideo: {
      type: Boolean,
      default: true,
    },
    enableLogs: {
      type: Boolean,
      default: false,
    },
    stunServer: {
      type: String,
      default: null,
    },
    turnServer: {
      type: String,
      default: null,
    },
  },
  watch: {
    videoList(){
      if(this.isLeave){
        this.$emit("isLeave");
      }
      if(this.videoList.length>1){
        this.$store.commit("changeIsRemote",true);
        console.log("remote 들어옴 : "+ this.$store.getters['getIsRemote'])
        this.isLeave = true;
      }
    },
  },
  mounted() {
    var that = this;
    this.rtcmConnection = new RTCMultiConnection();
    this.rtcmConnection.socketURL = this.socketURL;
    this.rtcmConnection.autoCreateMediaElement = false;
    this.rtcmConnection.enableLogs = this.enableLogs;
    this.rtcmConnection.session = {
      audio: this.enableAudio,
      video: this.enableVideo,
      data: true,
    };
    this.rtcmConnection.sdpConstraints.mandatory = {
      OfferToReceiveAudio: this.enableAudio,
      OfferToReceiveVideo: this.enableVideo,
    };
    if (this.stunServer !== null || this.turnServer !== null) {
      this.rtcmConnection.iceServers = []; // clear all defaults
    }
    if (this.stunServer !== null) {
      this.rtcmConnection.iceServers.push({
        urls: this.stunServer,
      });
    }
    if (this.turnServer !== null) {
      var parse = this.turnServer.split("%");
      var username = parse[0].split("@")[0];
      var password = parse[0].split("@")[1];
      var turn = parse[1];
      this.rtcmConnection.iceServers.push({
        urls: turn,
        credential: password,
        username: username,
      });
    }
    this.rtcmConnection.onstream = function (stream) {
      let found = that.videoList.find((video) => {
        return video.id === stream.streamid;
      });
      if (found === undefined) {

        let view = null;
        if (stream.type === "local") {
          view = `hidden`;
        }
        let video = {
          id: stream.streamid,
          muted: stream.type === "local",
          visibility: view
        };
        that.videoList.push(video);
        if (stream.type === "local") {
          that.localVideo = video;
        }
      }
      setTimeout(function () {
        for (var i = 0, len = that.$refs.videos.length; i < len; i++) {
          if (that.$refs.videos[i].id === stream.streamid) {
            that.$refs.videos[i].srcObject = stream.stream;
            break;
          }
        }
      }, 1000);

      that.$emit("joined-room", stream.streamid);
    };
    this.rtcmConnection.onstreamended = function (stream) {
      var newList = [];
      that.videoList.forEach(function (item) {
        if (item.id !== stream.streamid) {
          newList.push(item);
        }
      });
      that.videoList = newList;
      that.$emit("left-room", stream.streamid);
    };
    this.rtcmConnection.onmessage = function (event) {
      // console.dir(event.data);
      that.$emit('childs-event', event.data);
    };
  },
  methods: {
    join() {
      var that = this;
      this.rtcmConnection.openOrJoin(this.roomId, function (
        isRoomExist,
        roomid
      ) {
        if (isRoomExist === false && that.rtcmConnection.isInitiator === true) {
          that.$emit("opened-room", roomid);
        }
      });
    },
    leave() {
      this.rtcmConnection.attachStreams.forEach(function (localStream) {
        localStream.stop();
      });
      this.videoList = [];
    },
    capture() {
      return this.getCanvas().toDataURL(this.screenshotFormat);
    },
    getCanvas() {
      let video = this.getCurrentVideo();
      if (video !== null && !this.ctx) {
        let canvas = document.createElement("canvas");
        canvas.height = video.clientHeight;
        canvas.width = video.clientWidth;
        this.canvas = canvas;
        this.ctx = canvas.getContext("2d");
      }
      const { ctx, canvas } = this;
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      return canvas;
    },
    getCurrentVideo() {
      if (this.localVideo === null) {
        return null;
      }
      for (var i = 0, len = this.$refs.videos.length; i < len; i++) {
        if (this.$refs.videos[i].id === this.localVideo.id)
          return this.$refs.videos[i];
      }
      return null;
    },
    sendMessage(event) {
      this.rtcmConnection.send(event);
    },
  },
};
</script>
<style scoped>
.video-list {
  height: auto;
}
.video-list div {
  padding: 0px;
}
.video-item {
  display: inline-block;
}
</style>
