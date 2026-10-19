import { useEffect, useRef, useState } from 'react';
import styles from './MyPaletteOrder.module.css';
import { useRecoilState } from 'recoil';
import { palettesState, palOrderState } from '../../states/myPaletteAtom';
import addMyPal from '../../api/mypal/addMyPal';

export default function MyPaletteOrder(){
    const orderInfo = [['기본순', 'pal_order,ASC'], 
                ['최근 수정순','last_edit_date,DESC'],
                ['생성일순', 'create_date,DESC'],
                ['공개 팔레트', 'open,ASC'],
                ['비공개 팔레트', 'open,DESC']];
    const [orderNum, setOrderNum] = useState(0);
    const [isOpen, setIsOpen] = useState(false);
    const [palOrder, setPalOrder] = useRecoilState(palOrderState);
    const dropdownRef = useRef(null); // 드롭다운 참조

    const [palettes, setPalettes] = useRecoilState(palettesState);

    useEffect(() => {
        // `palOrder`가 변경되었을 때, `orderNum`을 해당하는 인덱스로 업데이트
        const foundIndex = orderInfo.findIndex(item => item[1] === palOrder);
        if (foundIndex !== -1) {
            setOrderNum(foundIndex);
        }
    }, [palOrder]); // `palOrder` 변경 시 실행

    const onClickOrder = (e) => {
        setOrderNum(e);
        setPalOrder(orderInfo[e][1]);
        setIsOpen(false);
    }

    // 팔레트 새로 등록시 빈 팔레트 등록됨
    const onClickCreatePalette = () => {
        console.log("나 눌림");
        async function fetchData() {
            try{
                const res = await addMyPal();
                setPalettes(res.data.data.content);
                setPalOrder("pal_order,ASC");

                // 새로 생성된 팔레트 위치로 스크롤 이동 이벤트
                let moveTo = 200;
                for(let i = 0; i<3; i++){
                    if(res.data.data.content.length >i && res.data.data.content[i].bookmark>0){
                        moveTo+=400;
                    }
                }
                window.scrollTo({ top: moveTo, behavior: "smooth" });
            } catch(e){
                alert(e);
            }
        }

        fetchData();
    }

    // 드롭다운 외부 클릭 감지하여 닫기
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <div className={styles.wrap_order_dropdown} ref={dropdownRef}>
            <button className={styles.create_button} onClick={()=>onClickCreatePalette()}>+ 팔레트</button>
            <div className={styles.left_sorted}>
                <div className={styles.dropdown}>
                    <button className={styles.dropdown_button} onClick={()=>setIsOpen(!isOpen)}>
                        <div className={styles.dropdown_word}>{orderInfo[orderNum][0]}</div> 
                        <div className={styles.dropdown_down_arrow}>▼</div>
                    </button>
                    {isOpen && (
                        <ul className={styles.dropdown_menu}>
                            {orderInfo.map((item, index) => (
                                <li key={index} onClick={() => onClickOrder(index)}>
                                    {item[0]} {/* 드롭다운 항목 출력 */}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>
            
        </div>
    );
}