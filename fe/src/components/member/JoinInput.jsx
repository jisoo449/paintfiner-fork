/*
 추후 수정 및 고도화 필요
 - 이메일 중복 확인 API
 - 닉네임 중복 확인 API
 - 비밀번호 확인 함수
 - 비밀번호 안보이게 가리기
*/
import { useState } from "react";
import styles from "./Auth.module.css";
import { useNavigate } from "react-router-dom";
import setMember from "../../api/member/setMember";

export default function JoinInput(){

    const navigate = useNavigate();

    const [nickname, setNickname] = useState("");
    const [email, setEmail] = useState("");
    const [pw, setPw] = useState("");
    const [confirmPw, setConfirmPw] = useState("");

    const handleInputEmail = (e) => setEmail(e.target.value);
    const handleInputPw = (e) => setPw(e.target.value);
    const handleInputNickname = (e) => setNickname(e.target.value);
    const handleInputconfirmPw = (e) => setConfirmPw(e.target.value);

    const onClickJoin = (e) => {
        setMember().then(navigate("/")).catch((error)=>{throw new Error(error)});
    }

    return (
        <div className={styles.input_wrapper}>
            <div className={styles.input_cell}>
                <label htmlFor="input_email" className={styles.label}>Email:&nbsp;</label>
                <input type="text" name="input_email" value={email} onChange={handleInputEmail} className={styles.input}/>
            </div>
            <div className={styles.input_cell}>
                <label htmlFor="input_nickname" className={styles.label}>Nickname:&nbsp;</label>
                <input type="text" name="input_nickname" value={nickname} onChange={handleInputNickname} className={styles.input}/>
            </div>
            <div className={styles.input_cell}>
                <label htmlFor="input_pw" className={styles.label}>PW:&nbsp;</label>
                <input type="text" name="input_pw" value={pw} onChange={handleInputPw} className={styles.input}/>
            </div>
            <div className={styles.input_cell}>
                <label htmlFor="input_confirmPw" className={styles.label}>Confirm PW:&nbsp;</label>
                <input type="text" name="input_confirmPw" value={confirmPw} onChange={handleInputconfirmPw} className={styles.input}/>
            </div>
            <button type="button" onClick={onClickJoin} className={styles.authBtn}>Join in</button>
        </div>
    );
}