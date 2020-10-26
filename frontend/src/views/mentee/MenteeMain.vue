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
export default {
  data() {
    return {};
  },
  methods: {
    goLive() {
      this.$router.push(`/userWRTC`);
    },
  },
};
</script>
