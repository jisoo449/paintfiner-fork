import { KAKAO_AUTH_URL } from "../../constants/kakao_auth_url.constants";
import styles from "./Auth.module.css";

export default function OAuthJoinButton(){

    const onClickKakaoJoin = (e) => {
        window.location.href=KAKAO_AUTH_URL;
    }; 

    return(
        <div className={styles.oauthbtn_wrapper}>
            <img src="/button/kakao_join_medium_narrow.png" onClick={onClickKakaoJoin}/>
        </div>
    );
}