import {atom,selector} from "recoil";
import { recoilPersist } from "recoil-persist";

const {persistAtom}=recoilPersist();

export const testUserState = atom({
    key:"testUserState", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: {
        state:true,
        email:"mumu@123",
        nickname:"mumu"
    }, // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});

export const gettestUserState = selector({
    key:"gettestUserState",
    get: ({get}) => {
        return get(testUserState); //testUserState를 get을 통해 그대로 읽어 return한다
    }
});