import { getActiveElement } from '@testing-library/user-event/dist/utils';
import  React ,  {  useEffect ,  useState  }  from  'react' ; 
import Pagination from "react-js-pagination";
import style from './Paging.module.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import { pageReqAtom, pageResAtom } from '../../states/pageAtom';
require("bootstrap/less/bootstrap.less");
//npm: https://www.npmjs.com/package/react-js-pagination
//참고: https://velog.io/@ctdlog/React-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-%EA%B5%AC%ED%98%84

export default function Paging(){
        
    const [pageReqState, setPageReqState] = useRecoilState(pageReqAtom);
    const pageResState = useRecoilValue(pageResAtom);

    const handlePageChange = (clickedpage) => {
        setPageReqState({
            ...pageReqState,
            page:clickedpage
        });
        // query.delete("current_page");
        // query.set("current_page",clickedpage);
        // const newURL = `${pathname}?${query}`;
        // navigate(newURL);
    };

    return (
        <>
            <hr/>
                <Pagination
                activePage={pageResState.page} // 현재 페이지
                itemsCountPerPage={pageResState.size} // 한 페이지랑 보여줄 아이템 갯수
                totalItemsCount={pageResState.totalElements} // 총 아이템 갯수
                pageRangeDisplayed={10} // paginator의 페이지 범위
                prevPageText={"‹"} // "이전"을 나타낼 텍스트
                nextPageText={"›"} // "다음"을 나타낼 텍스트
                onChange={handlePageChange} // 페이지 변경을 핸들링하는 함수
                />
            <hr/>
        </>
      );
}