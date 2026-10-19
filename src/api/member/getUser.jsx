import { apiInstance } from "..";

export default async function getUser(success, fail){
    const api = apiInstance();
    
    return await api.get("/user/getUser").then(success).catch(fail);
}