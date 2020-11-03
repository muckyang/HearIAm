<template>
  <div>
    <h1>알람 리스트</h1>
    <v-card>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th class="text-center">mentee</th>
              <th class="text-center">room name</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in list" :key="item.name" @click="onjoin(item)">
              <td class="text-center" >
                {{ item.mentee}}
              </td>
              <td class="text-center" >
                   {{ item.room }}
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card>
    <!-- <v-pagination v-model="page" :length="pageLength" circle></v-pagination> -->
  </div>
</template>
<script>
import http from "@/util/http-common.js";
import axios from "axios";
export default {
  data() {
    return {
      list: [],
    };
  },
  mounted() {
    http.get(`/counseling/alarmList`).then((res) => {
      this.list = res.data;
    });
  },
  methods: {
    onjoin(data) {
      let mentorName = this.$store.getters['getUserNum'];
      console.log(mentorName+" "+data.room);
      http
        .get(`/counseling/isRoom/${mentorName}/${data.room}`)
        .then((res) => {
          console.log(res.data);
          if (res.data == "fail") {
            alert("이미 상담 중입니다. 다음엔 더 빨리 수락하세욧! ㅇㅅㅇ! ");
          } else {
            alert(" 상담을 시작합니다. ")
            this.unsubscribe();
            this.$router.push(`/counselorWRTC/${data.room}&${mentorName}`);
          }
        })
        .catch((e) => {
          console.log(e);
        });
      
    },
    unsubscribe(){
      let token = this.$store.getters["getDeviceID"];
      let topic = "streaming";
      axios({
        method: "POST",
        url: "https://iid.googleapis.com/iid/v1:batchRemove",
        data: {
          to: "/topics/" + topic,
          registration_tokens: [token],
        },
        headers: {
          "Content-type": "application/json",
          Authorization:
            "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd",
        },
      })
        .then((response) => {
          if (response.status < 200 || response.status >= 400) {
            throw (
              "Error subscribing to topic: " +
              response.status +
              " - " +
              response.text()
            );
          }
          console.log("unsubscribe success : " + response);
        })
        .catch((e) => {
          console.log(e);
        });
        console.log("dekl "+this.$store.getters['getUserNum']);
        http.delete(`/counseling/deleteReadyMentor/${this.$store.getters['getUserNum']}`).then(()=>{
      });
      }
    },
};
</script>