import {apiInstance} from '../index';

const api = apiInstance();

export default async function getPaintDetail(paintIdx) {
    console.log("페인트idx ", paintIdx);
    return await api.get(`paint/getPaintDetail`,{params:{"paintIdx":paintIdx}});
}