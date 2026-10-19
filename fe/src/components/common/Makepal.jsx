import styles from './Makepal.module.css';
import setMyPal from '../../api/mypal/setMyPal';
import { useState } from 'react';

/**
 * 새로운 팔레트 생성하는 모달
 * @param {} paintId 선택한 물감의 인덱스
 * @param setModalState 
 * @returns 
 */
export default function Makepal({paintIdx, setModalState}){
    const [txtlength, setTxtlength] = useState(0);
    const [newPaletteName, setNewPaletteName] = useState();
    const [whetherOpen,setWhetherOpen] = useState(0);

    const handleInputPalName = (e) =>{
        setNewPaletteName(e.target.value)
        setTxtlength(e.target.value.length)
    }

    const handleSelectOpen = (e) => {
        document.getElementById("makebtn").disabled=false;
        setWhetherOpen(e.target.value);
    }

    /**
     * 팔레트 생성하기 버튼
     * 클릭시 paintIdx, newPaletteName(새로 생성한 팔레트 이름), whetherOpen(공개/비공개 여부)를 param으로 setMyPal api에 전달한다.
     */
    function onClickMakePal(){
        setMyPal(paintIdx,newPaletteName,whetherOpen).then().catch(err=>{throw Error(err)});
        setModalState(false);
    }

    return (
        <div className={styles.makePalette} id="makePalette">
            {/* 팔레트 이름 입력 */}
            <label htmlFor="palName"/>팔레트 이름<br/>
            <input placeholder="이름을 입력하세요" type="text" name="palName" id="palName" className={styles.input_style} value={newPaletteName} onChange={handleInputPalName}/>

            {/* 팔레트 공개/비공개 여부 선택 */}
            <div className={styles.txtlength}>{txtlength}/30</div>
            <label htmlFor="privacySetting"/>공개범위<br/>
            <select className={styles.select_public_scope} name="privacySetting" onChange={handleSelectOpen}>
                <option value={1}>공개</option>
                <option value={0}>비공개</option>
            </select>
            <br/>

            {/* 팔레트 생성 버튼 */}
            <button className={styles.makebtn} id="makebtn" onClick={onClickMakePal}>만들기</button>
        </div>
    );
}

//<option value="-1" selected disabled>선택하세요</option>