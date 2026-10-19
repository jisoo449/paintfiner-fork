import {apiInstance} from '../index';

const api = apiInstance();

export default async function delMyPalPaint(paintIdx, palIdx){
    return await api.delete('/auth/mypal/deleteMypalPaint',{data : { 
        "paintIdx": paintIdx,
        "palIdx": palIdx
     }})
}