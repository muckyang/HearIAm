<template>
  <v-app>
    <!-- <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
          transition="scale-transition"
          width="40"
        />

        <v-img
          alt="Vuetify Name"
          class="shrink mt-1 hidden-sm-and-down"
          contain
          min-width="100"
          src="https://cdn.vuetifyjs.com/images/logos/vuetify-name-dark.png"
          width="100"
        />
      </div>

      <v-spacer></v-spacer>

      <v-btn
        href="https://github.com/vuetifyjs/vuetify/releases/latest"
        target="_blank"
        text
      >
        <span class="mr-2">Latest Release</span>
        <v-icon>mdi-open-in-new</v-icon>
      </v-btn>
    </v-app-bar> -->
    <v-main>
      <router-view />
    </v-main>
    <!-- <v-btn @click="logout()">LogOut</v-btn> -->
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

    };
  },
  methods:{
    logout: function() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        this.drawer = false;
      });
      this.$router.push("/").catch(() => {});
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
    ...mapState({
      authLoading: state => state.auth.status === "loading",
      role: state => `${state.user.getRole}`,
      qualification: state => `${state.user.getQualification}`,
      userNum: state => `${state.user.getUserNum}`,
      userName: state => `${state.user.getUserName}`,
      userID: state => `${state.user.getUserID}`
    })
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
  -ms-overflow-style: none !important;
  scroll-behavior: smooth;
}
</style>