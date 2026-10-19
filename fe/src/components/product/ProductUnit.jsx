import styles from './ProductUnit.module.css';
import { Link, useLocation } from 'react-router-dom';
import Addbtn from './Addbtn';
import delMyPalPaint from '../../api/mypal/delMyPalPaint';
import nonePaintImage from '../../assets/images/image_none_paint.png'
import { useEffect } from 'react';

//셀렉트 꾸미고 싶으면 https://velog.io/@chumil7432/CSS-checkbox%EC%99%80-select-%EA%BE%B8%EB%AF%B8%EA%B8%B0

export default function ProductUnit({paint, page, palIdx}){
    const onClickDelProd = () => delMyPalPaint(palIdx,[paint['paintIdx']]);
    const pathname = useLocation().pathname;

    return(
        <div className={styles.wrapper} key={`wrapper_paint_`+paint['paintIdx']}>
            <div className={styles.content_paint}>
                <div className={styles.wrap_img}>
                    {/* <Link to={`/paintDetail${pathname}/${paint['paintCode']}`}> <img className={styles.productimg} src={`/images/${paint['paintIdx']}product.jpg`}/> </Link> */}
                    <Link to={`/paintDetail${pathname}/${paint['paintCode']}`}>
                        {paint.textureImg ? <span><img className={styles.productimg} src={paint.textureImg}/></span>:
                        <img className={styles.productimg} src={nonePaintImage}/>} 
                    </Link>
                    {page==="MyPalDetail" && <button className={styles.btn_del} onClick={onClickDelProd}>X</button>}
                </div>
                
                {/* 물감 정보 */}
                <div className={styles.txt_product_title} key={`title_paint_`+paint['paintIdx']}>
                    {/* 브랜드명 */}
                    <p className={styles.brand}>{paint['brandKR']}</p> 

                    {/* 물감 이름 */}
                    <Link to={`/paintDetail${pathname}/${paint['paintCode']}`}><p className={styles.product_name}>:{paint['paintNameEN']}</p></Link>                     
                </div>

                {/* 물감 정보 */}
                <div className={styles.div_txt_inform} key={`inform_paint_`+paint['paintIdx']}>
                    <p className={styles.txt_inform}> 
                        {paint['colorEN']} / {paint['size']}{paint['unit']}
                    </p>
                    
                    {/* 안료 정보 */}
                    <div className={styles.pig_wrapper}>
                        {paint.pigments && paint.pigments.map((pig,i)=>(
                            <p className={styles.txt_inform}>#{pig.ciName}&nbsp;</p>
                        ))}
                    </div>
                </div>
            </div>
            <div className={styles.btns} key={`btn_etc_paint_`+paint['paintIdx']}>
                <Addbtn usage={'팔레트'} paintId={paint['paintIdx']} paint={paint}/>
                &nbsp;
                <Addbtn usage={'비교하기'} paintId={paint['paintIdx']} paint={paint}/>
            </div>
        </div>
    )
}