import { USER_REQUEST, USER_ERROR, USER_SUCCESS, USER_UPDATE } from "../actions/user";
import apiCall2 from "../../util/api2";
import Vue from "vue";
import { AUTH_LOGOUT } from "../actions/auth";

const state = {
    status: "",
    profilename: localStorage.getItem("profilename") || "",
    realname: localStorage.getItem("realname") || "",
    userNum: localStorage.getItem("userNum") || "",
    userID: localStorage.getItem("userID") || "",
    userBirth: localStorage.getItem("userBirth") || "",
};

const getters = {
    getProfile: state => state.profilename,
    isProfileLoaded: state => !!state.profilename,
    getRealName: state => state.realname,
    getUserNum: state => state.userNum,
    getUserID: state => state.userID,
    getUserBirth: state => state.userBirth,
};

const actions = {
    [USER_REQUEST]: ({ commit, dispatch }, args) => {
        apiCall2(args.userId)
            .then(resp => {
                commit(USER_SUCCESS, resp);
            })
            .catch(() => {
                commit(USER_ERROR);
                dispatch(AUTH_LOGOUT);
            });
    },
    [USER_UPDATE]: ({ commit }, resp) => {
        apiCall2(resp)
            .then(resp => {
                commit(USER_SUCCESS, resp);
            })
            .catch(() => {
                commit(USER_ERROR);
            });
    }
};

const mutations = {
    [USER_REQUEST]: state => {
        state.status = "loading";
    },
    [USER_SUCCESS]: (state, resp) => {
        state.status = "success";
        localStorage.setItem("profilename", resp.nickName);
        localStorage.setItem("realname", resp.name);
        localStorage.setItem("userNum", resp.num);
        localStorage.setItem("userID", resp.userId);
        localStorage.setItem("userBirth", resp.birth);
        Vue.set(state, "profilename", resp.nickName);
        Vue.set(state, "realname", resp.name);
        Vue.set(state, "userNum", resp.num);
        Vue.set(state, "userID", resp.userId);
        Vue.set(state, "userBirth", resp.birth);


    },
    [USER_ERROR]: state => {
        state.status = "error";
    },
    [AUTH_LOGOUT]: state => {
        state.profilename = "";
        state.realname = "";
        state.userNum = "";
        state.userID = "";
        state.userBirth = "";
    },
    modifyProfileName: function(state, after) {
        state.profilename = after;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};