import {apiInstance} from '../index';

const api = apiInstance();

export default async function setMyPalUpdateName(palName, palIdx){
    return await api.put('/auth/mypal/updateMypalName',{
        'myPalName':palName,
        'palIdx':palIdx
    })
}