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
            <tr v-for="item in list" :key="item.name">
              <td class="text-center">
                {{ item.mentee}}
              </td>
              <td class="text-center">
                   {{ item.room }}
                <!-- <v-btn
                  small
                  @click="startCounseling(item)"
                  style="font-size: 0.9rem"
                  >상담시작</v-btn
                > -->
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
};
</script>