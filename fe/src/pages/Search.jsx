import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import FilterSection from '../components/search/FilterSection';
import ListSection from '../components/search/ListSection';
import styles from './Search.module.css'
import Paging from "../components/common/Paging";
import getPaints from '../api/paint/getPaints';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';
import { paintFilterAtom } from '../states/paintFilterAtom';
import { pageResAtom, pageReqAtom } from '../states/pageAtom';
import useQueryFromFilterState from '../hooks/useQueryFromFilterState';
import { selectedCategoryAtom } from '../states/selectedCategoryAtom';


export default function Search() {
  const [paints,setPaints] = useState();

  const filterStates = useRecoilValue(paintFilterAtom);                       // 필터 상태 리코일
  const pageReqState = useRecoilValue(pageReqAtom);                           // 페이징 요청 리코일
  const [pageResState, setPageResState] = useRecoilState(pageResAtom);        // 페이징 응답 리코일
  const setSelectedCategoryState = useSetRecoilState(selectedCategoryAtom);

  const location = useLocation();
  const pathname = location.pathname;

  const queryString = useQueryFromFilterState();                              // 페이징, 필터 리코일 토대로 쿼리 스트링 생성

  /**
   * @param {*} requestURL : 네비게이션바에서 선택한 cate값들, 필터값들
   * getPaints api를 통해 paint 정보를 res로 받아와 paints에 저장
   * paints는 페이지 상단에 useState 훅으로 선언되었다.
   */
  async function fetchData(requestURL){
    await getPaints(requestURL).then(res=>{

      const result = res.data.data;
      const paints = result.content;
      const paging = result.pageable;

      setPaints(paints);
      setPageResState({totalElements:result.numberOfElements, size:paging.pageSize, page:paging.pageNumber})
    });
  }


  /**
   * URL 및 필터값에 변경이 생길 때 마다 렌더링
   * 즉, 네비게이션 바에서 카테고리를 선택하거나 필터 섹션에서 값을 선택하면
   * fetchData를 호출해 getPaints api를 실행하여 paint들을 가져온다.
   */
  useEffect(()=>{
    
    // 1. 리코일로 저장된 페이지, 필터 정보로 쿼리 스트링(URL) 생성
    const requestURL = pathname+"?"+queryString;

    // 2. fetchData 실행(데이터 요청 및 저장)
    fetchData(requestURL);

    setSelectedCategoryState(pathname.split('/').pop());
  
  },[filterStates, pageReqState, pathname]);



  if(pageResState.totalElements===0){
    return (
      <div className={styles.outlet_box}>
        <FilterSection/>
        <div className={styles.search_section}>
          <p>검색 결과가 존재하지 않습니다.</p>
        </div>
      </div>
    )
  }

  else{
    return ( 
      <div className={styles.outlet_box}>
        <FilterSection/>
        <div className={styles.search_section}>
          <div className={styles.search_section_content}>
            <ListSection paints={paints} />
            <Paging />
          </div>
        </div>
      </div>
    );
  }
}