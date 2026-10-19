import {apiInstance} from '../index';

const api = apiInstance();

export default async function getCompare(email){
    //return (await api.post('/common/get_compareList')).data;
    return (await api.get('/getCompareList')).data;
}//,{params:{'email_fk':email}}