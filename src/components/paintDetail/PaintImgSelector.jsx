import { useState } from 'react';
import styles from './PaintImgSelector.module.css';

export default function PaintImgSelector({paint}) {
    const [selector, setSelector] = useState(0); // 이미지 선택
    const [isOpen, setIsOpen] = useState(false);

    const imgArr = [paint.prodImg, paint.textureImg1, paint.textureImg2, paint.textureImg3];
    return (
        <div className={styles.wrapper}>
            <div className={styles.img_preview_queue}>
            {imgArr.map((img, index) =>
                img ? (
                    <div
                    key={index}
                    className={selector === index ? styles.img_privew_unit_selected : styles.img_privew_unit}
                    onMouseEnter={() => setSelector(index)}
                    >
                        <img src={img} alt={`paint preview ${index}`} />
                    </div>
                ) : null
            )}
            </div>
            <div className={styles.img_represent} onClick={()=>setIsOpen(true)}>
                <img src={imgArr[selector]} alt='paint Represent img'/>
            </div>
            {isOpen && <div className={styles.overlay} onClick={()=>setIsOpen(false)}>
                <img src={imgArr[selector]} alt='' className={styles.overlayImage}/>
            </div>
            }
        </div>
    )
}