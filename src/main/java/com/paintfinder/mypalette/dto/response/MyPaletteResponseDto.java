package com.paintfinder.mypalette.dto.response;

import com.paintfinder.palette_paint.entity.Palette_Paint;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MyPaletteResponseDto {
    private int palIdx;
    private String palName;
    private int palOrder;
    private int open;
    private String imgSrc;
    private int bookmark;
//    private List<Palette_Paint> palettePaintsList;

    @Builder
    @QueryProjection
    public MyPaletteResponseDto(int palIdx, String palName, int palOrder, int open, String imgSrc, int bookmark
//            , List<Palette_Paint> palettePaintsList
    ){
        this.palIdx = palIdx;
        this.palName = palName;
        this.palOrder = palOrder;
        this.open = open;
        this.imgSrc = imgSrc;
        this.bookmark = bookmark;
//        this.palettePaintsList = palettePaintsList;
    }
}
