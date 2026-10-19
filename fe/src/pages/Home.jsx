import React, { useEffect, useState } from 'react';
import LightBox from '../components/home/LightBox';
import { homeApis } from '../api/homeApis';

import styles from "./Home.module.css";
import Thumbnails from '../components/home/Thumbnails';
import SelectedPalette from '../components/home/SelectedPalette';
import { useSetRecoilState } from 'recoil';
import { selectedCategoryAtom } from '../states/selectedCategoryAtom';

export default function Home() {
  const [resData, setResData] = useState([]);                 // 전체 데이터 저장

  // 라이트박스
  const [index, setIndex] = useState(0);                      // 현재 선택된 이미지 id
  const [slides, setSlides] = useState([]);                   // LightBox에 넣을 이미지들

  // 썸네일
  const [title, setTitle] = useState("");                     // 이미지 이름

  // 팔레트
  const [paints, setPaints] = useState();                    // 페인트(도구) 목록
  const [nickname, setNickname] = useState("");              // 팔레트 생성자 닉네임
  const [palIdx, setPalIdx] = useState("");

  const setSelectedCategoryState = useSetRecoilState(selectedCategoryAtom);   // 검색창에 띄울 값을 결정하는 state. 검색어 카테고리가 아니면 검색창을 비워줘야 하기 때문문

  const {getImgPal} = homeApis();

  //**************************테스트용***************************
  //백엔드 api 구현 이후 새로운 api로 만들어야 한다.
  // res.data: api로 받아와야 하는 이미지, 팔레트 데이터(예시)
  const fetchData = async() => {

    //1. 홈 화면(갤러리뷰)에서 보일 데이터 요청
    await getImgPal()
    .then(res=>{
        const data = res.data.data;
        console.log("통신은?",data);

        //2. 받아온 데이터 images state에 넣기
        setTitle(data[0].palName);

        //3. 받아온 데이터 LightBox용 데이터로 수정
        setSlides( data.map( date=>({ src: date.imgSrc } ) ) )
        
        //4. 선택된 이미지의 mypalette 값 저장
        const mypal = data[0];
        setPaints(mypal.paintList);
        setNickname(mypal.memberNickname);
        setPalIdx(mypal.palIdx);

        setResData(data);

    })
    .catch(err=>console.error("홈 통신 에러"+err))
  }
  //**************************테스트용***************************

  useEffect(()=>{
      setSelectedCategoryState("");
      fetchData();
  },[])

  useEffect(() => {
    if(resData.length > 0){
      const mypal = resData[index];
      setTitle(mypal.palName);
      setPaints(mypal.paintList);
      setNickname(mypal.memberNickname);
      setPalIdx(mypal.palIdx);
    }
    
  }, [index, title])


  return (
    <div className={styles.wrapper_gallery}>

      <LightBox 
        index={index} 
        setIndex={setIndex} 
        slides={slides} 
        title={title} 
        artist={nickname}/>

      <Thumbnails 
        imgs={resData}
        index={index}
        setIndex={setIndex}
        setPaints={setPaints}
        setNickname={setNickname}
        setTitle={setTitle}/>
      
      <SelectedPalette
        paints={paints}
        page={"home"}
        palName={title}
        nickname={nickname}
      />
    </div>
  );
}