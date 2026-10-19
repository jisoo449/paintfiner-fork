import {atom,selector} from "recoil";
import { recoilPersist } from "recoil-persist";

const {persistAtom}=recoilPersist();

export const pageReqAtom = atom({
    key:"pageReqState", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: {
        size:25,
        page:0
    }, // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});

export const pageResAtom = atom({
    key:"pageResState", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: {
        totalElements:0,
        size:25,
        page:0
    }, // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});
