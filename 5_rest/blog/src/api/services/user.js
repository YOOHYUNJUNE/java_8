import api from '../api';
// .js는 생략가능

export const userAPI = {

    getUserList : () => api.get("/auth"),
    addUser : (data) => api.post("/auth", data),
    emailCheck : (email) => api.get("/auth/duplicate", {data:{email}}),
    modifyAdd : (data) => api.patch("/auth", data),
    deleteUser : (data) => api.delete(`/auth`, {data:{data}})
    

}



