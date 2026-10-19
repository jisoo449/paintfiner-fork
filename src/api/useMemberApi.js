/** useMemberApi.jsx
 * 사용자 인가와 관련된 api 요청 커스텀 훅
 * 
 */
// import { useRecoilState, useRecoilValue, useResetRecoilState } from "recoil";
import { useRecoilValue } from "recoil";
import { apiInstance } from ".";
import { setupInterceptors } from "./setupInterceptors ";
import { accessTokenState } from "../states/accessTokenAtom";

export const useMemberApi = () => {
    const accessToken = useRecoilValue(accessTokenState);
    const api = apiInstance();
    setupInterceptors(api,accessToken); // 인스턴스를 생성한 후 인터셉터 설정

    // 헤더에 access token을 넣어 사용자 정보 요청 보내기
    const getUser = async (success, fail) => {
        return await api.get("/auth/member/findMember").then(success).catch(fail);
    }

    // 로그아웃 요청 보내기
    // const getLogout = async (success, fail) => {
    //     return await api.post("/auth/member/logout").then(success).catch(fail)
    // }

    return {getUser}; //, getLogout};
}