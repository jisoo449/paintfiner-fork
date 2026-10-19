import { apiInstance } from "..";

export default async function setOpenMyPal(palIdx, open){
    const api = apiInstance();
    return await api.put('/auth/mypal/openMypal', {
        "open": open,
        "palIdx": palIdx
      }
    )
}