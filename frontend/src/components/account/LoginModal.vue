<template>
  <div>
    <v-card-title class="text-center justify-center p-8">
      <!-- <h1 class="font-weight-bold display-3 basil--text">{{ userRole }}</h1> -->
      <p style="font-family: 'Capriola'; font-size:3rem; font-weight:700;">Hear I Am</p>
    </v-card-title>
    <Login :role="role" />
    <!-- <v-tabs v-model="tab" centered icons-and-text>
      <v-tabs-slider></v-tabs-slider>
      <v-tab href="#tab-1">
        로그인
      </v-tab>
      <v-tab href="#tab-2">
        회원가입
      </v-tab>
    </v-tabs>

    <v-tabs-items v-model="tab">
      <v-tab-item :value="'tab-1'">
        <Login :role="role" />
      </v-tab-item>
      <v-tab-item :value="'tab-2'">
        <SignUp :role="role" />
      </v-tab-item>
    </v-tabs-items> -->
  </div>
</template>

<script>
// import SignUp from "@/components/account/SignUp.vue";
import Login from "@/components/account/Login.vue";
export default {
  name: "LoginModal",
  props: {
    role: { type: String },
  },
  components: {
    // SignUp,
    Login,
  },
  data() {
    return {
      tab: null,
      userRole: this.role,
    };
  },
  mounted() {
    if (this.role == `mentee`) {
      this.userRole = `청소년`;
    } else {
      this.userRole = `상담사`;
    }
  },
  watch: {
    role(val) {
      if (val == `mentee`) {
        this.userRole = `청소년`;
      } else {
        this.userRole = `상담사`;
      }
    },
  },
};
</script>

<style>
</style>