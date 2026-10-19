import { useLocation } from 'react-router-dom';
import styles from './Navigation.module.css';
import { searchQueryParams } from '../../constants/searchQueryParam.constants';

/**
 * 현재 위치를 알려주는 
 * ex) 페인트파인더 > 물감 > 유화
 * @returns 
 */
export default function Navigation(){
    const location = useLocation();
    const query = new URLSearchParams(location.search);
    const path = location.pathname.replace('/','').split('/');

    return (
        <div className={styles.navigate_location}>
            <p>페인트 파인더</p>
            {// case1. 경로가 /search 로 시작
             path[0]==="search"?
                path.map((e,i)=> i>0 && <p>&nbsp;&#62;&nbsp;{searchQueryParams[e]}</p>)
                :
             // case 2. 경로가 /mypalette로 시작
             path[0]==="mypalette"?
                <p>&nbsp;&#62;&nbsp;마이페이지&nbsp;&#62;&nbsp;마이팔레트</p>
                :
             //case 3. 경로가 /paint로 시작
             path[0]==="paint"&&
                <p>&nbsp;&#62;&nbsp;{searchQueryParams["paint"]}</p>
            }
            {query.get("cate1") && <p>&nbsp;&#62;&nbsp;{searchQueryParams[query.get("cate1")]}</p>}
            {query.get("cate2") && <p>{query.get("cate2")}</p>}
        </div>
    )
}