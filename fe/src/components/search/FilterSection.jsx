import React from 'react';
import { filters,filterType } from '../../constants/filters.constants.js';
import Filter from './Filter';
import styles from './FilterSection.module.css'
import { useRecoilState } from 'recoil';
import { paintFilterAtom } from '../../states/paintFilterAtom.js';

/**
 * ### 필터 컴포넌트  
 * 필터 UI 및 필터 값 선택에 따라 필터 state 변경
 * @param {*} param0 
 * @returns 
 */
export default function FilterSection() {

  const [filterState, setFilterStates] = useRecoilState(paintFilterAtom);
  
  const handleFilterChange = (filtertype, filterName, value, checked) => {

    if(filtertype===filterType.CHECK_BOX_MULTI){
      if(checked){
        setFilterStates({
          ...filterState,
          [filterName]:[...(filterState[filterName] || []), value],
        });
      }
      else{
        setFilterStates({
          ...filterState,
          [filterName]: filterState[filterName].filter(item => item !== value),
        });
      }
    }

    if(filtertype===filterType.CHECK_BOX_SINGLE){
      if(checked){
        setFilterStates({
          ...filterState,
          [filterName]:value,
        });
      }
      else{
        setFilterStates({
          ...filterState,
          [filterName]:0,
        });
      }
    }    

    else if(filtertype===filterType.INTEGER_RANGE){
      if(filterName==='size'){
        setFilterStates({
          ...filterState,
          start_size: value.start, // 사용자 입력 범위의 시작값
          end_size: value.end, // 사용자 입력 범위의 종료값
          unit: checked ? value.unit : 'ml', // 'ml' 또는 'g'
        });
      }
      // if(filterName==='price'){
      //   setParentState({
      //     ...parentState,
      //     [filterName]:value,
      //     ['currency']:checked,
      //   });
      // }
    }
  };

  return (
    <div className={styles.filter_wrapper}>
      <section className={styles.section}>
        <div className={styles.filter_title}>필터</div>
        {filters.map((filter,index) => (
          <Filter key={`filter_${filter.queryName}${index}`} filter={filter} onChange={(value, checked)=>handleFilterChange(filter.filterType, filter.queryName, value, checked)}/>
        ))}
      </section>
    </div>
  );
}

