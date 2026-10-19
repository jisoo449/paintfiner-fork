/** useAuthToken.jsx
 * accessToken, refrehToken값을 가져와 각각 메모리, 쿠키에 저장
 * 
 */
import { useEffect } from "react";
import { setCookie, getCookie, removeCookie } from "../utils/cookie";
import { useRecoilState, useResetRecoilState } from "recoil";
import { accessTokenState } from "../states/accessTokenAtom";
import { userState } from "../states/userAtom";
import getAccessToken from "../api/member/getAccessToken";
import { useMemberApi } from "../api/useMemberApi";

const useAuthToken = () => {
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [user, setUser] = useRecoilState(userState);
    const removeAccessToken = useResetRecoilState(accessTokenState);
    const removeUser = useResetRecoilState(userState);
    const refreshToken = getCookie('refreshToken');
    const {getUser} = useMemberApi();
    // const {getUser, getLogout} = useMemberApi();

    //refresh token으로 access token 갱신하기
    const refreshAccessToken = async () => {
        
        await getAccessToken().then(res=>{
            setToken(res.data.accessToken, res.data.refreshToken);
        })
    }

    //access token으로 사용자 정보 가져오기
    const fetchUser = async () => {
        await getUser().then(res=>{ 
            const resEmail=res.data.email;
            const resNickname = res.data.nickname;
            setUser({state:true,
                    email: resEmail,
                    nickname: resNickname});
            })
    }

    // 로그아웃 - 서버에 로그아웃 통신을 보내고(<-일단 삭제), 저장된 리코일, 쿠키를 모두 삭제
    const logout = async () => {
        removeAccessToken();
        removeCookie('refreshToken');
        removeUser();
    }

    useEffect(()=>{
        if (accessToken)
            fetchUser();

        // else if (refreshToken)
        //     refreshAccessToken();
        
    }, [accessToken, refreshToken])

    //access token, refresh token, 사용자 정보 저장
    const setToken = (accessToken, refreshToken) =>{
        setAccessToken(accessToken);
        setCookie('refreshToken', refreshToken, { expires: new Date(Date.now() + 86400 * 1000) });
    }

    return {accessToken, setAccessToken, user, refreshToken, refreshAccessToken, fetchUser, setToken, logout};
}

export default useAuthToken;