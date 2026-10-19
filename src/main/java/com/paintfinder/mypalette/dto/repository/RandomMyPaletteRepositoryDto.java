package com.paintfinder.mypalette.dto.repository;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class RandomMyPaletteRepositoryDto {
    private int palIdx;
    private String palName;
    private int palOrder;
    private int open;
    private String imgSrc;
    private int bookmark;
    private int memberIdx;
    private String memberNickname;
    private List<PaintInMyPaletteRepositoryDto> paintList;

    @Builder
    @QueryProjection
    public RandomMyPaletteRepositoryDto(int palIdx, String palName, int palOrder, int open, String imgSrc, int bookmark, int memberIdx, String memberNickname, List<PaintInMyPaletteRepositoryDto> paintList) {
        this.palIdx = palIdx;
        this.palName = palName;
        this.palOrder = palOrder;
        this.open = open;
        this.imgSrc = imgSrc;
        this.bookmark = bookmark;
        this.memberIdx = memberIdx;
        this.memberNickname = memberNickname;
        this.paintList = paintList;
    }
}
