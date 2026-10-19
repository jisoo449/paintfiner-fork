import styles from "./MyPalHeader.module.css";
import { Link } from "react-router-dom";
import EditMyPal from "./EditMyPal";
import { useCallback, useEffect, useState } from "react";
import setMyPalUpdateName from "../../api/mypal/setMyPalUpdateName";
import delMyPal from "../../api/mypal/delMyPal";
import pinIcon from "../../assets/icons/icon_pinned.png";
import unPinIcon from "../../assets/icons/icon_unpinned.png";
import bookmarkMyPal from "../../api/mypal/setBookmarkMyPal";
import setOpenMyPal from "../../api/mypal/setOpenMyPal";

export default function MyPalHeader({ palIdx, palName, open, page, bookmark, onDelete}) {

  // mypalette header의 wrapper
  // mypalette 하나만 상세보기(detail)의 경우 title_mypal_detail_pg를,
  // mypalette전체 리스트 보기의 경우 title_mypal_li_pg 스타일을 가짐.
  const title_wrapper = page === "detail" ? styles.title_mypal_detail_pg : styles.title_mypal_li_pg;
  const open_btn = open === 1 ? styles.toggle_btn_pb : styles.toggle_btn_pv;          // mypalette 공개 여부에 따른 스타일 (공개:1, 비공개:0)
  const [editMyPalTitle, setEditMyPalTitle] = useState(false);                        // 팔레트 수정 모드 상태
  const [title, setTitle] = useState(palName);                                       // 팔레트 제목
  const [bookmarkOrder, setBookmarkOrder] = useState(bookmark);
  const [isOpen, setIsOpen] = useState(open);

  const onClickEditMyPalTitle = () => setEditMyPalTitle((prev) => !prev);

  const onChangeTitle = (e) => {
    setTitle(e.target.value);
  }
  
  /**
   * 팔레트 이름 수정 버튼 클릭 이벤트
   */
  const onClickChangeTitle = () => {
    async function fetchData(){
      setMyPalUpdateName(title, palIdx).then(res => {
      setEditMyPalTitle(false);
    })};
    fetchData();
  };
  
  /**
   * 팔레트 공개모드 전환 버튼 클릭 이벤트
   * @returns 
   */
  const onClickOpen = () => {
    setOpenMyPal(palIdx, (isOpen+1)%2).then(res => {
      setIsOpen(res.data.data);
    })
  }; 
  
  /**
   * 팔레트 삭제 버튼 클릭 이벤트
   */
  const onClickDelMyPal = () => {
    console.log("씨발");
    delMyPal(palIdx);
    window.location.href = "/mypalette";
  };

  /**
   * 팔레트 북마크 버튼 클릭 이벤트
   */
  const onClickBookmark = () => {

    async function fetchData(){
      if(bookmarkOrder === 0){
        bookmarkMyPal(palIdx, 0, true).then(res => {
          setBookmarkOrder(res.data.data);
        }).catch(e => {
          alert(e.response.data.code.message);
        })
      } else {
        bookmarkMyPal(palIdx, 0, false).then(res => {
          setBookmarkOrder(res.data.data);
        })
      }
      
    }
    fetchData();
  }

  return (
    <div className={title_wrapper}>
      {/* 마이팔헤더 */}
      <div className={styles.title_content}>

        {/* 북마크(즐겨찾기) */}
        <div className={styles.pin_pin} onClick={()=>onClickBookmark()}>
          {bookmarkOrder > 0 ? <img src={pinIcon}/> :<img src={unPinIcon}/>}
        </div>
        <button className={styles.btn_toggle} style={{ backgroundColor: isOpen === 1 ? "#3352f0" : "#b6b6b6" }} onClick={onClickOpen}>
          {isOpen === 1 ? `전체공개` : `비공개`}
        </button>

        {editMyPalTitle ? (
          //   이름 수정 상태일 때 적용됨
          <div>
            <input type="text" className={styles.input_title} value={title} onChange={onChangeTitle}></input>
            <button onClick={onClickChangeTitle}>입력</button>
          </div>
        ) : page === "detail" ? (
          <div>{title}</div>
        ) : (
          <Link to={`/mypalette/${palIdx}`}>
            <p>{title}</p>
          </Link>
        )}
        
      </div>

      {
        page != "detail"
          //마이팔레트 목록 페이지의 경우 EditMyPal 컴포넌트 사용
          ? (
            <>
              
              <EditMyPal 
                palIdx={palIdx} 
                editPalTitle={(res) => setEditMyPalTitle(res)} 
                onDelete={onDelete}
                 />
            </>
          )

          // 마이팔레트 상세보기(page===detail) 
          :(
            <div className={styles.wrapper_btn}>
              <button className={styles.btn_cor} onClick={onClickEditMyPalTitle}>
                이름 수정
              </button>

              <button className={styles.btn_cor}>내 팔레트 공유</button>

              <button className={styles.btn_cor} onClick={onClickDelMyPal}>
                팔레트 삭제
              </button>
              <select className={styles.select_sort} disabled>
                <option value="none" hidden>
                  정렬
                </option>
                <option value="basic">기본순</option>
              </select>
            </div>
      )}
    </div>
  );
}
