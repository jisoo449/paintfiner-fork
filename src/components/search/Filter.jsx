import React from 'react';
import { filters,filterType } from '../../constants/filters.constants';
import styles from './Filter.module.css'
import { useState } from 'react';

export default function Filter({filter, onChange}) {

  const [isToggled, setIsToggled] = useState(false);
  const [startNum, setStartNum] = useState('');
  const [endNum, setEndNum] = useState('');
  const [selectedUnit, setSelectedUnit] = useState('');

  const onClickToggle = () => setIsToggled((prevToggled)=>!prevToggled)
  const onChangeCheckbox = (e) => onChange(e.target.value, e.target.checked);
  const onChangeSelectBox = (e) => setSelectedUnit(e.target.value);
  const inputStartNum = (e) => setStartNum(e.target.value);
  const inputEndNum = (e) => setEndNum(e.target.value);
  const onClickSetting = ()=> onChange([startNum,endNum],selectedUnit);
  const onClickUnsetting = () => {
    setStartNum('');
    setEndNum('');
    setSelectedUnit('');
    onChange([],selectedUnit);
  }
  
  return (
    <div className={styles.filter_box}>
      <div className={styles.cate_filter}>
        <div className={styles.str_name}>{filter.mainFilterName}</div>
        <div className={styles.btn_toggle} onClick={onClickToggle}>▼</div>
      </div>
      { isToggled && (
        <div className={styles.div_checkbox}>
          {filter.filterType===filterType.CHECK_BOX_MULTI && filter.filters.map(lv=>
            <div key={`chkbx_div_${lv[1]}`} className={styles.wrapper_checkbox}>
              <input key={`chkbx_chkbx_${lv[1]}`} className={styles.checkbox} type="checkbox" value={lv[1]} onChange={onChangeCheckbox}></input>
              <li key={`chkbx_li_${lv[1]}`} className={styles.txt_checkbox}>{lv[0]}</li>
            </div>
          )}

          {filter.filterType===filterType.CHECK_BOX_SINGLE && filter.filters.map(lv=>
            <div key={`chkbx_div_${lv[0][1]}`} className={styles.wrapper_checkbox}>
              <input key={`chkbx_chkbx_${lv[0][1]}`} className={styles.checkbox} type="checkbox" value={lv[1]} onChange={onChangeCheckbox}></input>
              <li key={`chkbx_li_${lv[0][1]}`} className={styles.txt_checkbox}>{lv[0][0]}</li>
            </div>
          )}

          {filter.filterType===filterType.INTEGER_RANGE && (
            <div className={styles.wrapper_range}>
              <div className={styles.div_range}>
                <input type="number" className={styles.input_range} onChange={inputStartNum} value={startNum}/>
                <div className={styles.tilde}>~</div>
                <input type="number" className={styles.input_range} onChange={inputEndNum} value={endNum}/>
                <select className={styles.select_unit} value={selectedUnit} onChange={onChangeSelectBox}>
                  {filter.filters.map(unit=><option key={`opt_${unit}`} value={unit}>{unit}</option>)}
                </select>
              </div>
              <div className={styles.wrapper_box}>
                <div className={styles.box_setting} onClick={onClickUnsetting}>초기화</div><div className={styles.box_setting} onClick={onClickSetting}>설정</div>
              </div>
            </div>
         )}

        </div>
      )}
    </div>
  );
}