import {apiInstance} from '../index';

const api = apiInstance();

/**
 * 기존 마이팔레트에 물감 추가
 * @param {*} palIdx
 * @param {*} paintArr 
 * @returns 
 */
export default async function setMyPalPaint(palIdx,paintArr){
    return await api.post('/auth/mypal/addMypalPaints',{ "palIdx": palIdx, "paintIdxList":paintArr })
}   //data=1, status=200이면 성공. 아니면 에러.