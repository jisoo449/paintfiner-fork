import {apiInstance} from '../index';

const api = apiInstance();

export default async function delMyPal(palIdx){
    return await api.delete('/auth/mypal/deleteMypal',{
        data: [palIdx]
    });
}   