import styles from './CompareList.module.css';
import {useState,useEffect} from 'react';
import { useRecoilValue,useRecoilState, useResetRecoilState } from 'recoil';
import {testUserState} from '../states/testAtom';
// import delCompare from '../api/compare/delCompare';
// import getCompare from '../api/compare/getCompare';
import { compareState } from "../states/compareAtom";

/**
 * 비교하기 데이터 모달창
 * @returns 
 */
export default function CompareList(){
    const lis=[(<div><img/><img/><ul>
            <ol><p>Brand Name</p></ol>
            <ol><p>Product Name</p></ol>
            <ol><p>Color Code</p></ol>
            <ol><p>Pigment number</p></ol>
            <ol><p>Series</p></ol>
            <ol><p>Transparency</p></ol></ul></div>)];
    const [compares,setCompares] = useRecoilState(compareState); // useState([]);
    const resetCompares = useResetRecoilState(compareState);
    
    const [modalOpen, setModalOpen] = useState(false);
    const user = useRecoilValue(testUserState);

    const onClickCloseModal = (e) => {setModalOpen(prev=>!prev); console.log('close clicked');}
    const onClickDelProd = (i) => setCompares(prev => prev.filter((_, idx) => idx !== i));
        //delCompare(id);
        //getCompare().then(res=>setCompares(res))
    
    // const prod_wrap = (paint) => {
    //     return(
    //         <div className={styles.prod_section}>
    //             <img className={styles.prod_img}></img>
    //             <button className={styles.btn_del} onClick={onClickDelProd(paint.paintCode)}>X</button>
    //             <span>{paint.colorEN}</span>
    //             <span>{paint.brandKR}</span>
    //         </div>
    //     )
    // }

    async function fetchData() {
        //getCompare("test@naver.com").then(res=>setCompares(res))
    }

    useEffect(()=>{
        fetchData()
    },[compares])

    return(
        <div className={styles.compare_section}>
            <div className={styles.compare_header}>
                <span>비교하기({compares.length})</span>
                <div className={styles.compare_header_right}>
                    <button className={styles.btn_showBig} onClick={()=>resetCompares()}>전체삭제</button>
                    <button className={styles.btn_showBig}>전체화면</button>
                    <button className={styles.close_btn} onClick={onClickCloseModal}>{modalOpen?<span>▼</span>:<span>▲</span>}</button>
                </div>
            </div>
            {<div>
                {modalOpen&&compares.map((paint,i)=>
                            <div className={styles.prod_section}>
                                <img className={styles.prod_img} src={paint.textureImg}></img>
                                <button className={styles.btn_del} onClick={()=>onClickDelProd(i)}>X</button>
                                <span>{paint.colorEN}</span>
                                <span>{paint.brandKR}</span>
                            </div>
                )}
            </div>}
        </div>
    );
}