import { atom, selector } from "recoil";
import getMyPalList from "../api/mypal/getMyPalList";

// 1️⃣ 전역 상태 `palettes`
export const palettesState = atom({
  key: "palettesState",
  default: [],
});

// 3️⃣ 정렬 기준 상태
export const palOrderState = atom({
  key: "palOrderState",
  default: "pal_order,ASC",
});

// 4️⃣ API 호출을 위한 `selector`
export const fetchPalettesSelector = selector({
  key: "fetchPalettesSelector",
  get: async ({ get }) => {
    const order = get(palOrderState);
    const response = await getMyPalList(order);
    return response;
  },
});
