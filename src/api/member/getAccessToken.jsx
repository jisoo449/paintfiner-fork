import { apiInstance } from "..";

const api = apiInstance();

export default async function getAccessToken(success, fail){
    return await api.get("/auth/member/refreshToken").then(success).catch(fail);
}