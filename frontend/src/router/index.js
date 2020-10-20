import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import WebRTC from '../views/webRTC/WebRTC.vue'
import Emotion from '../views/emotion/EmotionTest.vue'
import Record from '../views/videoRecord/Record.vue'

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
        path: '/webRTC',
        name: 'WebRTC',
        component: WebRTC
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
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router