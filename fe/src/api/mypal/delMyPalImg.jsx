import { apiInstance } from "..";

export default async function deleteMypalImg(palIdx){
    const api = apiInstance();

    return await api.delete(`/auth/mypal/deleteMypalImg?palIdx=${palIdx}`);
}