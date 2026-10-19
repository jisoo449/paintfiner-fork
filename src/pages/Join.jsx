import styles from "./Login.module.css";
import LoginJoinBox from "../components/member/LoginJoinBox";
import { useEffect } from "react";
import { selectedCategoryAtom } from "../states/selectedCategoryAtom";
import { useSetRecoilState } from "recoil";

export default function Join() {

  const actionType="Join"
  const setSelectedCategoryState = useSetRecoilState(selectedCategoryAtom);   // 검색창에 띄울 값을 결정하는 state. 검색어 카테고리가 아니면 검색창을 비워줘야 하기 때문문
  
  useEffect(()=>setSelectedCategoryState(""))  

  return (
    <div className={styles.wrapper}>
      <LoginJoinBox actionType={actionType}/>
    </div>
  );
}
