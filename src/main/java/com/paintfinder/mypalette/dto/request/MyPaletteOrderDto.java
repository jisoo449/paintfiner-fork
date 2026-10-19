package com.paintfinder.mypalette.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPaletteOrderDto {
    private int palIdx;
    private int changeOrder;

    public MyPaletteOrderDto(int palIdx, int changeOrder) {
        this.palIdx = palIdx;
        this.changeOrder = changeOrder;
    }
}
