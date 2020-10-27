<template>
    <div>
        <div style="height: 100%; width: 100%; background: linear-gradient(to left, #93dfff, #f5a2bb);">
          
          <div style="
          height: 100vh" class="d-flex justify-content-center">
             <v-col class="my-auto" align="center">
                 <div>
                 <v-btn
                  depressed
                  elevation="9"
                  fab
                  icon
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/t_stream_btn.png"
                    style="width: 100%; height: 100%; "
                    @click="goLiveList()"
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
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/t_voice_btn.png"
                    style="width: 100%; height: 100%; "
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
                  style="width: 260px; height: 250px; background: linear-gradient(to top, #93dfff, #f5a2bb);"
                  @click="goMyMenteeList()"
                >
                <div style="width: 260px; height: 250px; padding: 17px;" class="justify-content-center mb-5; " text-align="center" >
                <img
                    src="@/assets/icons/t_report_btn.png"
                    style="width: 100%; height: 100%; "
                  />
                </div>
                </v-btn>
                </div>
             </v-col>
             <v-btn @click="goMypage()">마이페이지</v-btn>
             <v-btn @click="logout()">로그아웃</v-btn>
          </div>
        </div>
    </div>
</template>

<script>
import { AUTH_LOGOUT } from "@/store/actions/auth";
export default {
  methods:{
    goLiveList(){
      this.$router.push(`/liveList`);
    },
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
      });
      this.$router.push("/").catch(() => {});
    },
    goMypage() {
      this.$router.push(`/mentorMypage`);
    },
    goMyMenteeList() {
      this.$router.push(`/myMenteeList`);
    }
  }
}
</script>