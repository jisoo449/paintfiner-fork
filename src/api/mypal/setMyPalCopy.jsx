import {apiInstance} from '../index';

const api = apiInstance();

export default async function setMyPalCopy(palIdx){
    return await api.post(`auth/mypal/copyMypal/${palIdx}`)
}