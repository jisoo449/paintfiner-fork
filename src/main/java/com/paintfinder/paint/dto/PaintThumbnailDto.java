package com.paintfinder.paint.dto;

import com.paintfinder.pigment.dto.PigmentForPaintDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class PaintThumbnailDto {

    @Setter(AccessLevel.NONE)
    private String paintCode;          // 물감 종류지시 코드

    private Integer paintIdx;       //물감 인덱스
    private String brandEN;			//브랜드
    private String brandKR;			//브랜드
    private String paintNameEN;		//물감이름
    private String paintNameKR;		//물감이름
    private String colorEN;           //물감 색 이름
    private String colorKR;           //물감 색 이름

    private String sizes;                   //용량  	//null
    private String textureImg;            // 물감 질감 이미지
    private String prodImg;                //물감 제품 사진
//    private List<Float> price = new ArrayList<>();             // 물감의 가격

    private List<PigmentForPaintDto> pigments = new ArrayList<>();  //안료 목록

    @QueryProjection
    public PaintThumbnailDto(String paintCode, Integer paintIdx, String brandEN, String brandKR, String paintNameEN,
                             String paintNameKR, String colorEN, String colorKR,
                             String sizes, String textureImg, String prodImg, List<PigmentForPaintDto> pigments){
        this.paintCode = paintCode;
        this.paintIdx = paintIdx;
        this.brandEN = brandEN;
        this.brandKR = brandKR;
        this.paintNameEN = paintNameEN;
        this.paintNameKR = paintNameKR;
        this.colorEN = colorEN;
        this.colorKR = colorKR;
        this.sizes = sizes;
        this.textureImg = textureImg;
        this.prodImg = prodImg;
        this.pigments = pigments;
    }

//    public void addSize(Float size, String prodImg, Float price) {
//        this.size.add(size);
//        this.prodImg.add(prodImg);
//        this.price.add(price);
//    }

    public void addPigments(PigmentForPaintDto pigments) {  
        this.pigments.add(pigments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaintThumbnailDto that = (PaintThumbnailDto) o;
        return Objects.equals(brandEN, that.brandEN) &&
                Objects.equals(brandKR, that.brandKR) &&
                Objects.equals(paintNameEN, that.paintNameEN) &&
                Objects.equals(paintNameKR, that.paintNameKR) &&
                Objects.equals(colorEN, that.colorEN) &&
                Objects.equals(colorKR, that.colorKR) &&
                Objects.equals(sizes, that.sizes) &&
                Objects.equals(textureImg, that.textureImg) &&
                Objects.equals(prodImg, that.prodImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandEN, brandKR, paintNameEN, paintNameKR, colorEN, colorKR, sizes, textureImg, prodImg);
    }
}
