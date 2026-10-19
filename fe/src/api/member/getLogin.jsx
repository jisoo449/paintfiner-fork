/*
    추후 고도화 필요
    - 로그인 규칙 정하기
*/
import { apiInstance } from "../index";

const api = apiInstance();

export default async function getLogin(email, pw){
    await api.post("/member/login", {"email": email, "password": pw});//.then(success).catch(fail);
}