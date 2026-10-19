import styles from "../../pages/PaintDetail.module.css";
import { attributeTranslateKor } from "../../constants/attributeTranslateKor.constants";
import { specTable } from "../../constants/specTable.constants";
import { OX } from "../../constants/OX.constants";

export default function SpecTable({paint}){

    return(
        <ul className={styles.table_spec}>
            {specTable.map(key=>
                <li>
                    <p className={styles.td_head}>{attributeTranslateKor[key]}</p>
                    {key==="pigment_list" ?
                        <div className={styles.tmp}>{paint.pigment.map((obj)=><p className={styles.td_content}>#{obj["ciName"]}&nbsp;</p>)}</div>
                    :
                    key==="size" ?
                        <p className={styles.td_content}>{paint[key]}{paint['unit']}</p>
                    :
                    key==="vegan"?
                        <p className={styles.td_content}>{OX[paint[key]]}</p>
                    :   
                        <p className={styles.td_content}>{paint[key]}</p>
                    
                    }
                </li>
            )}
        </ul>
    );
}