import React, { useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import getPaintDetail from '../api/paint/getPaintDetail';
import styles from './PaintDetail.module.css';
import Comments from '../components/common/Comments';
import PaintImgSelector from '../components/paintDetail/PaintImgSelector';
import PaintSpecification from '../components/paintDetail/PaintSpecification';
import PaintDescription from '../components/paintDetail/PaintDescription';

export default function PaintDetail() {
  const { paintIdx } = useParams();
  const [paint, setPaint] = useState({ pigment: [] });
  const [paints, setPaints] = useState({});
  const [loading, setLoading] = useState(true);
  
  async function fetchData() {
    
    await getPaintDetail(paintIdx)
      .then(res => {
        console.log("통 성 ", res.data.data);
        setLoading(false);

        setPaint(res.data.data.find(item => item.paintIdx === Number(paintIdx)))
        setPaints(res.data.data.sort((a, b) => a.size - b.size))
        
      })
      .catch(error => {
        console.log(error)
      })
  }

  useEffect(()=>{
    fetchData();
  },[paintIdx])
  

  if (loading || !paint) {
    return <p>Loading...</p>;
  }

  return (
    <div className={styles.wrapper_spec}>
      <div className='제품 영역'>
        <PaintImgSelector paint={paint}/>
        <PaintSpecification paint={paint} setPaint={setPaint} paints={paints}/>
      </div>
      <PaintDescription paint={paint}/>
      <Comments/>
    </div>

  );
}