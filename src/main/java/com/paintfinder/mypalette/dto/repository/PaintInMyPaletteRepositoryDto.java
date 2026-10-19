package com.paintfinder.mypalette.dto.repository;

import com.paintfinder.pigment.dto.PigmentForPaintDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 마이팔레트 내의 페인트 정보를 담을 페인트 dto.
 */
@Data
public class PaintInMyPaletteRepositoryDto {
    // 인덱스
    private Integer paintIdx;

    // 페인트코드
    private String paintCode;

    // 브랜드명
    private String brandEN;
    private String brandKR;

    // 페인트이름
    private String paintNameEN;
    private String paintNameKR;

    // 색 이름
    private String colorEN;
    private String colorKR;

    // TODO: 안료 목록, 용량도 만들어야 함.
    // 용량
    private Float size;

    // 용량 단위
    private String unit;

    private String prodImg;

    private String textureImg1;

    private Integer paintOrder;

    // 안료 목록
    private List<PigmentForPaintDto> pigments;

    @Builder
    @QueryProjection
    public PaintInMyPaletteRepositoryDto(Integer paintIdx, String paintCode, String brandEN, String brandKR, String paintNameEN, String paintNameKR, String colorEN, String colorKR, Float size, String unit, String prodImg, String textureImg1, Integer paintOrder, List pigments){
        this.paintIdx = paintIdx;
        this.paintCode = paintCode;
        this.brandEN = brandEN;
        this.brandKR = brandKR;
        this.paintNameEN = paintNameEN;
        this.paintNameKR = paintNameKR;
        this.colorEN = colorEN;
        this.colorKR = colorKR;
        this.size = size;
        this.unit = unit;
        this.prodImg = prodImg;
        this.textureImg1 = textureImg1;
        this.paintOrder = paintOrder;
        this.pigments = pigments;
    }
}
