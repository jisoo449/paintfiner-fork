import {apiInstance} from './index';

//Todo: 백에 맞춰 api 수정 필요

export const homeApis = () =>{

    const api = apiInstance();

    /**
     * 홈에서 보일 이미지-팔레트 데이터
     * @returns 
     */
    const getImgPal = async(success,fail) => {
        return await api.get('/mypal/getRandomPaletteList').then(success).catch(fail);
    }

    return {getImgPal};
}