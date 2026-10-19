import styles from './Choosepal.module.css';
import Modal from 'react-modal';
import React, {useState} from 'react';
import setMyPal from '../../api/mypal/setMyPal';
import setMyPalPaint from '../../api/mypal/setMyPalPaint';
import delMyPalPaint from '../../api/mypal/delMyPalPaint';
import getMyPalList from '../../api/mypal/getMyPalList';

/**
 * 기존에 존재하는 팔레트에 페인트 추가
 * @param {*} paintIdx 선택한 물감의 인덱스
 * @param {*} palettes 선택한 팔레트의 이름름
 * @param {*} setMakePalState
 * @returns 
 */
export default function Choosepal({paintIdx, palettes, setMakePalState, setPaletteModalState}){

    const onChangeCheckbox = (e) => {
        if (e.target.checked){
            setMyPalPaint(e.target.value,[paintIdx]).then(res=>{
                console.log(res)
                setMakePalState(false);
                setPaletteModalState(false);
                document.getElementById("paletteLists").style.display='none';
            });
        }
    }

    /**
     * 새 팔레트 만들기 버튼 클릭
     */
    function toggleFunc(){
        document.getElementById("paletteLists").style.display='none';
        setMakePalState(true)
    }

    return(
        <>
            <div className={styles.wrapper_list} id="paletteLists">
                <div className={styles.list_pal}>

                    {/* palettes 목록을 map으로 순회하며 팔레트 이름 출력 */}
                    { palettes.map ( pal => (
                        <label className={styles.label}>
                            <input 
                                className={styles.checkbox} 
                                value={pal["palIdx"]} 
                                type="checkbox"
                                onChange={onChangeCheckbox}
                            />
                            {pal["palName"]}
                        </label>
                    ))}
                </div>

                {/* 새 팔레트 만들기 버튼 */}
                <div className={styles.collections_btn} onClick={toggleFunc}>
                    <img className={styles.btn_plus} src={"/icon/icon_palette_checkbox_add-button_48px.png"}/>
                    &nbsp;&nbsp;
                    <p className={styles.txt_makepal}>새 팔레트 만들기</p>
                </div>
            </div>
        </>
    )
}