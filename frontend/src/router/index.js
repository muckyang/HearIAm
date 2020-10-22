import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import UserWRTC from '../views/webRTC/UserWRTC.vue'
import CounselorWRTC from '../views/webRTC/CounselorWRTC.vue'
import LiveList from '../views/webRTC/LiveList.vue'
import Emotion from '../views/emotion/EmotionTest.vue'
import Record from '../views/videoRecord/Record.vue'
import RecordConsult from '../views/consult/RecordConsult.vue'

Vue.use(VueRouter)

const routes = [{
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () =>
            import ( /* webpackChunkName: "about" */ '../views/About.vue')
    },
    {
        path: '/userWRTC',
        name: 'UserWRTC',
        component: UserWRTC
    },
    {
        path: '/counselorWRTC/:room&:num',
        name: 'CounselorWRTC',
        component: CounselorWRTC
    },
    {
        path: '/liveList',
        name: 'LiveList',
        component: LiveList
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
        path: '/recordConsult',
        name: 'RecordConsult',
        component: RecordConsult
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router