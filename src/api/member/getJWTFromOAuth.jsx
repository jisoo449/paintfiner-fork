/** getJWTFromOAuth.jsx
 * 소셜로그인 성공 시 백엔드에게 사이트의 access token, refresh token을 요청
 */
import { apiInstance } from "..";

const api = apiInstance();

export default async function getJwtFromOauth(code, success, fail) {
  return await api.post("/member/accessCode", {"accessCode":code}).then(success).catch(fail);
}
