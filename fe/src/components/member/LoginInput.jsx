/*
    추후 고도화 필요
        - 비밀번호 안보이게 가리기
*/
import { useState } from "react";
import styles from "./Auth.module.css";
import { useNavigate } from "react-router-dom";
import getLogin from "../../api/member/getLogin";

export default function LoginInput(){

    const navigate = useNavigate();
    
    const [email, setEmail] = useState("");
    const [pw, setPw] = useState("");

    const handleInputEmail = (e) => setEmail(e.target.value);
    const handleInputPw = (e) => setPw(e.target.value);

    const onClickLogin = (e) => {
       getLogin(email, pw).then(navigate("/"));//.catch(fail);
    }

    return(
        <div className={styles.input_wrapper}>
            <div className={styles.input_cell}>
                <label htmlFor="input_email" className={styles.label}>ID:{" "}</label>
                <input type="text" name="input_email" value={email} onChange={handleInputEmail} className={styles.input}/>
            </div>
            <div className={styles.input_cell}>
                <label htmlFor="input_pw" className={styles.label}> PW:{" "} </label>
                <input type="password" name="input_pw" value={pw} onChange={handleInputPw} className={styles.input}/>
            </div>
            <button type="button" onClick={onClickLogin} className={styles.authBtn}>Login</button>
        </div>
    );
}