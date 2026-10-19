import { useLayoutEffect, useRef } from "react";
import styles from "./Indicator.module.css"

/**
 * 슬라이더(인디케이터)
 * @param containerRef 부모 DOM 참조
 * @param index 선택된 인덱스
 * @returns 
 */
export default function({containerRef, index}){

    const indicatorRef = useRef(null);    


  // 부모 DOM 요소에 따라 인디케이터 위치 조정
  const updateIndicatorPosition = () => {
    if (containerRef.current && containerRef.current.children[index] && indicatorRef.current) {
      // 1. 선택된 인덱스의 위치 찾기
      const selectedImage = containerRef.current.children[index];
      const rect = selectedImage.getBoundingClientRect();
      const containerRect = containerRef.current.getBoundingClientRect();

      // 2. 인디케이터 위치 설정
      const leftPosition = rect.left - containerRect.left + (rect.width / 2) - (indicatorRef.current.offsetWidth / 2);
      indicatorRef.current.style.left = `${leftPosition}px`;
    }
  };

  useLayoutEffect(() => {
    const intervalId = setInterval(() => {
      if (containerRef.current && containerRef.current.children[index] && indicatorRef.current) {
        updateIndicatorPosition();
        clearInterval(intervalId); // 조건이 충족되면 반복 중지
      }
    }, 100); // 100ms 간격으로 시도

    const handleResize = () => updateIndicatorPosition();
    window.addEventListener('resize', handleResize);

    return () => {
      clearInterval(intervalId);
      window.removeEventListener('resize', handleResize);
    };
  }, [containerRef, index]);

    return(
        <div className={styles.indicator} ref={indicatorRef}>
            <img src="/icon/slider.png"/>
        </div>
    )

}