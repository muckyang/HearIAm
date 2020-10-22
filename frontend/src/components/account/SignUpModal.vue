<template>
  <div>
    <v-card-title class="text-center justify-center p-8">
      <!-- <h1 class="font-weight-bold display-3 basil--text">{{ userRole }}</h1> -->
      <p style="font-family: 'Capriola'; font-size:3rem; font-weight:700;">Hear I Am</p>
    </v-card-title>
    <SignUp :role="role" />
  </div>
</template>

<script>
import SignUp from "@/components/account/SignUp.vue";
export default {
  name: "SignUpModal",
  props: {
    role: { type: String },
  },
  components: {
    SignUp,
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