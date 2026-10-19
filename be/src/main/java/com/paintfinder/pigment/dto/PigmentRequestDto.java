package com.paintfinder.pigment.dto;

import lombok.Data;

@Data
public class PigmentRequestDto {
    private int pigmentIdx;
    private String ciName;
    private String commonName;
    private String ciNumber;
    private String pigmentColor;
    private String opacity;
    private String lightfastness;
    private String toxicLevel;
}
