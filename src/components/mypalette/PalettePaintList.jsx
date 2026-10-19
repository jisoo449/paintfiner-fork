import styles from "./PalettePaintList.module.css";
import PalettePaintUnit from "./PalettePaintUnit";

/**
 * 물품 목록 뷰를 리턴하는 컴포넌트
 * 
 * @param {paints} paints 물감(도구) 배열 
 * @param {palIdx} palIdx 팔레트 인덱스
 * @param {page} page 본 컴포넌트를 호출한 페이지
 *  
 * @returns 물품 목록 view
 */
export default function PalettePaintList({paints, page, palIdx}){

    // console.log("리스트", page)
    // TODO : 페인트_팔레트 데이터 넣고, 연결 테스트 필요,
    //        안료 데이터도 몇개 넣어야됨.

    // wrapper_prod: ProductUnitList를 호출한 페이지에 따라 다른 css를 사용
    const wrapper_prods= page==="Search"? styles.product_grid 
                        : page==="MypalList"? styles.product_scrollable /* 가로 스크롤 적용됨 */
                        : page==="home" ? styles.product_row100
                        : styles.product_scrollable;

    return(
        <div className={wrapper_prods} style={{marginTop : page==="MyPalDetail" && "0px"}}>
            {paints && paints.map((paint,i) => (
                <PalettePaintUnit key={`productUnit_${i}`} paint={paint} page={page} palIdx={palIdx} id={i}/>
            ))}
        </div>
    )
}