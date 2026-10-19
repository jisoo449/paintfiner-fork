package com.paintfinder.sets.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetsForPaintDetailDto {
    private int setsIdx;
    private String setsNameEN;
    private String setsNameKR;

    @QueryProjection
    public SetsForPaintDetailDto(int setsIdx, String setsNameEN, String setsNameKR){
        this.setsIdx = setsIdx;
        this.setsNameEN = setsNameEN;
        this.setsNameKR = setsNameKR;
    }
}
