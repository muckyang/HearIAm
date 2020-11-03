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
import http from "@/util/http-common.js";

export default {
  name: 'consultingCenter',
  components: {
    // KakaoMap
  },    
  data (){
      return {
          centerList:[],
          markerImg : 'https://blog.kakaocdn.net/dn/pe3Gt/btqKibc7VPl/JMG0zmTTAZuBAegMSA2c9k/img.png',
            kakaomap : '',
            markers:[],

      }
  },
    created() {
        http.get('/center/getCenter').then((res) =>{
            this.centerList = res.data
            if (window.kakao && window.kakao.maps) {
                this.initMap();
            } else {
                const script = document.createElement('script');
                /* global kakao */
                script.onload = () => kakao.maps.load(this.initMap);
                script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4e99ee62c46f9d78ea96e3c4785fc69a';
                document.head.appendChild(script);
            }
        })

        
    },
    methods: {
        initMap() {
            var container = document.getElementById('map');
            var options = {
                center: new kakao.maps.LatLng(36.31688206, 127.3736809),
              level: 3
            };


            this.kakaomap = new kakao.maps.Map(container, options);
            console.log(this.centerList)
            for (var i = 0; i <  this.centerList.length; i ++) {
                // 마커 이미지를 생성합니다    
                var imageSrc = this.markerImg
                // 마커 이미지의 이미지 크기 입니다
                var imageSize = new kakao.maps.Size(55, 55); 
                var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
                // 마커를 생성합니다
                var latlng  = new kakao.maps.LatLng(this.centerList[i].lon, this.centerList[i].lat)
                var marker = new kakao.maps.Marker({
                    map: this.kakaomap, // 마커를 표시할 지도
                    position: latlng, // 마커를 표시할 위치
                    title : this.centerList[i].name, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                    image : markerImage // 마커 이미지 
                });

                // this.markers.push(marker);
                    var infowindow = new kakao.maps.InfoWindow({
                    content: `<div fill style="width:230px" align="center">`+this.centerList[i].name+`</div>`// 인포윈도우에 표시할 내용
                    });

                    // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
                    kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(this.kakaomap, marker, infowindow));
                    kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
            }

            function makeOverListener(map, marker, infowindow) {
                return function() {
                    infowindow.open(map, marker);
                };
            }

            // 인포윈도우를 닫는 클로저를 만드는 함수입니다 
            function makeOutListener(infowindow) {
                return function() {
                    infowindow.close();
                };
            }
        }
    }
}
</script>

