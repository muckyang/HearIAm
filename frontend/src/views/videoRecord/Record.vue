<template>
  <v-container>
    <main class="container has-text-centered">
      <section id="example-audio">
        <div class="columns">
          <div class="column">
            <div class="has-text-right">
              <h2 >녹음상담</h2>
              <h3 class="subtitle"><strong>마이크</strong> 버튼을 눌러 상담내용을 녹음하세요.</h3>
            </div>

            <div class="record-settings">
              <vue-record-audio :mode="recordMode.audio" @stream="onStream" @result="onResult" />
            </div>
          </div>
          <div class="column">
            <div class="recorded-audio">
              <div v-for="(record, index) in recordings" :key="index" class="recorded-item">
                <div class="audio-container"><audio :src="record.src" controls /></div>
                <div><button @click="removeRecord(index)" class="button is-dark">삭제하기</button></div>
                
              </div>
                <div><button @click="fileUpload()" class="button is-dark">저장하기</button></div>
            </div>
          </div>
        </div>
      </section>
<!--  <section id="example-video">
        <div class="columns">
          <div class="column">
            <div class="has-text-right">
              <h3 class="title is-3">Recording video files</h3>
              <p class="subtitle">Simply <strong>{{recordMode.video}}</strong> the button to record a video clip</p>
            </div>

            <div class="record-settings">
              <vue-record-video mode="press" @stream="onVideoStream" @result="onVideoResult" />
            </div>
          </div>
          <div class="column">
            <div class="recorded-video">
              <video ref="Video" width="400" height="250" controls></video>
            </div>
          </div>
        </div>
      </section> -->
    </main>
  </v-container>
</template>

<script>
import axios from "axios";


const BACK_URL = 'http://localhost:8080';
export default {
    name: 'Record',
   data () {
    return {
     recordMode: {
        audio: 'press',
        video: 'press'
      },
      recordings: [],
      file :''
    }
  },
  methods: {
    callback (data) {
      console.debug(data)
    },
    removeRecord (index) {
      this.recordings.splice(index, 1)
    },
    onStream (stream) {
      console.log('Got a stream object:', stream);
    },
    // onVideoStream (stream) {
    //   console.log('Got a video stream object:', stream);
    // },
    // onVideoResult (data) {
    //   this.$refs.Video.srcObject = null
    //   this.$refs.Video.src = window.URL.createObjectURL(data)
    //   console.log(data)
    // },
    onResult (data) {
      console.log(data)
      // this.file = this.blobToFile(data, "my-record.wav"); 
      data.lastModifiedDate = new Date();
      this.file = new File([data], "record.wav")
      this.recordings.push({
        src: window.URL.createObjectURL(data)
      })
    },
   
    fileUpload() {
      console.log("upload")
      console.log(this.file)
      var formData = new FormData();
      formData.append("file", this.file);
      axios
        .post(`${BACK_URL}/api/record/test`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(() => {
          console.log("in!!!!!!!!!!!!")
        })
        .catch((error) => {
          console.log(error);
        });
    },

  }
};

</script>

<style lang="scss">
html, body, #app {
  width: 100%;
  height: 100%;
}
#app {
  display: flex;
  flex-direction: column;
  main{
    height: 100%;
  }

  strong, a {
    color: #41b883 !important;
  }

  section {
    margin: 40px 0;
  }
}

.header {
  padding: 20px 10px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  .navbar-brand img {
    height: 64px;
    margin-right: 20px;
    width: 64px;
  }
}

.vue-audio-recorder, .vue-video-recorder {
  margin-right: 16px;
}

.record-settings {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.recorded-audio {
  margin: 0 auto;
  height: 200px;
  overflow: auto;
  border: thin solid #eee;
  background-color: #f7f7f7;
  padding: 10px 5px;
  .recorded-item {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16px;
  }
  .audio-container {
    display: flex;
    height: 54px;
    margin-right: 16px;
  }
}

.recorded-video {
  video {
    width: 100%;
    max-height: 400px;
  }
}

.footer {
  background-color: #eee;
}
</style>