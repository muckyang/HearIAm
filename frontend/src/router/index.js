import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import UserWRTC from '../views/webRTC/UserWRTC.vue'
import CounselorWRTC from '../views/webRTC/CounselorWRTC.vue'
import Emotion from '../views/emotion/EmotionTest.vue'

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
        path: '/counselorWRTC',
        name: 'CounselorWRTC',
        component: CounselorWRTC
    },
    {
        path: '/emotion',
        name: 'Emotion',
        component: Emotion
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router