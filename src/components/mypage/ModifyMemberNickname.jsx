import { useEffect, useState } from 'react'
import styles from './ModifyMemberNickname.module.css'

export default function ModifyMemberNickname({ isEdit, originNickname, nickname, setNickname, isValid, setIsValid }) {
    const [validationMessage, setValidationMessage] = useState("");
    // const [isValid, setIsValid] = useState(true);

    function validateNickname(nickname, originNickname) {
        const koreanNicknameRegex = /^[가-힣0-9\s]+$/;
        const englishNicknameRegex = /^[A-Za-z0-9._\s]+$/;
        const mixedLangRegex = /(?=.*[가-힣])(?=.*[A-Za-z])/;
        const invalidCharRegex = /[^A-Za-z가-힣0-9._\s]/;
        const KORMAXLEN = 8;
        const ENGMAXLEN = 15;

        if (nickname === "") {
            return ["", false];
        } else if (nickname === originNickname) {
            return ["", true];
        } else if (mixedLangRegex.test(nickname)) {
            return ["한글과 영어는 혼용할 수 없습니다.", false];
        } else if (invalidCharRegex.test(nickname)) {
            return ["한글, 영어, 숫자, ., _ 이외의 문자는 사용할 수 없습니다.", false];
        } else if (/[가-힣]/.test(nickname)) {
            if (!koreanNicknameRegex.test(nickname)) {
                return ["올바르지 않은 한글 닉네임입니다.", false];
            } else if (nickname.length > KORMAXLEN) {
                return ["한글 닉네임의 최대 길이 8자를 초과하였습니다.", false];
            } else {
                return ["사용 가능한 형식의 닉네임입니다ㄴㅇ.", true];
            }
        } else {
            if (!englishNicknameRegex.test(nickname)) {
                return ["올바르지 않은 영어 닉네임입니다.", false];
            } else if (nickname.length > ENGMAXLEN) {
                return ["영어 닉네임의 최대 길이 15자를 초과하였습니다.", false];
            } else {
                return ["사용 가능한 형식의 닉네임입니다.", true];
            }
        }
    }

    useEffect(() => {
        console.log(isEdit)
    }, [isEdit])
    
    useEffect(() => {
        const [message, valid] = validateNickname(nickname, originNickname);
        setValidationMessage(message);
        setIsValid(valid);
    }, [nickname, originNickname])

    return (
        <div className={validationMessage === "" ? styles.container_50px : styles.container_36px}>
            {!isEdit ?
                <input type="text"
                className={styles.nicknameInput}
                value={nickname}
                    disabled 
                placeholder="닉네임을 입력하세요" />
                :
                <input type="text"
                className={styles.nicknameInput}
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                placeholder="닉네임을 입력하세요" />
            }
            <div className={isValid ? styles.subTextTrue : styles.subTextFalse}>{validationMessage}</div>
        </div>
    )
}