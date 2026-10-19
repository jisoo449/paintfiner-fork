package com.paintfinder.mypalette.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPalettePaintOrderDto {
    private int palIdx;
    private int paintIdx;
    private int changeOrder;

    @Builder
    public MyPalettePaintOrderDto(int palIdx, int paintIdx, int changeOrder) {
        this.palIdx = palIdx;
        this.paintIdx = paintIdx;
        this.changeOrder = changeOrder;
    }
}
