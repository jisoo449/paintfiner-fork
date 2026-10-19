import { useState } from 'react';
import styles from './ModifyMemberBtn.module.css'

export default function ModifyMemberBtn({ isEdit, setIsEdit, setNickname, originNickname, setApiRequest }) {
    const onClickEdit = () => {
        setIsEdit(true);
    }

    const onClickEditCancel = () => {
        setIsEdit(false);
        setNickname(originNickname)
    }

    const onClickEditComplete = () => {
        setIsEdit(false);
        setApiRequest(true);
    }

    // TODO: 닉네임 변경 api 쏘기, 저장 실패 시 띄울 모달 디자인, 저장 성공 시 recoil 에 token 값 저장하기

    return (
        <div className={styles.buttonSection}>
                {!isEdit ? <button onClick={onClickEdit} className={styles.textButton}>정보수정</button> :
                    <div>
                        <button onClick={onClickEditCancel} className={styles.textButton}>취소</button>
                        <button onClick={onClickEditComplete} className={styles.textButton}>수정완료</button>
                    </div>
                }
        </div>
    )
}