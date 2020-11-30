import {
    AUTH_REQUEST,
    AUTH_ERROR,
    AUTH_SUCCESS,
    AUTH_LOGOUT,
    AUTH_REQUEST_SOCIAL,
} from "../actions/auth";
import { USER_REQUEST } from "../actions/user";
import apiCall from "../../util/api";

const state = {
    token: localStorage.getItem("user-token") || "",
    status: "",
    hasLoadedOnce: false
};

const getters = {
    isAuthenticated: state => !!state.token,
    authStatus: state => state.status
};
//   {accessToken: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNTcwM…XYOy7KZva2LCwHnDixyQHAIX1E6K3EzmA9WSMNJklkIVthjyQ", tokenType: "Bearer"}

const actions = {
    [AUTH_REQUEST]: ({ commit, dispatch }, user) => {
        return new Promise((resolve, reject) => {
            commit(AUTH_REQUEST);
            apiCall({ url: "auth", data: user, method: "POST" })
                .then(resp => {
                    // console.log("------------" + resp.accessToken)
                    localStorage.setItem("user-token", resp.accessToken);
                    commit(AUTH_SUCCESS, resp);
                    dispatch(USER_REQUEST, user);
                    resolve(resp);

                    // console.log("resp:", resp, "user", user)
                })
                .catch(err => {
                    commit(AUTH_ERROR, err);
                    localStorage.removeItem("user-token");
                    localStorage.removeItem("role");
                    localStorage.removeItem("qualification");
                    localStorage.removeItem("userName");
                    localStorage.removeItem("userNum");
                    localStorage.removeItem("userID");
                    reject(err);
                });
        });
    },
    [AUTH_REQUEST_SOCIAL]: ({ commit, dispatch }, resp) => {
        return new Promise((resolve) => {
            commit(AUTH_REQUEST);
            // console.log("------------"+resp.accessToken)
            localStorage.setItem("user-token", resp.token.accessToken);
            commit(AUTH_SUCCESS, resp.token);
            dispatch(USER_REQUEST, resp);
            resolve(resp.token);
            // console.log(resp.email, 11, resp.token)

        });
    },
    [AUTH_LOGOUT]: ({ commit }) => {
        return new Promise(resolve => {
            commit(AUTH_LOGOUT);
            localStorage.removeItem("user-token");
            localStorage.removeItem("role");
            localStorage.removeItem("qualification");
            localStorage.removeItem("userName");
            localStorage.removeItem("userNum");
            localStorage.removeItem("userID");
            localStorage.removeItem("deviceID");            
            localStorage.removeItem("isready");            
            localStorage.removeItem("alarmCnt");
            resolve();
        });
    }
};

const mutations = {
    [AUTH_REQUEST]: state => {
        state.status = "loading";
    },
    [AUTH_REQUEST_SOCIAL]: state => {
        state.status = "loading";
    },
    [AUTH_SUCCESS]: (state, resp) => {
        state.status = "success";
        state.token = resp.accessToken;
        state.hasLoadedOnce = true;
    },
    [AUTH_ERROR]: state => {
        state.status = "error";
        state.hasLoadedOnce = true;
    },
    [AUTH_LOGOUT]: state => {
        state.token = "";
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};