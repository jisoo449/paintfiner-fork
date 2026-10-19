import React, { useState } from 'react';
import setCompare from '../api/compare/setCompare';
import setMyPal from '../api/mypal/setMyPal';
import getCompare from '../api/compare/getCompare';
import getMyPalList from '../api/mypal/getMyPalList';
import getPaintDetail from '../api//paint/getPaintDetail';
import setMyPalPaint from '../api/mypal/setMyPalPaint';
import setMyPalCopy from '../api/mypal/setMyPalCopy';
import setMyPalUpdateOrder from '../api/mypal/setMyPalUpdateOrder';
import getMyPalPaint from '../api/mypal/getMyPalPaint';
import setMyPalUpdateName from '../api/mypal/setMyPalUpdateName';
import delMyPal from '../api/mypal/delMyPal';
import delMyPalPaint from '../api/mypal/delMyPalPaint';
import getPaints from '../api/paint/getPaints';
import axios from 'axios';

export default function ApiTest() {

  const [myPalVo,setMyPalVo]=useState();

  const onClicksetCompare = e => setCompare([2,3]).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClicksetMyPal = e => setMyPal("diamond",[1,3]).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClicksetMyPalPaint = e => setMyPalPaint(1, [0,2]).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClickgetCompare = e => getCompare('mumu@123').then(res=>console.log(res)).catch(err=>{throw Error(err)});
  
  const onClickgetMyPalList = e => getMyPalList('mumu@123').then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClickgetPaintDetail = e => getPaintDetail(1).then(res=>console.log(res)).catch(err=>{throw Error(err)});  

  const onClicksetMyPalCopy = e => setMyPalCopy(1).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClickDelMyPal = e => delMyPal(2).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClickDelMyPalPaint = e => delMyPalPaint(38,[1,2]).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClickMyPalUpdateName = e => setMyPalUpdateName(43,'chage0516', 1).then(res=>console.log(res)).catch(err=>{throw Error(err)});
    
  const onClicksetMyPalUpdateOrder = e => setMyPalUpdateOrder([47,48,49,46]).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  const onClickgetMyPalPaint = e => getMyPalPaint(44).then(res=>console.log(res)).catch(err=>{throw Error(err)});
  
  const realTest=()=>{
    axios.get('http://3.35.70.142:8080/get_show/test').then(res=>console.log(res)).cathch(err=>{throw Error(err)})
  }

  const onClickgetPaints = e => getPaints(1).then(res=>console.log(res)).catch(err=>{throw Error(err)});

  return(
    <div>
      <button onClick={onClicksetCompare}>setCompare</button><br/>
      <button onClick={onClickgetCompare}>getCompare</button><br/>
      <button onClick={onClicksetMyPal}>setMyPal</button><br/>
      <button onClick={onClickgetMyPalList}>getMyPal</button><br/>
      <button onClick={onClickgetPaints}>getPaints</button><br/>
      <button onClick={onClickgetPaintDetail}>getPaintDetail</button><br/>
      <button onClick={onClicksetMyPalPaint}>setMyPalPaint</button><br/>
      <button onClick={onClicksetMyPalCopy}>setMyPalCopy</button><br/>
      <button onClick={onClickMyPalUpdateName}>setMyPalUpdateName</button><br/>
      <button onClick={onClicksetMyPalUpdateOrder}>setMyPalUpdateOrder</button><br/>
      <button onClick={onClickgetMyPalPaint}>getMyPalPaint</button><br/>
      <button onClick={onClickDelMyPal}>delMyPal</button><br/>
      <button onClick={onClickDelMyPalPaint}>delMyPalPaint</button><br/>
      <button onClick={realTest}>realTest</button>
    </div>
  )
}