import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styles from './MyPaletteDetail.module.css';
import getMyPalPaint from '../api/mypal/getMyPalPaint';
import MyPalHeader from '../components/mypalette/MyPalHeader';
import ProductUnitList from '../components/product/ProductUnitList';

export default function MyPaletteDetail() {
  const {pal_idx_pk} = useParams();
  const [palette,setPalette] = useState({});

  useEffect(()=>{
    async function fetchData() {
      console.log("성공?");
      await getMyPalPaint(pal_idx_pk).then(res => {
        console.log(res);
        setPalette(res.data)
    })
    }
    fetchData();
  },[])

  return (
    <div className={styles.wrapper_mypal}>
      <MyPalHeader pal_idx_pk={pal_idx_pk} pal_name={palette["pal_name"]} open={palette["open"]} page={"detail"}/>
      <div className={styles.content_mypal}>
        <div className={styles.img_title}><img/></div>
        <ProductUnitList paints={palette.paintVoList} pal_idx_pk={pal_idx_pk} page="MyPalDetail"/>
      </div>
    </div>
  );
}