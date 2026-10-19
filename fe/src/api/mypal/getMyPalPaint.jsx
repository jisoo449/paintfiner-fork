import {apiInstance} from '../index';

const api = apiInstance();

export default async function getMyPalPaint(pal_idx_pk) {
     const params = {"pal_idx_pk":pal_idx_pk}
     // return (await api.get('/mypal/mypal_paint_list',{ params:{'pal_idx_pk':pal_idx_pk} })).data;
     return (await api.get('/mypal_list', {params}));
}