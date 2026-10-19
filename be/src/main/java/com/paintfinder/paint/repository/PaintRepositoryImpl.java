package com.paintfinder.paint.repository;

import com.paintfinder.paint.dto.PaintThumbnailDto;
import com.paintfinder.paint.dto.QPaintThumbnailDto;
import com.paintfinder.paint.dto.requestDto.PaintRequestDto;
import com.paintfinder.paint.dto.responseDto.PaintDetailResponseDto;
import com.paintfinder.paint.dto.responseDto.QPaintDetailResponseDto;
import com.paintfinder.paint.entity.QPaint;
import com.paintfinder.paint_pigment.entity.QPaint_Pigment;
import com.paintfinder.pigment.dto.PigmentForPaintDto;
import com.paintfinder.pigment.dto.QPigmentForPaintDto;
import com.paintfinder.pigment.entity.QPigment;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Pattern;

import static com.paintfinder.paint.entity.QPaint.paint;

// To-do: 가격, 세트

@Repository
@RequiredArgsConstructor
public class PaintRepositoryImpl implements PaintRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<PaintDetailResponseDto>> findPaintDetailByPaintCode(String paintCode) {
        QPaint paint = QPaint.paint;
        QPaint_Pigment paintPigment = QPaint_Pigment.paint_Pigment;
        QPigment pigment = QPigment.pigment;

        List<PaintDetailResponseDto> paintDetailResponseDtos = queryFactory
                .select(
                        new QPaintDetailResponseDto(
                                paint.paintIdx,
                                paint.paintCode,
                                paint.brandEN,
                                paint.brandKR,
                                paint.paintNameEN,
                                paint.paintNameKR,
                                paint.colorEN,
                                paint.colorKR,
                                paint.colorgroupEN,
                                paint.colorgroupKR,
                                paint.series,
                                paint.transparencyEN,
                                paint.transparencyKR,
                                paint.lightfastnessEN,
                                paint.lightfastnessKR,
                                paint.qualityEN,
                                paint.qualityKR,
                                paint.textureEN,
                                paint.textureKR,
                                paint.cate1EN,
                                paint.cate1KR,
                                paint.cate2EN,
                                paint.cate2KR,
                                paint.binder,
                                paint.unit,
                                paint.vegan,
                                paint.price,
                                paint.size,
                                paint.sizes,
                                paint.descriptionEN,
                                paint.descriptionKR,
                                paint.prodImg,
                                paint.textureImg1,
                                paint.textureImg2,
                                paint.textureImg3,
                                Expressions.constant(Collections.emptyList())
                        )
                )
                .from(paint)
                .where(paint.paintCode.eq(paintCode))
                .fetch();

        List<PigmentForPaintDto> pigments = queryFactory
                .select(new QPigmentForPaintDto(
                            pigment.pigmentIdx,
                            pigment.ciName,
                            pigment.ciNumber
                        )
                )
                .from(paintPigment)
                .leftJoin(pigment).on(paintPigment.pigment.eq(pigment))
                .where(paintPigment.paintCode.eq(paintCode))
                .fetch();

        for(int i = 0; i<paintDetailResponseDtos.size(); i++){
            paintDetailResponseDtos.get(i).setPigments(pigments);
        }

        return Optional.of(paintDetailResponseDtos);
    }

    /**
     * 검색 조건에 따라 물감 검색
     * @param paintRequestDto
     * @return
     */
    @Override
    public Page<PaintThumbnailDto> searchPaints(PaintRequestDto paintRequestDto, String category1, String category2, Pageable pageable) {

        QPaint paint = QPaint.paint;                                    // 물감 엔티티의 Q타입

        QPaint_Pigment paintPigment = QPaint_Pigment.paint_Pigment;     // 물감-안료 중계테이블의 Q타입
        QPigment pigment = QPigment.pigment;                            // 안료 엔티티의 Q타입

        // 1. 필터링/검색에 해당하는 paintThumbnails 탐색
        List<PaintThumbnailDto> results =  queryFactory
                                        .from(paint)
                                        .leftJoin(paintPigment).on(paint.paintCode.eq(paintPigment.paintCode))
                                        .leftJoin(paintPigment.pigment, pigment)
                                        .where(
                                                category1Eq(category1),
                                                category2Eq(category2),
                                                brandEq(paintRequestDto.getBrand()),
                                                qualityEq(paintRequestDto.getQuality()),
                                                textureEq(paintRequestDto.getTexture()),
                                                transparencyEq(paintRequestDto.getTransparency()),
                                                lightfastnessEq(paintRequestDto.getLightfastness()),
                                                colorgroupEq(paintRequestDto.getColorgroup()),
                                                sizeBetween(paintRequestDto.getStart_size(), paintRequestDto.getEnd_size()),
                                                unitEq(paintRequestDto.getUnit()),
                                                searchContains(paintRequestDto.getSearch())
                                        )
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .transform(GroupBy.groupBy(paint.paintCode).list(
                                                new QPaintThumbnailDto(
                                                        paint.paintCode,
                                                        paint.paintIdx,
                                                        paint.brandEN,
                                                        paint.brandKR,
                                                        paint.paintNameEN,
                                                        paint.paintNameKR,
                                                        paint.colorEN,
                                                        paint.colorKR,
                                                        paint.sizes,
                                                        paint.textureImg1,
                                                        paint.prodImg,
                                                        GroupBy.list(new QPigmentForPaintDto(
                                                                pigment.pigmentIdx,
                                                                pigment.ciName,
                                                                pigment.ciNumber
                                                        ))
                                                )
                                        ));

        // pigment 중 default값은 삭제하기
        for (PaintThumbnailDto paintThumbnailDto : results) {
            List<PigmentForPaintDto> pigments = paintThumbnailDto.getPigments();
            pigments.removeIf(pig -> pig.getPigmentIdx() == 0 && pig.getCiName() == null);      // removeIf를 사용하여 기본값 요소 제거
        }

        // 3. 총 개수 count
        long total = results.size();

        return new PageImpl<>(results, pageable, total);

    }

    /**
     * 대분류(유화, 수채화, etc)에 맞는 검색 결과 반환
     * @param category1
     * @return
     */
    private BooleanExpression category1Eq(String category1) {
        return category1==null? null : paint.cate1EN.eq(category1);
    }

    /**
     * 소분류(전통유화, 수성유화, etc)와 일치하는 검색 결과 반환
     * @param category2
     * @return
     */
    private BooleanExpression category2Eq(String category2) {
        return category2==null? null : paint.cate2EN.eq(category2);
    }

    //************************** 필터 ****************************************

    /**
     * 브랜드와 일치하는 결과 반환
     * @param brand
     * @return
     */
    private BooleanExpression brandEq(String brand) {

        if (brand == null) {
            return null;
        }

        // brand가 한국어인 경우
        if (isKorean(brand)) {
            return paint.brandKR.eq(brand);
        }

        // brand가 영어인 경우
        else {
            return paint.brandEN.eq(brand);
        }

    }

    /**
     * 퀄리티 값과 일치하는 결과 반환
     * @param quality
     * @return
     */
    private BooleanExpression qualityEq(String quality) {

        if (quality == null) {
            return null;
        }

        // quality가 한국어인 경우
        if (isKorean(quality)) {
            return paint.qualityKR.eq(quality);
        }

        // quality 영어인 경우
        else {
            return paint.qualityEN.eq(quality);
        }
    }

    /**
     * 텍스처 값이 일치하는 결과 반환
     * @param texture
     * @return
     */
    private BooleanExpression textureEq(String texture) {

        if (texture == null) {
            return null;
        }

        // 텍스처 값이 한국어인 경우
        if (isKorean(texture)) {
            return paint.textureKR.eq(texture);
        }

        // 텍스처 값이 영어인 경우
        else {
            return paint.textureEN.eq(texture);
        }
    }

    /**
     * 투명도 값이 일치하는 결과 반환
     * @param transparency
     * @return
     */
    private BooleanExpression transparencyEq(String transparency) {

        if (transparency == null) {
            return null;
        }

        // 텍스처 값이 한국어인 경우
        if (isKorean(transparency)) {
            return paint.transparencyKR.eq(transparency);
        }

        // 텍스처 값이 영어인 경우
        else {
            return paint.transparencyEN.eq(transparency);
        }
    }

    /**
     * 내광성 값이 일치하는 결과 반환
     * @param lightfastness
     * @return
     */
    private BooleanExpression lightfastnessEq(String lightfastness) {

        if (lightfastness == null) {
            return null;
        }

        // 텍스처 값이 한국어인 경우
        if (isKorean(lightfastness)) {
            return paint.lightfastnessKR.eq(lightfastness);
        }

        // 텍스처 값이 영어인 경우
        else {
            return paint.lightfastnessEN.eq(lightfastness);
        }
    }

    /**
     * 색상값 일치하는 결과물 반환
     * @param colorgroup
     * @return
     */
    private BooleanExpression colorgroupEq(String colorgroup) {

        if (colorgroup == null) {
            return null;
        }

        // 텍스처 값이 한국어인 경우
        if (isKorean(colorgroup)) {
            return paint.colorgroupKR.eq(colorgroup);
        }

        // 텍스처 값이 영어인 경우
        else {
            return paint.colorgroupEN.eq(colorgroup);
        }
    }

    private BooleanExpression sizeBetween(float startSize, float endSize) {

        if (startSize > 0 && endSize > 0) {
            return paint.size.between(startSize, endSize);
        } else {
            return null;
        }
    }

    private BooleanExpression unitEq(String unit) {
        return unit != null ? paint.unit.eq(unit) : null;
    }

//    private BooleanExpression priceBetween(float startPrice, float endPrice) {
//        return (startPrice > 0 && endPrice > 0) ? paint.price.between(startPrice, endPrice) : null;
//    }

//    private BooleanExpression setsEq(int sets) {
//        return sets >= 0 ? QSets_Paint.sets.eq(sets) : null;
//    }

    private BooleanExpression searchContains(String search) {
        return search != null ? paint.cate1KR.containsIgnoreCase(search)
                .or(paint.cate1EN.containsIgnoreCase(search))
                .or(paint.cate2KR.containsIgnoreCase(search))
                .or(paint.cate2EN.containsIgnoreCase(search))
                .or(paint.brandKR.containsIgnoreCase(search))
                .or(paint.brandEN.containsIgnoreCase(search))
                .or(paint.qualityKR.containsIgnoreCase(search))
                .or(paint.qualityEN.containsIgnoreCase(search))
                .or(paint.textureKR.containsIgnoreCase(search))
                .or(paint.textureEN.containsIgnoreCase(search))
                .or(paint.transparencyKR.containsIgnoreCase(search))
                .or(paint.transparencyEN.containsIgnoreCase(search))
                .or(paint.lightfastnessKR.containsIgnoreCase(search))
                .or(paint.lightfastnessEN.containsIgnoreCase(search))
                .or(paint.colorgroupKR.containsIgnoreCase(search))
                .or(paint.colorgroupEN.containsIgnoreCase(search)): null;
    }

    /**
     * 입력값이 한글인지 영어인지 판별
     * @param str
     * @return
     */
    private Boolean isKorean(String str){
        return Pattern.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*", str);
    }
}
