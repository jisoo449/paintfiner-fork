//비교 리스트에 물감 추가.
//ProductUnit에서 호출
//3.35.70.142
import {apiInstance} from '../index';

const api = apiInstance();

export default async function setCompare(paintArr){
    await api.put('/common/insert_compare_paint',{'paintArr':paintArr});
}