import {apiInstance} from '../index';

const api = apiInstance();

export default async function getPaints(requestURL,success,fail){
    return (await api.get(requestURL).then(success).catch(fail));
    // return (await api.get('/paint',{params:{"page":1, "size":25}}).then(success).catch(fail));
    // return (await api.get('/get_paints',paramData).then(success).catch(fail)); //mock데이터용 api
}