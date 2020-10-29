<template>
    <div>
         <v-btn
        large
        elevation="4"
        @click="subscribe()"
        class="mt-5"
        style="
          font-weight: bold;
          height: 4rem;
          border-radius: 20px;
          font-size: 1.2rem;
          background: linear-gradient(to right, #93dfff, #f5a2bb);
        "
        >구독하기\상담사\1대1 신청</v-btn>
         <v-btn
        large
        elevation="4"
        @click="unsubscribe()"
        class="mt-5"
        style="
          font-weight: bold;
          height: 4rem;
          border-radius: 20px;
          font-size: 1.2rem;
          background: linear-gradient(to right, #93dfff, #f5a2bb);
        "
        >구독하기\상담사\취소</v-btn>
        <v-btn
        large
        elevation="4"
        @click="newPostPush()"
        class="mt-5"
        style="
          font-weight: bold;
          height: 4rem;
          border-radius: 20px;
          font-size: 1.2rem;
          background: linear-gradient(to right, #93dfff, #f5a2bb);
        "
        >새포스트\학생\신청함</v-btn>
    </div>
</template>
<script>
import axios from "axios";
export default {
    data(){
        return {
            url : "https://fcm.googleapis.com/fcm/send",
            token: "dgxJR0fN6xK_Ge5uwmmj11:APA91bEMSt0M_BpXMA2fXPmOU0mTGKhK4rr6b6o5EszQxQozzQr3RAMUCAIYQIYNWFTmj3b5K7wGeFR_l1hsGfiRbgdN4BDyHukOG1z_f__jJs3NWt4OWRJdjukMD6aKZlXHJCwCN78M",
        }
    },
    methods:{
        unsubscribe(){
            console.log("click unsubscribe btn");
            let topic = "testtopic"
            this.unsubscribeTokenToTopic(this.token, topic);
        },
        unsubscribeTokenToTopic(token, topic){
            axios({
                method: 'POST',
                url: 'https://iid.googleapis.com/iid/v1:batchRemove',
                data:{
                    "to":"/topics/"+topic,
                    "registration_tokens":[token]
                },
                headers: {
                    "Content-type": "application/json",
                    "Authorization" : "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd"
                 }
            })
            .then(response => {
                if (response.status < 200 || response.status >= 400) {
                    throw 'Error subscribing to topic: '+response.status + ' - ' + response.text();
                }
                console.log("unsubscribe success : "+response);
            }).catch(e =>{
                console.log(e);
            })
        },
        subscribe(){
            console.log("click subscribe btn");
            let topic = "testtopic"
            this.subscribeTokenToTopic(this.token, topic);
        },
        subscribeTokenToTopic(token, topic){
            axios({
                method: 'POST',
                url: 'https://iid.googleapis.com/iid/v1/'+token+'/rel/topics/'+topic,
                headers: {
                    "Authorization" : "key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd"
                 }
            })
            .then(response => {
                if (response.status < 200 || response.status >= 400) {
                    throw 'Error subscribing to topic: '+response.status + ' - ' + response.text();
                }
                console.log("subscribe success : "+response);
            }).catch(e =>{
                console.log(e);
            })
        },
        newPostPush(){
            // const message = {
            //     data :{
            //         body : "새 포스트가 등록되었습니다!.",
            //         title: "nowpostpush",
            //         icon: "favicon.ico"
            //     },
            //     to : "/topics/testtopic"
            // }
            // const config = {
            //     headers:{
            //         'Content-type': 'application/json',
            //         'Accept':'application/json',
            //         'Authorization' : 'key=AAAAEDiSbms:APA91bH-uXikdH1nixzEB2RRH5dMl14_rotnU1ujpcU7Ii6dW-oaV4N_Q6Uh_TvHzumQzllUui2-E4ZdcShX2upbC52FaNAaxxVxjnwnqxcel4RgNYPp_uzWmKNe5OblH2aRX5NWZbcd'
            //     }
            // }
            // axios.post(this.url , message, config)
            // .then(response => {
            //     if (response.status < 200 || response.status >= 400) {
            //         throw 'Error subscribing to topic: '+response.status + ' - ' + response.text();
            //     }
            //     console.log("nowpostpush success : "+response);
            //     console.log("nowpostpush success : "+response.rel);
            // }).catch(e =>{
            //     console.log(e);
            // })
        }
    }
}
</script>