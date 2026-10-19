import {atom,selector} from "recoil";
import { recoilPersist } from "recoil-persist";

const {persistAtom}=recoilPersist();

export const paintFilterAtom = atom({
    key:"paintFilterState", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: {
        search:"",
        brand:"",
        quality:"",
        texture:"",
        transparency:"",
        lightfastness:"",
        colorgroup:"",
        start_size:0,
        end_size:0,
        unit:"",
        sets:0
    }, // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});
