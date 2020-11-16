import { USER_REQUEST, USER_ERROR, USER_SUCCESS, USER_UPDATE } from "../actions/user";
import apiCall2 from "../../util/api2";
import Vue from "vue";
import { AUTH_LOGOUT } from "../actions/auth";

const state = {
    status: "",
    role: localStorage.getItem("role") || "",
    qualification: localStorage.getItem("qualification") || "",
    userName: localStorage.getItem("userName") || "",
    userNum: localStorage.getItem("userNum") || "",
    userID: localStorage.getItem("userID") || "",
 };

const getters = {
    isProfileLoaded: state => !!state.userName,
    getRole: state => state.role,
    getQualification: state => state.qualification,
    getUserName: state => state.userName,
    getUserNum: state => state.userNum,
    getUserID: state => state.userID,
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
        localStorage.setItem("role", resp.role);
        localStorage.setItem("qualification", resp.qualification);
        localStorage.setItem("userName", resp.name);
        localStorage.setItem("userNum", resp.num);
        localStorage.setItem("userID", resp.id);
        localStorage.setItem("deviceID",state.deviceID);
        Vue.set(state, "role", resp.role);
        Vue.set(state, "qualification", resp.qualification);
        Vue.set(state, "userName", resp.name);
        Vue.set(state, "userNum", resp.num);
        Vue.set(state, "userID", resp.id);
    },
    [USER_ERROR]: state => {
        state.status = "error";
    },
    [AUTH_LOGOUT]: state => {
        state.role = "";
        state.qualification = "";
        state.userName = "";
        state.userNum = "";
        state.userID = "";
        state.deviceID="";
        state.isready=false;
    },
    modifyProfileName: function(state, after) {
        state.userName = after;
    },
};

export default {
    state,
    getters,
    actions,
    mutations
};
