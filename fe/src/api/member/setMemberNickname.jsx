import { apiInstance } from "..";

const api = apiInstance();

export default async function setMemberNickname(nickname) {
    return await api.put("/auth/member/UpdateMemberNickname", null, { params: { "nickName": nickname } });
}
