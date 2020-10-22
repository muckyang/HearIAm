import axios from "axios";
const apiCall2 = usertoken =>
    new Promise((resolve, reject) => {
        // console.log(usertoken)
        //data: {usernameOrEmail: "cccccc", password: "cccccc"}
        //axios.get('http://192.168.28.129:8397/api/users/'+usertoken).then(({data}) => {
        axios
            // .get("http://localhost:8080/api/user/users/" + usertoken)
            .get("https://j3b108.p.ssafy.io:8080/api/user/users/" + usertoken)
            .then(({ data }) => {
                //console.log(data)
                resolve(data);
            })
            .catch(error => {
                //reject(new Error(error))
                reject(error);
            });
    });

export default apiCall2;