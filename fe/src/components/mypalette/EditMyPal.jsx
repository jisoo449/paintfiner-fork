import React, { useEffect, useState, useRef } from 'react';
import styles from './EditMyPal.module.css';
import ReactDOM from 'react-dom';
import delMyPal from '../../api/mypal/delMyPal';
import setMyPalCopy from '../../api/mypal/setMyPalCopy';
import { useRecoilState } from 'recoil';
import { palettesState, palOrderState } from '../../states/myPaletteAtom';

export default function EditMyPal({palIdx, editPalTitle, onDelete}){

    //modal state's field&method
    const [modalOpen, setModalOpen] = useState(false);
    const modalRef = useRef(); //✅ 모달을 감싸는 요소의 ref 생성
    const btnRef = useRef(); // ✅ 버튼의 ref

    const [palOrder, setPalOrder] = useRecoilState(palOrderState);
    const [palettes, setPalettes] = useRecoilState(palettesState);

    const onClickEditBtn = () => {
        if(modalOpen){
            setModalOpen(false);
        } else {
            setModalOpen(true);
        }
    }

    const closeModal = (e) => {
        if (modalRef.current && !modalRef.current.contains(e.target) && // 모달 바깥 클릭
        btnRef.current && !btnRef.current.contains(e.target)){ // 버튼을 누른 게 아닐 때만 닫힘
            setModalOpen(false);
        }
    }

      // 🔥 이벤트 리스너 등록 (모달 바깥 클릭 감지)
      useEffect(() => {
        if (modalOpen) {
            document.addEventListener("mousedown", closeModal);
        } else {
            document.removeEventListener("mousedown", closeModal);
        }

        return () => {
            document.removeEventListener("mousedown", closeModal);
        };
    }, [modalOpen]);

    //onClick methods
    const onClickEditName = () => editPalTitle(true);
    const onClickCopyMyPal = async() => {
        try {
            const res = await setMyPalCopy(palIdx);
            setPalettes(res.data.data.content);
            setPalOrder("pal_order,ASC");

            let moveTo = 200;
            for(let i = 0; i<3; i++){
                if(res.data.data.content.length >i && res.data.data.content[i].bookmark>0){
                    moveTo+=400;
                }
            }
            window.scrollTo({ top: moveTo, behavior: "smooth" });
        } catch(e){
            console.log("에러 발생", e);
        }
        
    }
    const onClickDelMyPal = async() => {
        try {
            await delMyPal(palIdx);
            onDelete(); // 삭제 시 화면 렌더링 반영용. MyPalHeader.jsx, MyPalList.jsx 에 전파됨.
        } catch (e){
            console.log("삭제 중 에러 발생", e);
        }
    }

    return(
        <div className={styles.btn_modal_container} key={`btn_modal_container_`+palIdx}>
            <button ref={btnRef} className={styles.btn_edit} key={`btn_`+palIdx} onClick={() => {onClickEditBtn();}}>• • •</button>
            {modalOpen && (
                <table className={styles.modal_edit} key={`modal_`+palIdx} onClick={closeModal} ref={modalRef}>
                    <tbody>
                        <tr onClick={onClickEditName}><td key={`td1_`+palIdx}>이름 수정</td></tr>
                        {/* <tr key={`tr2_`+palIdx}><td key={`td2_`+palIdx}>공유</td></tr> */}
                        <tr onClick={onClickCopyMyPal} key={`tr3_`+palIdx}><td key={`td3_`+palIdx}>복사</td></tr>
                        <tr onClick={onClickDelMyPal} key={`tr4_`+palIdx}><td key={`td4_`+palIdx}>삭제</td></tr>
                    </tbody>
                </table>
            )}
        </div>
    );
}