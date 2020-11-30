import axios from "axios";

// axios 객체 생성
export default axios.create({
    // baseURL: "http://localhost:8080/api",
    baseURL: "https://k3b202.p.ssafy.io:8080/api",
    headers: {
        "Content-type": "multipart/form-data"
    }
});