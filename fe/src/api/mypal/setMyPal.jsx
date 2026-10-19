import {apiInstance} from '../index';

const api = apiInstance();

/**
 * 새 팔레트 생성 및 페인트 추가 API
 * @param {*} paintIdx 물감 인덱스
 * @param {*} newPaletteName 새로 생성한 팔레트 이름
 * @param {*} whetherOpen 공개/비공개 여부
 * @returns 
 */
export default async function setMyPal(paintIdx, newPaletteName, whetherOpen){

    const formData = new FormData();
    formData.append("myPalette", JSON.stringify({   
        "palName":newPaletteName,
        "open": whetherOpen,
        "bookmark":0,
        "paintList":[paintIdx]
    }))
    
    return await api.post('/auth/mypal/addMypal',formData ,
        {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }
    );
}