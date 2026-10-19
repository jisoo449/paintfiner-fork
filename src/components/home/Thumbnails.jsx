import { useEffect, useRef, useState } from "react";
import styles from "./Thumbnails.module.css";
import Indicator from "./Indicator";

/**
 * 선택된 썸네일 이미지에 따라 lightbox, palette 컴포넌트에 보일 정보 수정
 * @param imgs 받아온 이미지-팔레트 데이터
 * @param index 선택된 이미지의 인덱스(프론트엔드 기준)
 * @param setIndex 인덱스 세터
 * @param setPaints 팔레트에 보일 페인트 세터 
 * @param setNickname 팔레트에 보일 닉네임 세터
 * @param setTitle 라이트박스에 보일 작품명 세터
 * @returns 썸네일 및 슬라이더(인디케이터)
 */
export default function Thumbnails({imgs, index, setIndex, setPaints, setNickname, setTitle}){

    const containerRef = useRef(null);  // 인디케이터 위치 조정에 필요한 요소


    const onClickImage = (img, i) => {

        setIndex(i);

        // 팔레트 컴포넌트에 영향
        // 선택한 이미지와 매칭되는 팔레트 state 설정
        setPaints(img.paintList);
        setNickname(img.memberNickname);

        // 라이트박스 컴포넌트에 영향
        // 선택한 이미지와 매칭되게 이미지 이름, 작가명 변경
        setTitle(img.title);
    
    }

    useEffect(()=>{},[imgs])


    return(

        <div className={styles.wrapper_thumbs}>
 
            <div className={styles.wrapper_artsList} ref={containerRef}>
                {Array.isArray(imgs) && imgs.map((img, i)=>(
                    <img 
                        src={img.imgSrc} 
                        key={i} 
                        onClick={()=>onClickImage(img, i)} 
                        className={index === i ? styles.selected : ''}
                    />                
                ))}
            </div>
            
            <Indicator containerRef={containerRef} index={index}/>

            <hr/>
        </div>
    )
}