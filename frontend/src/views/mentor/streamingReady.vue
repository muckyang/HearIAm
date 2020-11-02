<template>
  <div>
    <v-btn @click="onjoin()"> ready </v-btn>
  </div>
</template>
<script>
export default {
  props: {
    room: {
      type: String,
      default: "",
    },
    room_num: {
      type: Number,
      default: 0,
    },
  },
  methods: {
    onjoin() {
      console.log(
        "streaming Ready .vue ---> " + this.room + " " + this.room_num
      );
      this.$router.push(`/counselorWRTC/${this.room}&${this.room_num}`);
    },
  },
};
</script>