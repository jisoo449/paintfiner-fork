import {atom,selector} from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom }=recoilPersist();

//userState 아톰
export const userState = atom({
    key:"userState", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: {
        state:false,
        email:"",
        nickname:""
    }, // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});

//userState 셀렉터
export const getUserState = selector({
    key:"getUserState",
    get: ({get}) => {
        return get(userState); //userState를 get을 통해 그대로 읽어 return한다
    }
});

/*
export const drawerState = atom({
    key: "drawerState",
    default: false,
});

export const loginModalState = atom({
    key: "loginModalState",
    default: false,
});

*/