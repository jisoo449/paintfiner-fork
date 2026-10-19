import ProductUnitList from "../product/ProductUnitList"
import styles from "./SelectedPalette.module.css"

export default function SelectedPalette({paints, page, palName, nickname}){

    return(
        <div className={styles.wrapper_palette}>
            <p><b>{palName} 팔레트</b> by {nickname}</p>
            <ProductUnitList paints={paints} page={page} />
        </div>
    )
}