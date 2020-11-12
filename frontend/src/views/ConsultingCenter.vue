<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="pt-7 col-lg-5 col-md-5 col-sm-12 col-xs-12">
          <v-card> <div id="map" style="height: 365px; width: 100%" /> </v-card><br />
          <v-btn color="#262272" style="color:white; border-radius:30px;" small @click="goNowLocation()"> <v-icon small dark>mdi-map-marker</v-icon>내 위치로 이동</v-btn>
        </v-col>
        <v-col class="pt-0" align="center">
          <v-row class="d-none d-sm-flex">
            <v-select
              :items="regions"
              item-text="region"
              item-value="region"
              label="region"
              solo
              class="mt-7"
              v-model="word"
            ></v-select>
            <div style="width:100%;">
            <v-card>
              <v-simple-table>
                <template v-slot:default>
                  <thead>
                    <tr>
                      <th class="text-center">기관명</th>
                      <th class="text-center">전화번호</th>
                      <th class="text-center">주소</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="item in pagingList" :key="item.address">
                      <td class="text-center">
                        <v-btn
                        text
                          small
                          @click="goCenter(item)"
                          style="font-size: 0.9rem;color:#262272; font-weight:bold;"
                        >
                          {{ item.name }}
                        </v-btn>
                      </td>
                      <td class="text-center">
                        {{ item.phone }}
                      </td>
                      <td class="text-center">
                        {{ item.address }}
                      </td>
                    </tr>
                  </tbody>
                </template>
              </v-simple-table>
            </v-card>
            </div>
          </v-row>

          <v-row class="d-flex d-sm-none">
            <v-select
              :items="regions"
              item-text="region"
              item-value="region"
              label="region"
              solo
              class="mt-7"
              v-model="word"
            ></v-select>
            <v-simple-table style="width:500px;">
                <template v-slot:default>
                  <thead>
                    <tr>
                      <th class="text-center">기관 정보</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="item in pagingList" :key="item.address">
                      <td class="text-center" style="font-size: 0.7rem;">
                        <v-btn
                          small
                          @click="goCenter(item)"
                          style="color:white;"
                          color="#262272"
                          class="my-2"
                        >
                          {{ item.name }}
                        </v-btn>
                        <p class="my-2">{{ item.phone }}</p>
                        <p class="my-2">{{ item.address }}</p>
                      </td>
                    </tr>
                  </tbody>
                </template>
              </v-simple-table>
          </v-row>
          
          <v-row>
            <v-col>
              <v-pagination
                v-model="page"
                :length="pageLength"
                circle
                class="pb-3"
                color="#262272"
                :total-visible="7"
              ></v-pagination>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import http from "@/util/http-common.js";

export default {
  name: "consultingCenter",
  components: {
    // KakaoMap
  },
  data() {
    return {
      centerList: [],
      pagingList: [],
      pageLength: 0,
      page: 1,
      // markerImg:
      //   "https://blog.kakaocdn.net/dn/pe3Gt/btqKibc7VPl/JMG0zmTTAZuBAegMSA2c9k/img.png",
      markerImg:
        "https://ifh.cc/g/JkmS8N.png",
      kakaomap: "",
      markers: [],
      word: "전체",
      selectedRegion: {
        region: "default",
        lat: 37.867524,
        lon: 128.266861,
        scale: 3,
      },
      regions: [
        { region: "전체", lat: 36.341757, lon: 127.390007, scale: 7 },
        { region: "서울특별시", lat: 37.554891, lon: 126.983136, scale: 8 },
        { region: "경기도", lat: 37.554891, lon: 126.983136, scale: 9 },
        { region: "인천광역시", lat: 37.454423, lon: 126.701428, scale: 8 },
        { region: "부산광역시", lat: 35.174473, lon: 129.060118, scale: 8 },
        { region: "광주광역시", lat: 35.150194, lon: 126.826661, scale: 8 },
        { region: "대전광역시", lat: 36.341757, lon: 127.390007, scale: 7 },
        { region: "강원도", lat: 37.867524, lon: 128.266861, scale: 10 },
        { region: "경상남도", lat: 35.488857, lon: 128.22618, scale: 10 },
        { region: "경상북도", lat: 36.294186, lon: 128.88356, scale: 10 },
        { region: "전라북도", lat: 35.719286, lon: 127.1503, scale: 10 },
        { region: "전라남도", lat: 34.909267, lon: 126.920502, scale: 10 },
        { region: "충청남도", lat: 36.647572, lon: 126.806547, scale: 10 },
        { region: "충청북도", lat: 36.773015, lon: 127.639614, scale: 10 },
        { region: "제주특별자치도", lat: 33.36884, lon: 126.553079, scale: 9 },
      ],
    };
  },
  created() {
    http.get(`/center/getCenter/${this.word}`).then((res) => {
      this.centerList = res.data;
      if (window.kakao && window.kakao.maps) {
        this.initMap();
      } else {
        const script = document.createElement("script");
        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap);
        script.src =
          "https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4e99ee62c46f9d78ea96e3c4785fc69a";
        document.head.appendChild(script);
      }

      this.pagingList = this.centerList.slice(0, 5);
      if (this.centerList.length % 5 == 0) {
        this.pageLength = this.centerList.length / 5;
      } else {
        this.pageLength = parseInt(this.centerList.length / 5) + 1;
      }
    });
  },
  methods: {
    initMap() {
      var container = document.getElementById("map");
      var options = {
        center: new kakao.maps.LatLng(36.31688206, 127.3736809),
        level: 3,
      };

      this.kakaomap = new kakao.maps.Map(container, options);

      for (var i = 0; i < this.centerList.length; i++) {
        // 마커 이미지를 생성합니다
        var imageSrc = this.markerImg;
        // 마커 이미지의 이미지 크기 입니다
        var imageSize = new kakao.maps.Size(35, 35);
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
        // 마커를 생성합니다
        var latlng = new kakao.maps.LatLng(
          this.centerList[i].lon,
          this.centerList[i].lat
        );
        var marker = new kakao.maps.Marker({
          map: this.kakaomap, // 마커를 표시할 지도
          position: latlng, // 마커를 표시할 위치
          title: this.centerList[i].name, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          image: markerImage, // 마커 이미지
        });

        // this.markers.push(marker);
        var infowindow = new kakao.maps.InfoWindow({
          content:
            `<div fill style="width:230px" align="center">` +
            this.centerList[i].name +
            `</div>`, // 인포윈도우에 표시할 내용
        });

        // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
        kakao.maps.event.addListener(
          marker,
          "mouseover",
          makeOverListener(this.kakaomap, marker, infowindow)
        );
        kakao.maps.event.addListener(
          marker,
          "mouseout",
          makeOutListener(infowindow)
        );
      }

      function makeOverListener(map, marker, infowindow) {
        return function () {
          infowindow.open(map, marker);
        };
      }

      // 인포윈도우를 닫는 클로저를 만드는 함수입니다
      function makeOutListener(infowindow) {
        return function () {
          infowindow.close();
        };
      }
    },
    onSubmit() {
      for (var i in this.regions) {
        if (this.regions[i].region == this.word) {
          this.selectedRegion = this.regions[i];
          break;
        }
      }
      http.get(`center/getCenter/${this.selectedRegion.region}`).then((res) => {
        this.kakaomap.setCenter(
          new kakao.maps.LatLng(
            this.selectedRegion.lat,
            this.selectedRegion.lon
          )
        );
        this.kakaomap.setLevel(this.selectedRegion.scale);
        this.centerList = res.data;
        this.pagingList = this.centerList.slice(0, 5);
        if (this.centerList.length % 5 == 0) {
          this.pageLength = this.centerList.length / 5;
        } else {
          this.pageLength = parseInt(this.centerList.length / 5) + 1;
        }
      });
    },
    goCenter(item) {
      this.kakaomap.setCenter(new kakao.maps.LatLng(item.lon, item.lat));
      this.kakaomap.setLevel(3);
    },
    goNowLocation() {
      var kakaomap = this.kakaomap;
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

          var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
            message = '<div style="padding:5px;">현재위치 입니다!</div>'; // 인포윈도우에 표시될 내용입니다
          kakaomap.setLevel(3);
          var marker = new kakao.maps.Marker({
            map: kakaomap,
            position: locPosition,
          });

          var iwContent = message, // 인포윈도우에 표시할 내용
            iwRemoveable = true;

          // 인포윈도우를 생성합니다
          var infowindow = new kakao.maps.InfoWindow({
            content: iwContent,
            removable: iwRemoveable,
          });

          // 인포윈도우를 마커위에 표시합니다
          infowindow.open(kakaomap, marker);

          // 지도 중심좌표를 접속위치로 변경합니다
          kakaomap.setCenter(locPosition);
        });
      } else {
        // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
        var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
          message = "geolocation을 사용할수 없어요..";
        var marker = new kakao.maps.Marker({
          map: this.kakaomap,
          position: locPosition,
        });

        var iwContent = message, // 인포윈도우에 표시할 내용
          iwRemoveable = true;

        // 인포윈도우를 생성합니다
        var infowindow = new kakao.maps.InfoWindow({
          content: iwContent,
          removable: iwRemoveable,
        });

        // 인포윈도우를 마커위에 표시합니다
        infowindow.open(this.kakaomap, marker);

        // 지도 중심좌표를 접속위치로 변경합니다
        this.kakaomap.setCenter(locPosition);
      }
    },
  },
  watch: {
    page(page) {
      var first = (page - 1) * 5;
      this.pagingList = this.centerList.slice(first, first + 5);
    },
    word() {
      this.onSubmit();
    },
  },
};
</script>
