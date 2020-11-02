<template>
  <div>
     <v-sheet elevation="3" color="white" class="ml-3 mr-9 mt-7">
        <v-avatar
          class="elevation-4 mb-1 mr-2"
          color="blue lighten-1"
        >
          <v-icon dark>mdi-map</v-icon>
        </v-avatar>
        Map
        <v-sheet>
          <div id="map" style="height:700px;" />
        </v-sheet>
      </v-sheet>
  </div>
</template>

<script>

export default {
  name: 'consultingCenter',
  components: {
    // KakaoMap
  },    
  data (){
      return {
      
      }
  },
    mounted() {
        if (window.kakao && window.kakao.maps) {
            this.initMap();
        } else {
            const script = document.createElement('script');
            /* global kakao */
            script.onload = () => kakao.maps.load(this.initMap);
            script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4e99ee62c46f9d78ea96e3c4785fc69a';
            document.head.appendChild(script);
        }
    },
    methods: {
        initMap() {
            var container = document.getElementById('map');
            var options = {
              center: new kakao.maps.LatLng(33.450701, 126.570667),
              level: 3
            };

            var map = new kakao.maps.Map(container, options);
            var markerPosition  = new kakao.maps.LatLng(33.450701,126.570667); 
            var marker = new kakao.maps.Marker({
                 position: markerPosition
            });
            marker.setMap(map);
        }
    }

}
</script>

