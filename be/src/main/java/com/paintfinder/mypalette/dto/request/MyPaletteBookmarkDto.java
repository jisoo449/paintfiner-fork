package com.paintfinder.mypalette.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @palIdx  :  팔레트 idx
 * @bookmark : 북마크 순서. 북마크 해제, 신규 등록시 0, 북마크 내 순서 변경시 1 ~ 5
 * @isNewBookmark : 신규 북마크 등록 상태 체크용 boolean. 신규등록이면 true, 아니면 false
 */
@Data
@NoArgsConstructor
public class MyPaletteBookmarkDto {
    private int palIdx;
    private int bookmark;
    private Boolean isNewBookmark;

    public MyPaletteBookmarkDto(int palIdx, int bookmark, Boolean isNewBookmark) {
        this.palIdx = palIdx;
        this.bookmark = bookmark;
        this.isNewBookmark = isNewBookmark;
    }
}
