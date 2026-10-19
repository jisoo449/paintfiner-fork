package com.paintfinder.mypalette.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class MyPalettePaintAddDto {
    private int palIdx;
    private List<Integer> paintIdxList;
}
