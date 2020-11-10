const state = {   
    deviceID: localStorage.getItem("deviceID") || "",
    isRemote : false,
    isready : localStorage.getItem("isready") || false,
    alarmList : localStorage.getItem("alarmList") || [],
    alarmBtn : localStorage.getItem("alarmBtn") || "#49358b",
 };

const getters = {  
    getDeviceID : state => state.deviceID,
    getIsRemote : state => state.isRemote,
    getIsReady : state => state.isready, 
    getAlarmList : state => state.alarmList,
    getAlarmBtn : state => state.alarmBtn,
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
    },
    changeAlarmBtn : (state, data)=>{
        state.alarmBtn = data;
    },
    setAlarmList : (state, data )=>{
        state.alarmList = data;
    }

};

export default {
    state,
    getters,
    actions,
    mutations
};
