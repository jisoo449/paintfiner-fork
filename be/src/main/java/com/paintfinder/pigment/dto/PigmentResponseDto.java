package com.paintfinder.pigment.dto;

import com.paintfinder.pigment.entity.Pigment;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Data
@RequiredArgsConstructor
public class PigmentResponseDto {
    private int pigmentIdx;
    private String ciName;
    private String commonName;
    private String pigmentColor;
    private String opacity;
    private String lightfastness;
    private String toxicLevel;

    @Builder
    public PigmentResponseDto(Pigment pigment){
        this.pigmentIdx = pigment.getPigmentIdx();
        this.ciName = pigment.getCiName();
        this.commonName = pigment.getCommonName();
        this.pigmentColor = pigment.getPigmentColor();
        this.opacity = pigment.getOpacity();
        this.lightfastness = pigment.getLightfastness();
        this.toxicLevel = pigment.getToxicLevel();
    }
}
