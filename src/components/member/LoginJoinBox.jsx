import styles from "./Auth.module.css";
import OAuthLoginButton from "./OAuthLoginButton";
import OAuthJoinButton from "./OAuthJoinButton";
import LoginInput from "./LoginInput";
import JoinInput from "./JoinInput";

export default function LoginJoinBox({actionType}){

    return (
        <div className={styles.box}>
            <h2>{actionType}</h2>
            {/* {actionType=="Login" && (<LoginInput/>)}
            {actionType=="Join" && (<JoinInput/>)} */}
            {actionType=="Login" && (<OAuthLoginButton/>)}
            {actionType=="Join" && (<OAuthJoinButton/>)}
            <h2>================================</h2>
            <div className={styles.authOptions}>
                {actionType=="Login" && (<a href="/join">&gt;&gt; 회원가입 페이지로 이동</a>)}
                {actionType=="Join" && (<a href="/login">&gt;&gt; 로그인 페이지로 이동</a>)}
                {/* <a>&gt;&gt; ID 찾기</a>
                <a>&gt;&gt; 비밀번호 찾기</a> */}
            </div>
        </div>
    );
}