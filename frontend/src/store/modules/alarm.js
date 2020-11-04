const state = {   
    deviceID: localStorage.getItem("deviceID") || "",
    isRemote : false,
    isready : localStorage.getItem("isready") || false,
    alarmCnt : localStorage.getItem("alarmCnt") || 0,
    alarmFlag : localStorage.getItem("alarmFlag") || false,
 };

const getters = {  
    getDeviceID : state => state.deviceID,
    getIsRemote : state => state.isRemote,
    getIsReady : state => state.isready, 
    getAlarmCnt : state => state.alarmCnt,
    getAlarmFlag : state => state.alarmFlag,
};

const actions = {   
};

const mutations = {  
    setDeviceId: (state, data)=>{
        state.deviceID = data;
    },
    changeIsRemote : (state,data)=>{
        state.isRemote = data;
    },
    changeIsReady : (state, data )=> {
        state.isready = data;
        localStorage.setItem("isready",data);
    },
    setAlarmCnt : (state,data )=>{
        state.alarmCnt = data;
    },
    setAlarmFlag : (state,data) =>{
        state.alarmFlag = data;
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};
