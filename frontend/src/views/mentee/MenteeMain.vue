<template>
    <div>
        
        <button @click="show = !show">
            Toggle render
        </button>
        <transition name="slide-fade">
            <div v-if="show" style="background: #f5a2bb; height: 100vh"> <h1>hello</h1> </div>
        </transition>
    </div>
    
</template>
<script>
export default {
    data() {
        return{
            show:false
        }
    },
}
</script>
<style scoped>
.slide-fade-enter-active {
  transition: all .3s ease;
}
.slide-fade-leave-active{
transition: all .3s ease-out;
}
/* 93dfff */ 
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active below version 2.1.8 */ {
  transform: translateX(-100%);
  opacity: 0;
}


/* #loader>div {
    transform: translateX(-50%);
} */
</style>