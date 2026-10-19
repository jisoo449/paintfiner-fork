import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import getJwtFromOauth from "../api/member/getJWTFromOAuth";
import useAuthToken from "../hooks/useAuthToken";

export default function LoginHandler() {
  const navigate = useNavigate();
  const code = new URL(window.location.href).searchParams.get("code");
  const { setToken } = useAuthToken();

  // 소셜 로그인 토큰으로 서버에 access token, refresh token 요청
  const fetchData = async (code) => {
    await getJwtFromOauth(code)
    .then(res=>{                                                      //요청 성공 시 useAuthToken에서 setToken 메소드 불러와 토큰 저장
      setToken(res.data.accessToken, res.data.refreshToken)
    })
    .catch(error=>console.log("LoginHandler Error: ", error))         // 요청 실패 시 에러 출력
  }

  useEffect(() => {
    fetchData(code);
    navigate('/');
  }, []);

  return (
    <div>
      <div>
        <p>로그인 중입니다.</p>
        <p>잠시만 기다려주세요.</p>
      </div>
    </div>
  );
}
