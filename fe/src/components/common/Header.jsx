import React, { useEffect } from 'react';
import styles from './Header.module.css'
import { headerSearch } from '../../constants/placeholder.constants.js';
import UGIcon from './UGIcon';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue, useResetRecoilState } from 'recoil';
import { paintFilterAtom } from '../../states/paintFilterAtom.js';
import useQueryFromFilterState from '../../hooks/useQueryFromFilterState.jsx';
import { pageReqAtom } from '../../states/pageAtom.js';
import { selectedCategoryAtom } from '../../states/selectedCategoryAtom.js';

export default function Header() {
  const [input, setInput] = useState('');
  const navigate = useNavigate();
  
  const [searchState, setSearchState] = useRecoilState(paintFilterAtom);
  const resetPageState = useResetRecoilState(pageReqAtom);
  const queryString = useQueryFromFilterState(searchState);
  const selectedCategoryState = useRecoilValue(selectedCategoryAtom);

  const handleInput = (e) => setInput(e.target.value);

  /**
   * 리코일(페이지, 필터) 초기화 및 검색어로 페이지 이동
   */
  const onClickSearch = () => {
    //1. 초기화
    resetPageState();
    setSearchState({search:input});

    //2. 페이지 이동
    navigate("/paint?"+queryString);
  }

  /**
   * 엔터 입력 시 onClickSearch 함수 실행
   * @param {*} e 
   */
  const activeEnter = (e) => { 
    if (e.key==="Enter"){
      onClickSearch();
    }
  }

  useEffect(()=>{
    if (selectedCategoryState != "paint"){
      setInput("");
    }
  },[selectedCategoryState])

  return (
    <header className={styles.header}>
      <UGIcon/>
      <div className={styles.title_box}>
        <Link to='/'><h1 className={styles.h1}>PAINT FINDER</h1></Link>
        <div className={styles.search_wrapper}>
          <input className={styles.search_input} type='text' placeholder={headerSearch} value={input} onChange={handleInput} onKeyDown={activeEnter}/>
          <img className={styles.search_icon} alt="검색아이콘" src="/icon/search.png" onClick={onClickSearch}/>
        </div>
      </div>
    </header>
  );
}
//<AiOutlineSearch className={styles.search_icon}></AiOutlineSearch>
