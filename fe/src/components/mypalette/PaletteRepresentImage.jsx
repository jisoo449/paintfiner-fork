import { Link } from 'react-router-dom'
import styles from './PaletteRepresentImage.module.css'
import editIcon from '../../assets/icons/icon_img_edit_button.png';
import deleteIcon from '../../assets/icons/icon_img_delete_button.png'
import nonePaletteImage from '../../assets/images/image_none_palette.png'
import { useEffect, useRef, useState } from 'react';
import setMypalImg from '../../api/mypal/setMyPalImg';
import deleteMypalImg from '../../api/mypal/delMyPalImg';

export default function PaletteRepresentImage({palIdx, imgSrc}){
    const [shownImg, setShownImg] = useState(imgSrc);

    const [uploadedImg, setUploadedImg] = useState("");
    const fileInputRef = useRef(null);

    /**
     * 파일 업로드 핸들러
     * @param {*} e 
     */
    const handleImageUpload = (e) => {
        const { files } = e.target;
        if (files.length > 0) {
            const uploadFile = files[0];
            setUploadedImg(uploadFile);
        }
    };

    /**
     * 숨겨둔 파일 업로드용 input 태그 동작시키기 위함
     */
    const triggerFileInput = () => {
        fileInputRef.current.click(); // 버튼 클릭 시 파일 입력 창 열기
    };

    const onClickDeleteImg = (e) => {
        async function fetchData(){
            deleteMypalImg(palIdx).then(res => {
                setShownImg(nonePaletteImage);
            })
        }
        fetchData();
    }

    useEffect(() => {
        async function fetchData(){
            setMypalImg(uploadedImg, palIdx).then(res => {
                setShownImg(res.data.data);
            })
        }
        if(uploadedImg !== ""){fetchData();}
    }, [uploadedImg])

    return (
        <div className={styles.imageWrapper}>
            
            <Link to={`/mypalette/${palIdx}`}>
                <img className={styles.img_title} src={shownImg || nonePaletteImage    }/>
            </Link>

            <button className={styles.btn_img_delete} onClick={() => onClickDeleteImg("삭제")}>
                <img className={styles.icon_wrapper} src={deleteIcon} alt='사진 삭제'/>
            </button>

            <button className={styles.btn_img_modify} onClick={triggerFileInput}>
                <img className={styles.icon_wrapper} src={editIcon} alt='사진 수정'/>
            </button>

            {/* 파일 업로드 input (숨겨진 상태) */}
            <input
                type="file"
                accept="image/*"
                ref={fileInputRef}
                onChange={handleImageUpload}
                style={{ display: 'none' }} // UI에서 숨김
            />

            
        </div>
    )
}