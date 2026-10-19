package com.paintfinder.pigment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Data
public class PigmentForPaintDto {
//    @Setter(AccessLevel.NONE)
    private Integer pigmentIdx;
    private String ciName;
    private String ciNumber;

    @Builder
    @QueryProjection
    public PigmentForPaintDto(Integer pigmentIdx, String ciName, String ciNumber){
        this.pigmentIdx = pigmentIdx;
        this.ciName = ciName;
        this.ciNumber = ciNumber;
    }
}
