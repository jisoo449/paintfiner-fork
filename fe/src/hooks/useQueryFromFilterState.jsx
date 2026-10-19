import { useMemo } from 'react';
import { pageReqAtom } from '../states/pageAtom';
import { paintFilterAtom } from '../states/paintFilterAtom';
import { useRecoilValue } from 'recoil';

const defaultState = {
  search: "",
  brand: "",
  quality: "",
  texture: "",
  transparency: "",
  lightfastness: "",
  colorgroup: "",
  start_size: 0,
  end_size: 0,
  unit: "",
  sets: 0,
};

/**
 * 필터를 쿼리문으로 변환하는 훅
 * @returns 
 */
export default function useQueryFromFilterState() {
  const paintFilterState = useRecoilValue(paintFilterAtom);
  const pageReqState = useRecoilValue(pageReqAtom);

  return useMemo(() => {
    const params = new URLSearchParams();

    Object.keys(paintFilterState).forEach(key => {
      if (paintFilterState[key] !== defaultState[key] && paintFilterState[key] !== "") {
        params.append(key, paintFilterState[key]);
      }
    });

    // 페이지 정보
    Object.keys(pageReqState).forEach(key => {
        params.append(key, pageReqState[key]);
    });


    return params.toString();
  }, [paintFilterState, pageReqState]);
}
