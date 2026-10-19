import {apiInstance} from '../index';

const api = apiInstance();

export default async function setMyPalUpdateOrder(palIdx, changeOrder){
    return await api.put('/auth/mypal/updateMypalOrder',{
        'changeOrder':changeOrder,
        'palIdx':palIdx
    });
}