package com.paintfinder.mypalette.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class MyPaletteUpdateRequestDto {
    private int palIdx;
    private String palName;
    private String imgSrc;
    private List<Integer> paintList;
}
