# paint finder frontend
- 사용기술: react, react-router-dom, tanstack-query

## 0. 사전준비사항
- nodejs 설치: 18.13.0버전 이상권장, 그이하버전에서 실행해보진 않음

## 1. 실행방법
```bash
# 의존성 라이브러리 설치
npm install

# 실행
npm run start
```

## 2. 배포방법
```bash
npm run build

# public 파일들 잘 배포... 추후작성
```

## 3. 기타
### 3.1. mockup 데이터
mockup 데이터는 json-server를 사용해 띄운다. 이후 요청을 보낼 baseUrl을 기존의 db가 아닌 json-server를 돌리는 url 및 포트로 수정한다.  
mockup 데이터를 사용해 실제 백엔드처럼 동작하게 하는 방법은 다음과 같다.  
0. json-server 설치
   ```
   npm i -g json-server
   ```
1. json파일이 있는 위치에서 json-server 돌리기
   ```
   json-server {json파일 이름}.json --port 3001
   ```
2. `/src/api/index.js`에서 설정한 baseUrl을 수정
   ```
   const base_URL = "http://localhost:3001";
   //const base_URL = "http://3.35.70.142:8080";
   ```
3. 수정하고자 하는 api의 요청과 일치하게 json파일에 데이터 추가하기  
   만약 <code>return (await api.get('/get_paints',paramData).then(success).catch(fail));</code>와 같은 요청을 보내는 api가 있다면 json파일에 다음과 같이 값을 추가하라.
   ```
   {
     "get_paints": [
       {
         "idx_pk": 0,
         "paint_name": "트레디셔널 오일 페인트",
         "color_code": "", . . . 
       }
     ]
   }
   ```
   즉, 요청하는 경로로 key를, 받아와야 하는 값으로 value를 설정하라.
   만약 api의 요청이 `api.get('/api/get_paints',paramData)`처럼 다중으로 되어 있다면 테스트를 위해 해당 경로를 단일 경로로 수정하라.

   테스트
