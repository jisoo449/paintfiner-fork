/**
 * setupInterceptors.js
 * API 설정. 
 * axios 인스턴스 생성 후 호출
 */

import { getCookie } from "../utils/cookie";

export const setupInterceptors = (apiInstance, accessToken) => {

  // 요청 인터셉터
  apiInstance.interceptors.request.use(
    (config) => {
      if (accessToken) {                                              // accessToken이 있다면 헤더에 accessToken 담기
        config.headers.Authorization = `Bearer ${accessToken}`;     
      }
      else if (getCookie('refreshToken')){                            // 없다면 refreshToken 담기
        const refreshToken = getCookie('refreshToken');
        config.headers.Authorization = `Bearer ${refreshToken}`;  
      }
      return config;
    },
    (error) => Promise.reject(error)
  );
  
  //  응답 인터셉터
  apiInstance.interceptors.response.use(
      (response) => {
          return response;
      },
      (error) => {
        //return Promise.reject(error);
        const errorCode = error.response.status;

        switch(errorCode) {
          //사용자 권한 에러
          case 401:
            // To-do: 서버와 협의하여 상황별 에러코드를 정하고, 각 상황(에러코드)에 따라 헤더에 저장할 값 정하기
            const refreshToken = getCookie('refreshToken');
            break;

          //Not Found Error
          case 404:
            window.location.href='/error404';
            break;
          
          case 500:
            window.location.href='/error500';
            break;

          default:
            window.location.href='/error';
            break;

        }
  
        return Promise.reject(error);  // ✅ 여기서 reject해야 try-catch가 정상적으로 동작함
      }
  );
};