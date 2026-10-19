import { useEffect, useState } from 'react';
import styles from './PaintSpecification.module.css';
import Addbtn from '../product/Addbtn';

export default function PaintSpecification({paint, setPaint, paints}) {
    const [sizeOrderIdx, setSizeOrderIdx] = useState();

    useEffect(() => {
        for(let i=0; i<paints.length; i++){
            if(paints[i].paintIdx === paint.paintIdx){
                setSizeOrderIdx(i);
            }
        }
    }, [paint])

    const onClickSize = (idx) => {
        setPaint(paints[idx]);
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.text_brand}>{paint.brandEN}: {paint.paintNameEN}</div>
            <div className={styles.text_productName}>{paint.colorEN}</div>
            <div className={styles.wrapper_pigment}>
                {paint.pigments.map((pigment) =>
                    pigment ? (
                        <div>#{pigment.ciName ? pigment.ciName : pigment.ciNumber}</div>
                    ) : null
                )}
            </div>
            <div className={styles.wrapper_size}>
                <div className={styles.text_paintSize}>
                    <b className={styles.text_sizeDefault}>SIZE: &nbsp;</b> 
                    <div>{paint.size}{paint.unit}</div>
                </div>
                <div className={styles.wrapper_sizeBoxArea}>
                    {paints.map((p, idx) =>
                        p.size ? (
                            <button className={`${styles.wrapper_sizeBoxUnit} ${idx===sizeOrderIdx ? styles.selected : ''}`} onClick={()=>onClickSize(idx)}>{p.size}{p.unit}</button>
                        ) : null
                    )}
                </div>
            </div>
            <div className={styles.wrapper_spec}>
                <table className={styles.spec_table}>
                    <tbody>
                        <tr>
                            <th>lightfastness</th>
                            <td>{paint.lightfastnessEN}</td>
                        </tr>
                        <tr>
                            <th>quality</th>
                            <td>{paint.qualityEN ? paint.qualityEN : "-"}</td>
                        </tr>
                        <tr>
                            <th>texture</th>
                            <td>{paint.textureEN ? paint.textureEN : "-"}</td>
                        </tr>
                        <tr>
                            <th>transparency</th>
                            <td>{paint.transparencyEN ? paint.transparencyEN : "-"}</td>
                        </tr>
                        <tr>
                            <th>series</th>
                            <td>{paint.series ? paint.series : "-"}</td>
                        </tr>
                        <tr>
                            <th>vegan</th>
                            <td>{paint.vegan===1 ? "O" : "X"}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div className={styles.btn_wrapper}>
                <div className={styles.btn_unit}>
                    <Addbtn usage="비교하기" paint={paint}/>
                </div>
                &nbsp;
                <div className={styles.btn_unit}>
                    <Addbtn usage="팔레트" paint={paint}/>
                </div>
            </div>
        </div>
    )
}