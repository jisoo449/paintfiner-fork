import React, { useEffect, useState } from 'react';
import styles from './Mypage.module.css';
import MyPageNavigate from '../components/mypage/MyPageNavigate';
import ModifyMemberInfo from '../components/mypage/ModifyMemberInfo';
import ManageMyComment from '../components/mypage/ManageMyComment';

export default function Mypage() {
  // 정보변경 = 1, 내댓글 = 2 페이지 방문 판별 변수.
  const [visit, setVisit] = useState(1)

  useEffect(() => {
    console.log("visit 변경 : ", visit);
  },[visit])

  return (
    <div className={styles.wrap_mypage}>
      <MyPageNavigate visit={visit} setVisit={setVisit}/>
      {visit === 1 && <ModifyMemberInfo/>}
      {visit === 2 && <ManageMyComment />}
    </div>
  );
}