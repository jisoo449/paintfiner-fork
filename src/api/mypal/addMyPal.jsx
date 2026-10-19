import { apiInstance } from "..";

const api = apiInstance();

export default async function addMyPal(){
    const formData = new FormData();
    formData.append("myPalette", JSON.stringify({   
        "palName":"",
        "open":0,
        "bookmark":0,
        "paintList":[]
    }))
    
    return await api.post('/auth/mypal/addMypal',formData ,
        {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }
    );
}