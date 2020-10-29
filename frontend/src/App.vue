<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>
    <v-speed-dial
      v-if="getUserNum"
      v-model="fab"
      bottom
      right
      open-on-hover
      fixed
    >
      <template v-slot:activator>
        <v-btn v-model="fab" color="blue darken-2" dark fab>
          <v-icon v-if="fab"> mdi-close </v-icon>
          <v-icon v-else> mdi-account-circle </v-icon>
        </v-btn>
      </template>
      <v-btn fab dark small color="green">
        <v-icon @click="goHome()">mdi-home</v-icon>
      </v-btn>
      <v-btn fab dark small color="indigo">
        <v-icon @click="goMypage()">mdi-clipboard-list-outline</v-icon>
      </v-btn>
      <v-btn fab dark small color="red">
        <v-icon @click="logout()">mdi-logout</v-icon>
      </v-btn>
    </v-speed-dial>
  </v-app>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import { AUTH_LOGOUT } from "@/store/actions/auth";

export default {
  name: "App",
  components: {

  },
  data() {
    return {
      fab: false,
    };
  },
  methods: {
    logout: function () {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.drawer = false;
      });
      this.$router.push("/").catch(() => {});
    },
    goHome() {
      this.$router.push("/").catch(() => {});
    },
    goMypage() {
      if(this.getRole == `mentee`){
        this.$router.push(`/menteeMypage`).catch(()=>{});
      } else if(this.getRole == `mentor`){
        this.$router.push(`/mentorMypage`).catch(()=>{});
      }
    }
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
    ...mapState({
      authLoading: (state) => state.auth.status === "loading",
      role: (state) => `${state.user.getRole}`,
      qualification: (state) => `${state.user.getQualification}`,
      userNum: (state) => `${state.user.getUserNum}`,
      userName: (state) => `${state.user.getUserName}`,
      userID: (state) => `${state.user.getUserID}`,
    }),
  },
};
</script>
<style scoped>
@font-face {
  font-family: "S-CoreDream-5Medium";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/S-CoreDream-5Medium.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
@font-face {
  font-family: "yg-jalnan";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_four@1.2/JalnanOTF00.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
#app {
  font-family: "S-CoreDream-5Medium";
}
body {
  -ms-overflow-style: none !important;
}
::-webkit-scrollbar {
  display: none !important;
}

html {
  scroll-behavior: smooth;
}
#create .v-speed-dial {
  position: absolute;
}

#create .v-btn--floating {
  position: relative;
}
</style>