import styles from "./Auth.module.css";
import { KAKAO_AUTH_URL } from "../../constants/kakao_auth_url.constants";

export default function OAuthLoginButton(){

    const onClickKakaoLogin = (e) => {
        window.location.href=KAKAO_AUTH_URL;
    }

    return(
        <div className={styles.oauthbtn_wrapper}>
            <img src="/button/kakao_login_medium_narrow.png" onClick={onClickKakaoLogin}/>
        </div>
    )
}