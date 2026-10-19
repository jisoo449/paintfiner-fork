package com.paintfinder.mypalette.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class MyPaletteRequestDto {
    private String palName;
    private int open;
    private int bookmark;
    private List<Integer> paintList;
}
