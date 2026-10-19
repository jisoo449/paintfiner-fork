import styles from './PaintDescription.module.css';

export default function PaintDescription({paint}){
    return(
        <div className={styles.wrapper}>
            <div className={styles.title}>제품상세</div>
            <div className={styles.description}>{paint.descriptionEN}</div>
        </div>
    )
}