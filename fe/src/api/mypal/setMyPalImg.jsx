import { apiInstance } from "..";

export default async function setMypalImg(imgFile, palIdx){
    const api = apiInstance();

    const formData = new FormData();
    formData.append("img", imgFile);  // 이미지 파일 추가

    // multipart 파일(사진) 이 필요하기에 헤더에 멀티파트/폼데이터 정보 포함
    return (await api.put(`/auth/mypal/updateMypalImg?myPaletteIdx=${palIdx}`, formData,
        {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }
    ))
}