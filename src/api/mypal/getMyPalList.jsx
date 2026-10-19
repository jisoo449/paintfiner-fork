import {apiInstance} from '../index';

export default async function getMyPalList(palOrder="pal_order,ASC"){
    const api = apiInstance();
    return (await api.get('/auth/mypal/getMypalList?page=0&size=50&sort='+palOrder)).data.data.content;
}