import {atom,selector} from "recoil";
import { recoilPersist } from "recoil-persist";

const {persistAtom}=recoilPersist();

export const compareState = atom({
    key:"compareState", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: [], // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});