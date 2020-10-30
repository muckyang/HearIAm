<template>
  <div>
    <div
      style="
        height: 100%;
        width: 100%;
        background: linear-gradient(to right, #93dfff, #f5a2bb);
      "
    >
      <!-- background: linear-gradient(to right, #3D5469, #79B7D1); -->
      <div style="height: 100vh" class="d-flex justify-content-center">
        <v-col class="my-auto" align="center">
          <div>
            <v-btn
              depressed
              elevation="9"
              fab
              icon
              style="
                width: 260px;
                height: 250px;
                background: linear-gradient(to top, #93dfff, #f5a2bb);
              "
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/stream_btnm.png"
                  style="width: 100%; height: 100%"
                  @click="goLive()"
                />
              </div>
            </v-btn>
          </div>
        </v-col>
        <v-col class="my-auto" align="center">
          <div>
            <v-btn
              depressed
              elevation="9"
              fab
              icon
              style="
                width: 260px;
                height: 250px;
                background: linear-gradient(to top, #93dfff, #f5a2bb);
              "
              @click="goRecord()"
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/voice_btn.png"
                  style="width: 100%; height: 100%"
                />
              </div>
            </v-btn>
          </div>
        </v-col>
        <v-col class="my-auto" align="center">
          <div>
            <v-btn
              depressed
              elevation="9"
              fab
              icon
              style="
                width: 260px;
                height: 250px;
                background: linear-gradient(to top, #93dfff, #f5a2bb);
              "
            >
              <div
                style="width: 260px; height: 250px; padding: 17px"
                class="justify-content-center mb-5;"
                text-align="center"
              >
                <img
                  src="@/assets/icons/res_btn.png"
                  style="width: 100%; height: 100%"
                  @click="goReserve()"
                />
              </div>
            </v-btn>
          </div>
        </v-col>
      </div>
    </div>
  </div>
</template>
<script>
import { AUTH_LOGOUT } from "@/store/actions/auth";
import axios from "axios";
export default {
  data() {
    return {
      url : "https://fcm.googleapis.com/fcm/send",
      devecieId : this.$store.getters["getDeviceID"],
      topic : "streaming"
    };
  },
  methods: {
    goLive() {
      console.log("live click")
      const message = {
          data :{
              body : "상담을 하고 싶어해요~",
              title: "학생 클릭 함",
              icon: "favicon.ico"
          },
          to : "/topics/streaming"
      };
      const config = {
          headers:{
              'Content-type': 'application/json',
              'Accept':'application/json',
              'Authorization' : 'key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd'
          }
      };

      axios.post(this.url , message, config)
      .then(response => {
          if (response.status < 200 || response.status >= 400) {
              throw 'Error subscribing to topic: '+response.status + ' - ' + response.text();
          }
          console.dir(response);
      }).catch(e =>{
          console.log(e);
      });
      this.$router.push(`/userWRTC`);
    },
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
      });
      this.unsubscribe();
      // this.$router.push("/").catch(() => {});
      window.location.href="/";
    },
    goMypage(){
      this.$router.push(`/menteeMypage`).catch(()=>{});
    },
    goReserve(){
      this.$router.push(`/reserveMain`).catch(()=>{});
    },
    goRecord() {
      this.$router.push("/recordConsult/1").catch(()=>{});
    },
    unsubscribe(){
      this.unsubscribeTokenToTopic(this.devecieId, this.topic);
    },
    unsubscribeTokenToTopic(token, topic){
            axios({
                method: 'POST',
                url: 'https://iid.googleapis.com/iid/v1:batchRemove',
                data:{
                    "to":"/topics/"+topic,
                    "registration_tokens":[token]
                },
                headers: {
                    "Content-type": "application/json",
                    "Authorization" : "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd"
                 }
            })
            .then(response => {
                if (response.status < 200 || response.status >= 400) {
                    throw 'Error subscribing to topic: '+response.status + ' - ' + response.text();
                }
                console.log("unsubscribe success : "+response);
            }).catch(e =>{
                console.log(e);
            })
        },
  },
};
</script>
