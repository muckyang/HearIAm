<template>
  <div class="reserve-main">
    <div class="reserve-body">
      <v-sheet class="sheet-body container" rounded="xl" elevation="7">
        <h1 class="mb-5">상담 예약하기</h1>
        <v-row class="d-flex justify-content-center">
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <v-dialog ref="dialogDate" v-model="dateModal" :return-value.sync="date" persistent width="290px">
              <template v-slot:activator="{ on, attrs }">
                <div class="d-flex">
                  <v-icon class="mr-3" large color="black">mdi-calendar</v-icon>
                  <v-text-field
                    class="pt-7"
                    v-model="date"
                    label="날짜를 선택해주세요."
                    readonly
                    solo
                    v-bind="attrs"
                    v-on="on"
                  ></v-text-field>
                </div>
              </template>
              <v-date-picker v-model="date" scrollable>
                <v-spacer></v-spacer>
                <v-btn text color="primary" @click="dateModal = false">
                  Cancel
                </v-btn>
                <v-btn text color="primary" @click="$refs.dialogDate.save(date)">
                  OK
                </v-btn>
              </v-date-picker>
            </v-dialog>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <v-dialog ref="dialogTime" v-model="timeModal" :return-value.sync="time" persistent width="290px">
              <template v-slot:activator="{ on, attrs }">
                <div class="d-flex">
                  <v-icon class="mr-3" large color="black">mdi-clock-time-four-outline</v-icon>
                  <v-text-field
                    class="mt-7"
                    v-model="time"
                    label="시간을 선택해주세요."
                    readonly
                    solo
                    v-bind="attrs"
                    v-on="on"
                  ></v-text-field>
                </div>
              </template>
              <v-time-picker v-if="timeModal" v-model="time" full-width>
                <v-spacer></v-spacer>
                <v-btn text color="primary" @click="timeModal = false">
                  Cancel
                </v-btn>
                <v-btn text color="primary" @click="$refs.dialogTime.save(time)">
                  OK
                </v-btn>
              </v-time-picker>
            </v-dialog>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <div class="d-flex">
              <v-icon class="mr-3" large color="black">mdi-help-circle-outline</v-icon>
              <v-select class="mt-7" v-model="concern" :items="items" label="고민이 무엇인가요?" solo></v-select>
            </div>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="6" md="6" class="reserve-data py-1 px-0">
            <div class="d-flex">
              <v-icon class="mr-3" large color="black">mdi-account-check-outline</v-icon>
              <v-text-field class="mt-7" v-model="consultant" label="원하는 상담사가 있나요?" solo></v-text-field>
            </div>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="2" class="reserve-data pt-10 px-0">
            <v-btn color="#ffdc15">예약하기</v-btn>
          </v-col>
        </v-row>
      </v-sheet>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      date: new Date().toISOString().substr(0, 10),
      dateModal: false,
      timeModal: false,
      time: null,
      concern: '',
      consultant: '',
      items: ['시험 성적 때문에 고민이에요..', '진로에 대한 고민이 있어요.', '괴롭힘을 당하고 있어요...', '친구문제로 상담을 받고 싶어요.'],
    };
  },
};
</script>

<style scoped>
.reserve-main {
  background-image: linear-gradient(to right, #93dfff, #f5a2bb);
  height: 100%;
}
.reserve-body {
  height: 100%;
  padding: 2%;
}
.sheet-body {
  background-image: linear-gradient(to right, #93dfff, white, #f5a2bb);
  padding: 50px;
  height: 100%;
  text-align: center;
}
.v-picker__title {
  height: 100px;
}
.reserve-data {
  margin: 0 auto;
}
</style>
