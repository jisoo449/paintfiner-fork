import { apiInstance } from "..";

/**
 * 
 * @param {*} palIdx 
 * @param {*} bookmark 
 * @param {*} isNewBookmark 
 * @returns 
 */
export default async function bookmarkMyPal(palIdx, bookmark, isNewBookmark){
    const api = apiInstance();
    return await api.put('/auth/mypal/bookmarkMypal',{
            "palIdx": palIdx,
            "bookmark": bookmark,
            "isNewBookmark": Boolean(isNewBookmark)
        }
    )
}