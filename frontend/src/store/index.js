import Vue from 'vue'
import Vuex from 'vuex'
import VueRecord from '../components/Record/index'
import user from "./modules/user";
import auth from "./modules/auth";
import alarm from "./modules/alarm";

Vue.config.productionTip = false
Vue.use(VueRecord)
Vue.use(Vuex)

const debug = process.env.NODE_ENV !== "production";
export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules: {
        user,
        auth,
        alarm,
    },
    strict: debug
})