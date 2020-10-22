import axios from "axios";

export default axios.create({
    // baseURL: "http://localhost:8080/api/auth",
    baseURL: "http://k3b202.p.ssafy.io:8080/api/auth",
    headers: {
        "Content-type": "application/json"
    }
});