import {apiInstance} from "../index";

const api = apiInstance();

export default async function delCompare(paintArr){
    await api.post("/common/delete_compare_paint",{"paintArr":[paintArr]}).then(console.log('호출'));
}