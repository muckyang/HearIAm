import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import UserWRTC from '../views/webRTC/UserWRTC.vue'
import CounselorWRTC from '../views/webRTC/CounselorWRTC.vue'
import Emotion from '../views/emotion/EmotionTest.vue'
import Record from '../views/videoRecord/Record.vue'
import RecordConsult from '../views/consult/RecordConsult.vue'
import ReserveMain from '../views/reserve/ReserveMain.vue'
import MenteeMypage from '../views/mentee/MenteeMypage.vue'
import MentorMypage from '../views/mentor/MentorMypage.vue'
import MyMenteeList from '../views/mentor/MyMenteeList.vue'
import MyMenteeInfo from '../views/mentor/MyMenteeInfo.vue'
import ConsultingCenter from '../views/ConsultingCenter.vue'
import CounselingInfo from '../views/mentor/CounselingInfo.vue'
import AudioRecorder from 'vue-audio-recorder'
import RecordList from '../views/consult/RecordList.vue'
import RecordDetail from '../views/consult/RecordDetail.vue'
Vue.use(AudioRecorder)
Vue.use(VueRouter)

const routes = [{
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/userWRTC',
        name: 'UserWRTC',
        component: UserWRTC
    },
    {
        path: '/userWRTC/:room',
        name: 'UserWRTC',
        component: UserWRTC
    },
    {
        path: '/counselorWRTC/:room&:num',
        name: 'CounselorWRTC',
        component: CounselorWRTC
    },
    {
        path: '/emotion',
        name: 'Emotion',
        component: Emotion
    },
    {
        path: '/record',
        name: 'Record',
        component: Record
    },
    {
        path: '/recordConsult/:num',
        name: 'RecordConsult',
        component: RecordConsult
    },
    {
        path: '/reserveMain',
        name: 'ReserveMain',
        component: ReserveMain
    },
    {
        path: '/menteeMain',
        name: 'menteeMain',
        component: () =>
            import ('@/views/mentee/MenteeMain.vue')
    },
    {
        path: '/mentorMain',
        name: 'mentorMain',
        component: () =>
            import ('@/views/mentor/MentorMain.vue')
    },
    {
        path: '/menteeMypage',
        name: 'MenteeMypage',
        component: MenteeMypage
    },
    {
        path: '/mentorMypage',
        name: 'MentorMypage',
        component: MentorMypage
    },
    {
        path: '/myMenteeList',
        name: 'MyMenteeList',
        component: MyMenteeList
    },
    {
        path: '/myMenteeInfo/:num&:name',
        name: 'MyMenteeInfo',
        component: MyMenteeInfo
    },
    {
        path: '/counselingInfo/:num',
        name: 'CounselingInfo',
        component: CounselingInfo
    },
    {
        path: '/recordList',
        name: 'RecordList',
        component: RecordList
    },
    {
        path: '/recordDetail/:num',
        name: 'RecordDetail',
        component: RecordDetail
    },
    {
        path: '/consultingCenter',
        name: 'ConsultingCenter',
        component: ConsultingCenter
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router