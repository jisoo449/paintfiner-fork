import styles from './PalettePaintUnit.module.css';
import { Link, useLocation } from 'react-router-dom';
import delMyPalPaint from '../../api/mypal/delMyPalPaint';
import Addbtn from '../product/Addbtn';
import deleteIcon from '../../assets/icons/icon_img_delete_button.png'
import nonePaintImage from '../../assets/images/image_none_paint.png'
import { useState } from 'react';

//셀렉트 꾸미고 싶으면 https://velog.io/@chumil7432/CSS-checkbox%EC%99%80-select-%EA%BE%B8%EB%AF%B8%EA%B8%B0

export default function PalettePaintUnit({paint, page, palIdx}){
    const pathname = useLocation().pathname;

    const [paintUnit, setPaintUnit] = useState(paint);

    const onClickDeletePaint = () => {
        async function fetchData(){
            console.log("palIdx : ", palIdx);
            console.log("paintIdx : ", paint['paintIdx']);
            delMyPalPaint(paint['paintIdx'], palIdx).then(res => {
                setPaintUnit(0);
            })
        }
        fetchData();
    }

    if(paintUnit === 0) return;

    return(
        <div className={styles.container}>
            {page === "MyPalList" && <button className={styles.btn_delete} onClick={() => onClickDeletePaint()}>
                <img className={styles.icon_wrapper} src={deleteIcon} alt='사진 삭제'/>
            </button>}  {/* ✅ wrapper 위에 배치 */}
            <div className={styles.wrapper} key={`wrapper_paint_`+paintUnit['paintIdx']}>
                <div className={styles.content_paint}>
                    <div className={styles.wrap_img}>
                        {/* <Link to={`/paintDetail${pathname}/${paint['paintCode']}`}> <img className={styles.productimg} src={`/images/${paint['paintIdx']}product.jpg`}/> </Link> */}
                        <Link to={`/paintDetail/${paint['paintIdx']}`}>
                            {paint.textureImg1 ? <span><img className={styles.productimg} src={paint.textureImg1}/></span> : 
                            <img className={styles.productimg} src={nonePaintImage}/>}  
                        </Link>
                        {/* {page==="MyPalDetail" && <button className={styles.btn_del} onClick={onClickDeletePaint}>X</button>} */}
                    </div>
                    
                    {/* 물감 정보 */}
                    <div className={styles.txt_product_title} key={`title_paint_`+paint['paintIdx']+`_`+palIdx+`_${Math.random()}`}>
                        {/* 브랜드명 */}
                        <p className={styles.brand}>{paint['brandKR']}</p> 

                        {/* 물감 이름 */}
                        <Link to={`/paintDetail${pathname}/${paint['paintIdx']}`}><p className={styles.product_name}>: {paint['paintNameEN']}</p></Link>                     
                    </div>

                    {/* 물감 정보 */}
                    <div className={styles.div_txt_inform} key={`inform_paint_`+paint['paintIdx']+`_`+palIdx+`_${Math.random()}`}>
                        <div className={styles.txt_inform}> 
                            {paint['colorEN']}
                        </div>
                    </div>

                    <div className={styles.div_txt_inform_size} key={`inform_paint_`+paint['paintIdx']+`_`+palIdx+`_${Math.random()}`}>
                        <div className={styles.txt_inform}> 
                            {paint['size']}{paint['unit']}
                        </div>
                    </div>

                    {/* 안료 정보 */}
                    <div className={styles.div_txt_inform_pig} key={`inform_paint_`+paint['paintIdx']+`_`+palIdx+`_${Math.random()}`}>
                        
                        <div className={styles.pig_wrapper}>
                            {paint.pigments && paint.pigments.map((pig,i)=>(
                                <p className={styles.txt_inform}>#{pig.ciName}&nbsp;</p>
                            ))}
                        </div>
                    </div>
                </div>
                <div className={styles.btns} key={`btn_etc_paint_`+paint['paintIdx']+`_`+palIdx+`_${Math.random()}`}>
                    <Addbtn usage={'팔레트'} paint={paint}/>
                    &nbsp;
                    <Addbtn usage={'비교하기'} paint={paint}/>
                </div>
            </div>
        </div>
    )
}