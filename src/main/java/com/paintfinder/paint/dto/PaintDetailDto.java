package com.paintfinder.paint.dto;

import com.paintfinder.pigment.dto.PigmentForPaintDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PaintDetailDto {
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
    private String cate1KR;
    private String cate2EN;
    private String cate2KR;
    private String binder;
    private String unit;
    private int vegan;
    private String descriptionEN;
    private String descriptionKR;
    private String imgPath;
    private String textureImg1;
    private String textureImg2;
    private String textureImg3;

    private List<Float> size = new ArrayList<>();
    private List<String> prodImg = new ArrayList<>();
    private List<Float> price = new ArrayList<>();
    private List<PigmentForPaintDto> pigment = new ArrayList<>();

    @QueryProjection
    @Builder
    public PaintDetailDto(String paintCode, String brandEN, String brandKR, String paintNameEN, String paintNameKR,
                          String colorEN, String colorKR, String colorgroupEN, String colorgroupKR, String series,
                          String transparencyEN, String transparencyKR, String lightfastnessEN, String lightfastnessKR,
                          String qualityEN, String qualityKR, String textureEN, String textureKR,
                          String cate1EN, String cate1KR, String cate2EN, String cate2KR,
                          String binder, String unit, int vegan, String descriptionEN, String descriptionKR,
                          String textureImg1, String textureImg2, String textureImg3){
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
        this.descriptionEN = descriptionEN;
        this.descriptionKR = descriptionKR;
        this.textureImg1 = textureImg1;
        this.textureImg2 = textureImg2;
        this.textureImg3 = textureImg3;
    }

    public void addSize(Float size, String prodImg, Float price) {
        this.size.add(size);
        this.prodImg.add(prodImg);
        this.price.add(price);
    }
    public void addPigment(PigmentForPaintDto pigment) {
        this.pigment.add(pigment);
    }
}
