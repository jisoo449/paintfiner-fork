import styles from './MyPalList.module.css'
import { Link } from 'react-router-dom';
import MyPalHeader from './MyPalHeader';
import PaletteRepresentImage from './PaletteRepresentImage';
import PalettePaintList from './PalettePaintList';
import { useState } from 'react';

/** # MyPalList
 * 특정 팔레트에 저장한 물감 목록 출력  
 * 
 * #### 사용 컴포넌트
 * MyPalHeader: 팔레트 이름 출력, 동작(이름 변경, 수정, 삭제)을 수행  
 * ProductUnitList: 물감목록  
 * Link: 팔레트 상세화면으로 이동  
 * 
 * @param {*} palette 팔레트 정보, 물감 목록
 * @returns 특정 팔레트 뷰
 */
export default function MyPalList({palette}) {

    // 마이팔레트 삭제시 렌더링도 같이 삭제를 위한 상태변수
    const [isDeleted, setIsDeleted] = useState(false);
    if(isDeleted) return;

    return(
        <div key={`wrapper_mypal_`+palette.palIdx} className={styles.wrapper_mypal}>
            <MyPalHeader 
                palIdx={palette.palIdx} 
                palName={palette.palName} 
                open={palette.open} 
                bookmark={palette.bookmark}
                onDelete={() => setIsDeleted(true)}
            />
            
            <div key={`content_mypal_`+palette.palIdx} className={styles.prods_mypal}>
                <PaletteRepresentImage palIdx={palette.palIdx} imgSrc={palette.imgSrc}/>
                
                <PalettePaintList paints={palette.paintList} page="MyPalList" palIdx={palette.palIdx}/>
            
            </div>
        
        </div>
    )
}
