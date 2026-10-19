import styles from './Addbtn.module.css';
// import setCompare from '../../api/compare/setCompare';
import Choosepal from './Choosepal';
import Makepal from '../common/Makepal';
import getMyPalList from '../../api/mypal/getMyPalList';
// import getCompare from '../../api/compare/getCompare';
import { useState } from 'react';
import Modal from 'react-modal';
import { compareState } from '../../states/compareAtom';
import { useRecoilState, useSetRecoilState } from 'recoil';

export default function Addbtn({usage, paint}){
    const [paletteModal, setPaletteModal] = useState(false);
    const [palettes, setPalettes] = useState([]);
    const [makePalState, setMakePalState] = useState(false);
    const [compares, setCompares] = useRecoilState(compareState);

    const onClickCompare = e => {
        setCompares(prev => [...prev, paint]);     
    }

    /* +팔레트 버튼을 누르면 아래 동작을 수행한다.
       1. getMyPalList api를 수행해 팔레트 이름, idx를 가져오기
       2. paletteModal(이후 진행될 팔레트 생성 동작을 위한 모달)의 값을 true로 변경
    */
    const onClickMakePal = e =>{
        getMyPalList().then( res => {
            
            setPalettes(
                res.map(pal => ({"palIdx":pal.palIdx, "palName":pal.palName}))
            )        
        
            setPaletteModal(true);
        })
    }

    //***********************버튼 및 버튼 클릭 뷰↓↓****************************

    if(usage==="비교하기"){
        return(
            <button className={styles.btn_add} onClick={onClickCompare}>+{usage}</button>
        )
    }

    else if(usage==="팔레트"){
        return(
            <>
                <button className={styles.btn_add} onClick={onClickMakePal}>+{usage}</button>
                <Modal className={styles.modal} isOpen={paletteModal} onRequestClose={() => {setPaletteModal(false); setMakePalState(false);}} ariaHideApp={false}>
                    <p className={styles.txt_title}>팔레트 저장하기...</p>
                    {/* 팔레트가 있다면 Choosepal 컴포넌트를 띄운다. */
                     palettes.length > 0 && !makePalState
                        ? (
                            <Choosepal paintIdx={paint['paintIdx']}
                                       palettes={palettes} 
                                       setMakePalState={setMakePalState} 
                                       setPaletteModalState={setPaletteModal}
                            />
                        )
                        : ( /* makePalState가 true이거나(Choosepal 컴포넌트 작업 완료) paletteName이 없다면(기존에 생성한 팔레트가 없다면) Makepal 컴포넌트를 띄운다 */
                            <Makepal paintIdx={paint['paintIdx']} 
                                     setModalState={setPaletteModal}
                            />
                    )}
                </Modal>
            </>
        )
    }

}
//<div className='palName' onClick={onClickPalName}>{palname.pal_name}</div>
/*
    function onClickPalName(e){
        setChoosePalName(e.target.innerText);
        console.log(e.target.innerText);
    }
*/