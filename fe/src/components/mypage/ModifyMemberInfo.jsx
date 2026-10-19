import { useEffect, useState } from 'react';
import styles from './ModifyMemberInfo.module.css'
import ModifyMemberNickname from './ModifyMemberNickname';
import ModifyMemberBtn from './ModifyMemberBtn';
import AlertModal from './AlertModal';
import useAuthToken from '../../hooks/useAuthToken';
import setMemberNickname from '../../api/member/setMemberNickname';
import { useRecoilValue } from 'recoil';
import { userState } from '../../states/userAtom';

export default function ModifyMemberInfo() {
    const user = useRecoilValue(userState);
    const { setToken } = useAuthToken();

    const [nickname, setNickname] = useState(user.nickname);
    const [originNickname, setOriginNickname] = useState(user.nickname);
    const [email, setEmail] = useState(user.email)
    const [isEdit, setIsEdit] = useState(false);   // false 면 정보 조회,,, true 면 정보수정
    const [isValid, setIsValid] = useState(true);  // 닉네임 유효성 체크 boolean
    const [showModal, setShowModal] = useState(false) // 모달이 보일지 말지 결정하는 boolean
    const [modalMessage, setModalMessage] = useState("") // 모달에 띄울 메세지
    const [apiRequest, setApiRequest] = useState(false)
    

    const fetchData = async (name) => { 
        await setMemberNickname(name) 
            .then(res => {                          //요청 성공 시 useAuthToken에서 setToken 메소드 불러와 토큰 저장     
                if (res.data.success) {
                    setToken(res.data.data.accessToken, res.data.data.refreshToken)
                    setOriginNickname(name);

                    setModalMessage("닉네임 변경에 성공했습니다.");
                    setShowModal(true);
                } else {
                    setModalMessage(res.data.code.message);
                    setIsEdit(true);
                    setIsValid(false);
                    setShowModal(true);
                }
        })
            .catch(error => {  // 요청 실패 시 에러 출력
                console.log(error);
            })
    }

    useEffect(() => {
        if (apiRequest) {
            fetchData(nickname);
        }
        setApiRequest(false)
    }, [apiRequest])

    return(
        <div className={styles.container}>
            <div className={styles.title}>회원 정보 변경</div>
            <div className={styles.infoSection}>
                <div className={styles.infoItem}>
                    <div className={styles.infoTitle}>이메일</div>
                    <div className={styles.infoContent}>{email}</div>
                </div>
                <div className={styles.infoItem}>
                    <div className={styles.infoTitle}>닉네임</div>
                    <ModifyMemberNickname
                        isEdit={isEdit}
                        originNickname={originNickname}
                        nickname={nickname}
                        setNickname={setNickname}
                        isValid={isValid}
                        setIsValid={setIsValid}/>
                </div>
            </div>
            <ModifyMemberBtn
                isEdit={isEdit}
                setIsEdit={setIsEdit}
                setNickname={setNickname}
                originNickname={originNickname}
                setApiRequest={setApiRequest}
            />
            <AlertModal
                showModal={showModal}
                setShowModal={setShowModal}
                message={modalMessage}
            />
        </div>
    )

}