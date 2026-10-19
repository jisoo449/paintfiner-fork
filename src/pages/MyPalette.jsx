import React, { useCallback, useEffect, useState } from 'react';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import styles from './MyPalette.module.css';
import getMyPalList from '../api/mypal/getMyPalList';
import MyPalList from '../components/mypalette/MyPalList';
import MyPaletteOrder from '../components/mypalette/MyPaletteOrder';
import setMyPalUpdateOrder from '../api/mypal/setMyPalUpdateOrder';
import { useRecoilState } from 'recoil';
import { palettesState, palOrderState } from '../states/myPaletteAtom';

/**
 * 마이팔레트
 * @returns 
 */
export default function MyPalette() {
  const [palettes, setPalettes] = useRecoilState(palettesState);           
  const [palOrder, setPalOrder] = useRecoilState(palOrderState);
  const [bookmarkCnt, setBookmarkCnt] = useState(0); 
  const maxBookmarkCnt = 3;

  // 북마크 된 팔레트 개수 세기
  const countBookmarks = useCallback((palettes) => {
    if (palOrder === 'pal_order,ASC') {
      return palettes
        .slice(0, maxBookmarkCnt) 
        .filter(pal => pal.bookmark > 0) 
        .length;
    }
    return 0;
  }, [palOrder, maxBookmarkCnt]);

  useEffect(() => {
    async function fetchData(){
      try{
        const res = await getMyPalList(palOrder);
        setPalettes(res);
        setBookmarkCnt(countBookmarks(res));
      } catch(e){
        console.log(e);
      }
    }
    fetchData();
  }, [countBookmarks]);

  /** 🔥 Drag & Drop 이벤트 핸들러 */
  const handleDragEnd = async (result) => { // result : D&D 이벤트 포함하는 객체
    if (!result.destination) return; // 목적지가 없으면 종료

    const items = Array.from(palettes.slice(bookmarkCnt)); // 일반 리스트만 복사
    const [reorderedItem] = items.splice(result.source.index, 1); // 기존 위치에서 제거
    items.splice(result.destination.index, 0, reorderedItem); // 새로운 위치에 삽입
    
    // 새로 정렬된 배열을 기존 배열과 합쳐서 상태 업데이트
    // async 통신 성공 확인 이전에 화면 변화를 위한 저장
    setPalettes([...palettes.slice(0, bookmarkCnt), ...items]); 
    async function fetchData(){
      try{
        const res = await setMyPalUpdateOrder(palettes[result.source.index+bookmarkCnt].palIdx, palettes[result.destination.index+bookmarkCnt].palOrder);
        setPalettes(res.data.data.content);
        setBookmarkCnt(countBookmarks(res.data.data.content));
      } catch(e){
        console.log(e);
      }
    }
    fetchData();
  };

  return (
    <div className={styles.wrap_mypal_pg}>
      <div>
        <h2 className={styles.title_mypal_pg}>마이팔레트</h2>
        <MyPaletteOrder/>
      </div>

      {/* 북마크된 팔레트 리스트 */}
      {palettes.slice(0, bookmarkCnt).map((palette, i) => (
        <MyPalList palette={palette} key={`bookmark_${i}`} />
      ))}


      {palOrder !== "pal_order,ASC" ? palettes.slice(bookmarkCnt).map((palette, i) => (
        <MyPalList palette={palette} key={`bookmark_${i}`} />
      ))
      :
      <DragDropContext onDragEnd={handleDragEnd}> {/* 🟢 Drag & Drop 적용된 일반 팔레트 리스트 */}
        <Droppable droppableId="normalPalettes">  
          {(provided) => (  // droppableId 는 드래그 가능한 아이템을 포함한 고유 ID
            <div 
              className={styles.palettes_list} // CSS 적용
              {...provided.droppableProps} //Droppable로서 필수 속성
              ref={provided.innerRef} //React에서 Drag & Drop을 제대로 감지하도록 DOM 요소 연결.
            >
              {palettes.slice(bookmarkCnt).map((palette, i) => ( 
                //draggableId: 요소의 고유한 ID (String 변환 필수), index: 리스트 내에서 요소의 위치
                <Draggable key={palette.palIdx} draggableId={String(palette.palIdx)} index={i}>
                  {(provided) => (
                    <div 
                      ref={provided.innerRef} // React에서 드래그 감지를 위해 DOM 요소 연결.
                      {...provided.draggableProps} // 요소가 드래그 가능하도록 설정.
                      {...provided.dragHandleProps} // 드래그 핸들링 가능하도록 설정.
                      className={styles.draggable_pal} // 기존 CSS 유지
                    >
                      <MyPalList palette={palette} />
                    </div>
                  )}
                </Draggable>
              ))}
              {provided.placeholder /* 드래그 중일 때 공간을 유지하도록 설정 */}
            </div>
          )}
        </Droppable>
      </DragDropContext>
      }
    </div>
  );
}