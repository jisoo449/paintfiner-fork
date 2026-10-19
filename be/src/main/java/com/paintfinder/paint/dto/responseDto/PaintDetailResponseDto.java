package com.paintfinder.paint.dto.responseDto;

import com.paintfinder.pigment.dto.PigmentForPaintDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaintDetailResponseDto {
    private Integer paintIdx;
    private String paintCode;
    private String brandEN;
    private String brandKR;
    private String paintNameEN;
    private String paintNameKR;
    private String colorEN;
    private String colorKR;
    private String colorgroupEN;
    private String colorgroupKR;
    private String series;
    private String transparencyEN;
    private String transparencyKR;
    private String lightfastnessEN;
    private String lightfastnessKR;
    private String qualityEN;
    private String qualityKR;
    private String textureEN;
    private String textureKR;
    private String cate1EN;
    private String cate2EN;
    private String cate1KR;
    private String cate2KR;
    private String binder;
    private String unit;
    private Integer vegan;
    private Float price;
    private Float size;
    private String sizes;
    private String descriptionEN;
    private String descriptionKR;
    private String prodImg;
    private String textureImg1;
    private String textureImg2;
    private String textureImg3;
    private List<PigmentForPaintDto> pigments;

    @Builder
    @QueryProjection
    public PaintDetailResponseDto(
            Integer paintIdx,
            String paintCode,
            String brandEN,
            String brandKR,
            String paintNameEN,
            String paintNameKR,
            String colorEN,
            String colorKR,
            String colorgroupEN,
            String colorgroupKR,
            String series,
            String transparencyEN,
            String transparencyKR,
            String lightfastnessEN,
            String lightfastnessKR,
            String qualityEN,
            String qualityKR,
            String textureEN,
            String textureKR,
            String cate1EN,
            String cate1KR,
            String cate2EN,
            String cate2KR,
            String binder,
            String unit,
            Integer vegan,
            Float price,
            Float size,
            String sizes,
            String descriptionEN,
            String descriptionKR,
            String prodImg,
            String textureImg1,
            String textureImg2,
            String textureImg3,
            List<PigmentForPaintDto> pigments
    ){
        this.paintIdx = paintIdx;
        this.paintCode = paintCode;
        this.brandEN = brandEN;
        this.brandKR = brandKR;
        this.paintNameEN = paintNameEN;
        this.paintNameKR = paintNameKR;
        this.colorEN = colorEN;
        this.colorKR = colorKR;
        this.colorgroupEN = colorgroupEN;
        this.colorgroupKR = colorgroupKR;
        this.series = series;
        this.transparencyEN = transparencyEN;
        this.transparencyKR = transparencyKR;
        this.lightfastnessEN = lightfastnessEN;
        this.lightfastnessKR = lightfastnessKR;
        this.qualityEN = qualityEN;
        this.qualityKR = qualityKR;
        this.textureEN = textureEN;
        this.textureKR = textureKR;
        this.cate1EN = cate1EN;
        this.cate1KR = cate1KR;
        this.cate2EN = cate2EN;
        this.cate2KR = cate2KR;
        this.binder = binder;
        this.unit = unit;
        this.vegan = vegan;
        this.price = price;
        this.size = size;
        this.sizes = sizes;
        this.descriptionEN = descriptionEN;
        this.descriptionKR = descriptionKR;
        this.prodImg = prodImg;
        this.textureImg1 = textureImg1;
        this.textureImg2 = textureImg2;
        this.textureImg3 = textureImg3;
        this.pigments = pigments;
    }
}
