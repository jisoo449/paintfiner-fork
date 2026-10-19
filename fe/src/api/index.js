import axios from "axios";

//const base_URL = "http://localhost:3001";     // api 목 데이터
// const base_URL = "http://localhost:8080";        // 로컬 주소
const base_URL = "https://eb.paintfinder.art";

export const apiInstance = () => {

  // ✅ localStorage에서 Recoil-persist가 저장한 accessToken 가져오기
  const getAccessTokenFromStorage = () => {
    return localStorage.getItem("recoil-persist") // `recoil-persist`의 key 값
      ? JSON.parse(localStorage.getItem("recoil-persist"))?.accessTokenState
      : null;
  };

  const instance = axios.create({
    baseURL: base_URL,
    headers: {
      "Content-Type": "application/json",
    },
  });

  // ✅ 요청 인터셉터 설정 (항상 최신 accessToken을 읽어와 헤더에 추가)
  instance.interceptors.request.use(
    (config) => {
      const token = getAccessTokenFromStorage(); // 최신 accessToken 가져오기
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  return instance;
};
