import {atom,selector} from "recoil";
import { recoilPersist } from "recoil-persist";

const {persistAtom}=recoilPersist();

/**
 * 필터, 검색어창 등의 상태 변경에 사용됨
 * 선택된 카테고리에 따라 필터 값을 변화시키고, 검색어 입력 시에는 검색 창 상태 유지를, 타 카테고리나 페이지로 이동 시 검색창 초기화를 한다.
 */
export const selectedCategoryAtom = atom({
    key:"selectedCategoryAtom", // unique ID (다른 atoms/selectors을 구별하기 위해서)
    default: "", // default value (aka initial value)
    effects_UNSTABLE: [persistAtom],
});