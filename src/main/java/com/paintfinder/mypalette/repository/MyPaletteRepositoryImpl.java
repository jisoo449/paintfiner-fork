package com.paintfinder.mypalette.repository;

import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.global.util.QueryDslSortingUtil;
import com.paintfinder.member.entity.Member;
import com.paintfinder.mypalette.dto.repository.*;
import com.paintfinder.mypalette.dto.request.MyPaletteBookmarkDto;
import com.paintfinder.mypalette.dto.request.MyPaletteOpenDto;
import com.paintfinder.mypalette.dto.request.MyPaletteOrderDto;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.mypalette.entity.QMyPalette;
import com.paintfinder.mypalette.error.MyPaletteErrorCode;
import com.paintfinder.paint.entity.QPaint;
import com.paintfinder.paint_pigment.entity.QPaint_Pigment;
import com.paintfinder.palette_paint.entity.QPalette_Paint;
import com.paintfinder.pigment.dto.PigmentForPaintDto;
import com.paintfinder.pigment.entity.QPigment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MyPaletteRepositoryImpl implements MyPaletteRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final int maxBookmarkCount = 3;

    @Override
    public Page<MyPaletteRepositoryDto> findMyPaletteDetail(int memberIdx, Pageable pageable) {

        QMyPalette myPalette = QMyPalette.myPalette;
        QPalette_Paint palettePaint = QPalette_Paint.palette_Paint;
        QPaint paint = QPaint.paint;
        QPigment pigment = QPigment.pigment;
        QPaint_Pigment paintPigment = QPaint_Pigment.paint_Pigment;

        // 데이터 총 개수
        long total = jpaQueryFactory
                .from(myPalette)
                .where(myPalette.member.memberIdx.eq(memberIdx))
                .fetchCount();

        //------- 동적 정렬 방법 part -----------//
        // PathBuilder 설정
        PathBuilder<MyPalette> entityPath = new PathBuilder<>(myPalette.getType(), myPalette.getMetadata());

        // QueryDslSortUtil 사용
        OrderSpecifier<?> orderSpecifiers = QueryDslSortingUtil.getOrderSpecifiers(pageable.getSort(), entityPath);
        //------- 동적 정렬 방법 끝 -----------//


        // 정렬할 기준이 적인 문자열 파싱
        String orderCriterion = pageable.getSort().toString().split(":")[0];
        String orderBy = pageable.getSort().toString().split(":")[1];

        // where 절 조회 조건에 따라 다른 조건문 BooleanBuilder 에 저장
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(myPalette.member.memberIdx.eq(memberIdx));
        switch (orderCriterion) {
            case "pal_order":
                whereClause.and(myPalette.bookmark.eq(0));
                break;
            case "bookmark":
                whereClause.and(myPalette.bookmark.between(1, maxBookmarkCount));
                break;
            case "open":
                if ("ASC".equals(orderBy.trim())) { // 공개 팔레트 조회(asc)
                    whereClause.and(myPalette.open.eq(1));
                } else { // 비공개 팔레트 조회 (desc)
                    whereClause.and(myPalette.open.eq(0));
                }
                break;
            default:
                break;
        }

        // myPalette 리스트를 limit 과 offset 기준으로 가져오기. 나누어야 myPalette 기준으로 제대로 페이징 적용됨.
        List<Integer> palIdxList = jpaQueryFactory
                .select(myPalette.palIdx)
                .from(myPalette)
                .where(whereClause)  // 동적 where 절 적용
                .offset(pageable.getOffset())   // offset : 건너뛸 레코드 수
                .limit(pageable.getPageSize())  // pageSize : 한 페이지에 표시할 갯수
//              .orderBy(myPalette.palIdx.asc())// 정적 정렬 방법
                .orderBy(orderSpecifiers)       // 동적 정렬 적용
                .fetch();

        // 🔹 1단계: myPalIdx에 해당하는 팔레트 정보 조회 (paintList는 빈 값)
        List<MyPaletteRepositoryDto> myPaletteList = jpaQueryFactory
                .from(myPalette)
                .where(myPalette.palIdx.in(palIdxList))
                .orderBy(orderSpecifiers)
                .transform(GroupBy.groupBy(myPalette.palIdx).list(
                        new QMyPaletteRepositoryDto(
                                myPalette.palIdx,
                                myPalette.palName,
                                myPalette.palOrder,
                                myPalette.open,
                                myPalette.imgSrc,
                                myPalette.bookmark,
                                myPalette.createDate,
                                myPalette.lastEditDate,
                                Expressions.constant(Collections.emptyList()) // 🚀 빈 리스트로 초기화
                        ))
                );

        // 🔹 2단계: Palette_Paint 테이블에서 myPalIdx에 해당하는 페인트 목록 조회
        Map<Integer, List<PaintInMyPaletteRepositoryDto>> palettePaintMap = jpaQueryFactory
                .from(palettePaint)
                .leftJoin(palettePaint.paint, paint)
                .where(palettePaint.myPalette.palIdx.in(palIdxList))
                .transform(GroupBy.groupBy(palettePaint.myPalette.palIdx).as(
                        GroupBy.list(Projections.constructor(PaintInMyPaletteRepositoryDto.class,
                                paint.paintIdx,
                                paint.paintCode,
                                paint.brandEN,
                                paint.brandKR,
                                paint.paintNameEN,
                                paint.paintNameKR,
                                paint.colorEN,
                                paint.colorKR,
                                paint.size,
                                paint.unit,
                                paint.prodImg,
                                paint.textureImg1,
                                palettePaint.ppPaintOrder,
                                Expressions.constant(Collections.emptyList()) // pigments는 빈 값으로 초기화
                        ))
                ));

//        for(Map.Entry<Integer, List<PaintInMyPaletteRepositoryDto>> entry : palettePaintMap.entrySet()) {
//            List<PaintInMyPaletteRepositoryDto> sortedList = entry.getValue().stream()
//                    .sorted(Comparator.comparingInt(PaintInMyPaletteRepositoryDto::getPaintOrder))
//                    .toList();
//
//            entry.setValue(sortedList);
//        }

        // 🔹 Java에서 paintList 채우기
        myPaletteList.forEach(palette ->
                palette.setPaintList(palettePaintMap.getOrDefault(palette.getPalIdx(), new ArrayList<>()))
        );

        // 🔹 3단계: paintList 내의 paintIdx를 기준으로 Paint_Pigment 테이블 조회
        List<Integer> paintIds = myPaletteList.stream()
                .flatMap(palette -> palette.getPaintList().stream().map(PaintInMyPaletteRepositoryDto::getPaintIdx))
                .distinct()
                .collect(Collectors.toList());

        List<String> paintCodes = jpaQueryFactory
                .select(paint.paintCode)
                .from(paint)
                .where(paint.paintIdx.in(paintIds))
                .fetch();

        Map<String, List<PigmentForPaintDto>> paintPigmentMap = jpaQueryFactory
                .from(paintPigment)
                .leftJoin(paintPigment.pigment, pigment)
//                .where(paintPigment.paint.paintIdx.in(paintIds))
                .where(paintPigment.paintCode.in(paintCodes))
//                .transform(GroupBy.groupBy(paintPigment.paint.paintIdx).as(
                .transform(GroupBy.groupBy(paintPigment.paintCode).as(
                        GroupBy.list(Projections.constructor(PigmentForPaintDto.class,
                                pigment.pigmentIdx,
                                pigment.ciName,
                                pigment.ciNumber
                        ))
                ));

        // 🔹 Java에서 pigments 채우기
        myPaletteList.forEach(palette ->
                palette.getPaintList().forEach(paintList ->
                        paintList.setPigments(paintPigmentMap.getOrDefault(paintList.getPaintCode(), new ArrayList<>()))
                )
        );

        return new PageImpl<>(myPaletteList, pageable, total);
    }

    @Override
    public boolean updateMypaletteOrder(Member member, MyPaletteOrderDto myPaletteOrderDto) {
        QMyPalette myPalette = QMyPalette.myPalette;

        // 기존 순서 가져오기
        Integer currentOrder = jpaQueryFactory
                .select(myPalette.palOrder)
                .from(myPalette)
                .where(myPalette.palIdx.eq(myPaletteOrderDto.getPalIdx())
                        .and(myPalette.member.eq(member)))
                .fetchOne();

        if(currentOrder == null) return false;

        // 순서 변경로직: 기존 순서의 나머지 값들을 변경
        // 순서를 앞으로 변경 시
        if(currentOrder > myPaletteOrderDto.getChangeOrder()) {
            jpaQueryFactory.update(myPalette)
                    .set(myPalette.palOrder, myPalette.palOrder.add(1))
                    .where(myPalette.member.eq(member)
                            .and(myPalette.palOrder.between(myPaletteOrderDto.getChangeOrder(), currentOrder - 1)))
                    .execute();
        }
        // 순서를 뒤로 보낼 시
        else {
            jpaQueryFactory.update(myPalette)
                    .set(myPalette.palOrder, myPalette.palOrder.add(-1))
                    .where(myPalette.member.eq(member)
                            .and(myPalette.palOrder.between(currentOrder + 1, myPaletteOrderDto.getChangeOrder())))
                    .execute();
        }

        // 원하는 항목의 순서 변경
        jpaQueryFactory.update(myPalette)
                .set(myPalette.palOrder, myPaletteOrderDto.getChangeOrder())
                .where(myPalette.palIdx.eq(myPaletteOrderDto.getPalIdx())
                        .and(myPalette.member.eq(member)))
                .execute();

        return true;
    }

    @Override
    public int bookmarkMypalette(Member member, MyPaletteBookmarkDto myPaletteBookmarkDto) {
        QMyPalette myPalette = QMyPalette.myPalette;

        // 기존 순서 가져오기
        Integer currentOrder = jpaQueryFactory
                    .select(myPalette.bookmark)
                    .from(myPalette)
                    .where(myPalette.palIdx.eq(myPaletteBookmarkDto.getPalIdx()))
                    .fetchOne();

        if(currentOrder == null){
            throw new GlobalException(ErrorCode.EMPTY_RESULT);
        }

        // 기존 북마크된 팔레트 수 가져오기
        int totalBookmark = Math.toIntExact(jpaQueryFactory
                .select(myPalette.count())
                .from(myPalette)
                .where(myPalette.member.eq(member)
                        .and(myPalette.bookmark.between(1, maxBookmarkCount)))
                .fetchFirst());

        // 변경 북마크 위치를 저장할 변수
        int targetBookmark = 0;

        // ❕ 신규등록시
        if(myPaletteBookmarkDto.getIsNewBookmark()){

            // 북마크 가능 총 개수(3개) 를 넘겼는데 신규 등록이면 에러 반환
            if(totalBookmark >= maxBookmarkCount || totalBookmark < 0) {
                throw new GlobalException(MyPaletteErrorCode.EXCEEDED_BOOKMARK_MAXIMUM_LIMIT);
            }

            // 원하는 항목의 순서 변경
            targetBookmark = totalBookmark + 1;
        }

        // ❕ 북마크 해제시
        else if(myPaletteBookmarkDto.getBookmark() == 0){
            if(currentOrder > 0) { //
                jpaQueryFactory.update(myPalette)
                        .set(myPalette.bookmark, myPalette.bookmark.add(-1))
                        .where(myPalette.member.eq(member)
                                .and(myPalette.bookmark.between(currentOrder + 1, totalBookmark)))
                        .execute();

                targetBookmark = 0;
            }
        }

        // ❕ 북마크 이동시
        else {
            // 순서 변경로직: 기존 순서의 나머지 값들을 변경
            // 순서를 앞으로 변경 시
            if (currentOrder > myPaletteBookmarkDto.getBookmark()) {
                jpaQueryFactory.update(myPalette)
                        .set(myPalette.bookmark, myPalette.bookmark.add(1))
                        .where(myPalette.member.eq(member)
                                .and(myPalette.bookmark.between(myPaletteBookmarkDto.getBookmark(), currentOrder - 1)))
                        .execute();
            }
            // 순서를 뒤로 보낼 시
            else {
                jpaQueryFactory.update(myPalette)
                        .set(myPalette.bookmark, myPalette.bookmark.add(-1))
                        .where(myPalette.member.eq(member)
                                .and(myPalette.bookmark.between(currentOrder + 1, myPaletteBookmarkDto.getBookmark())))
                        .execute();
            }
            targetBookmark = myPaletteBookmarkDto.getBookmark();
        }

        // 원하는 항목의 순서 변경
        long updatedRows = jpaQueryFactory.update(myPalette)
                .set(myPalette.bookmark, targetBookmark)
                .where(myPalette.palIdx.eq(myPaletteBookmarkDto.getPalIdx()))
                .execute();

        return targetBookmark;
    }

    @Override
    public boolean openMypalette(MyPaletteOpenDto myPaletteOpenDto) {
        QMyPalette myPalette = QMyPalette.myPalette;

        // 업데이트 된 행의 갯수가 저장됨. 실패하면 계산하지 않음.
        long updatedRows = jpaQueryFactory.update(myPalette)
                .set(myPalette.open, myPaletteOpenDto.getOpen())
                .where(myPalette.palIdx.eq(myPaletteOpenDto.getPalIdx()))
                .execute();

        // 행 업데이트가 정상적으로 되면 true, 아니면 false
        return updatedRows > 0;
    }

    @Override
    public List<RandomMyPaletteRepositoryDto> findRandomPaletteDetail(List<Integer> randomPaletteIds) {
        QMyPalette myPalette = QMyPalette.myPalette;
        QPalette_Paint palettePaint = QPalette_Paint.palette_Paint;
        QPaint paint = QPaint.paint;
        QPigment pigment = QPigment.pigment;
        QPaint_Pigment paintPigment = QPaint_Pigment.paint_Pigment;

        // 🔹 1단계: myPalIdx에 해당하는 팔레트 정보 조회 (paintList는 빈 값)
        List<RandomMyPaletteRepositoryDto> myPaletteList = jpaQueryFactory
                .from(myPalette)
                .where(myPalette.palIdx.in(randomPaletteIds))
                .transform(GroupBy.groupBy(myPalette.palIdx).list(
                        new QRandomMyPaletteRepositoryDto(
                                myPalette.palIdx,
                                myPalette.palName,
                                myPalette.palOrder,
                                myPalette.open,
                                myPalette.imgSrc,
                                myPalette.bookmark,
                                myPalette.member.memberIdx,
                                myPalette.member.nickname,
                                Expressions.constant(Collections.emptyList()) // 🚀 빈 리스트로 초기화
                        ))
                );

        // 🔹 2단계: Palette_Paint 테이블에서 myPalIdx에 해당하는 페인트 목록 조회
        Map<Integer, List<PaintInMyPaletteRepositoryDto>> palettePaintMap = jpaQueryFactory
                .from(palettePaint)
                .leftJoin(palettePaint.paint, paint)
                .where(palettePaint.myPalette.palIdx.in(randomPaletteIds))
                .transform(GroupBy.groupBy(palettePaint.myPalette.palIdx).as(
                        GroupBy.list(Projections.constructor(PaintInMyPaletteRepositoryDto.class,
                                paint.paintIdx,
                                paint.paintCode,
                                paint.brandEN,
                                paint.brandKR,
                                paint.paintNameEN,
                                paint.paintNameKR,
                                paint.colorEN,
                                paint.colorKR,
                                paint.size,
                                paint.unit,
                                paint.prodImg,
                                paint.textureImg1,
                                palettePaint.ppPaintOrder,
                                Expressions.constant(Collections.emptyList()) // pigments는 빈 값으로 초기화
                        ))
                ));

//        for(Map.Entry<Integer, List<PaintInMyPaletteRepositoryDto>> entry : palettePaintMap.entrySet()) {
//            List<PaintInMyPaletteRepositoryDto> sortedList = entry.getValue().stream()
//                    .sorted(Comparator.comparingInt(PaintInMyPaletteRepositoryDto::getPaintOrder))
//                    .toList();
//
//            entry.setValue(sortedList);
//        }

        // 🔹 Java에서 paintList 채우기
        myPaletteList.forEach(palette ->
                palette.setPaintList(palettePaintMap.getOrDefault(palette.getPalIdx(), new ArrayList<>()))
        );

        // 🔹 3단계: paintList 내의 paintIdx를 기준으로 Paint_Pigment 테이블 조회
        List<Integer> paintIds = myPaletteList.stream()
                .flatMap(palette -> palette.getPaintList().stream().map(PaintInMyPaletteRepositoryDto::getPaintIdx))
                .distinct()
                .collect(Collectors.toList());

        List<String> paintCodes = jpaQueryFactory
                .select(paint.paintCode)
                .from(paint)
                .where(paint.paintIdx.in(paintIds))
                .fetch();

        Map<String, List<PigmentForPaintDto>> paintPigmentMap = jpaQueryFactory
                .from(paintPigment)
                .leftJoin(paintPigment.pigment, pigment)
//                .where(paintPigment.paint.paintIdx.in(paintIds))
                .where(paintPigment.paintCode.in(paintCodes))
//                .transform(GroupBy.groupBy(paintPigment.paint.paintIdx).as(
                .transform(GroupBy.groupBy(paintPigment.paintCode).as(
                        GroupBy.list(Projections.constructor(PigmentForPaintDto.class,
                                pigment.pigmentIdx,
                                pigment.ciName,
                                pigment.ciNumber
                        ))
                ));

        // 🔹 Java에서 pigments 채우기
        myPaletteList.forEach(palette ->
                palette.getPaintList().forEach(paintList ->
                        paintList.setPigments(paintPigmentMap.getOrDefault(paintList.getPaintCode(), new ArrayList<>()))
                )
        );

        // randomPalettes list 에 있는 palIdx 순으로 정렬
        myPaletteList.sort(Comparator.comparingInt(dto -> randomPaletteIds.indexOf(dto.getPalIdx())));
        return myPaletteList;
    }

    @Override
    public MyPaletteRepositoryDto getMyPaletteByPalIdx(int palIdx) {
        QMyPalette myPalette = QMyPalette.myPalette;
        QPalette_Paint palettePaint = QPalette_Paint.palette_Paint;
        QPaint paint = QPaint.paint;
        QPaint_Pigment paintPigment = QPaint_Pigment.paint_Pigment;
        QPigment pigment = QPigment.pigment;

        List<Tuple> result = jpaQueryFactory
                .select(
                        myPalette.palIdx,
                        myPalette.palName,
                        myPalette.palOrder,
                        myPalette.open,
                        myPalette.imgSrc,
                        myPalette.bookmark,
                        myPalette.createDate,
                        myPalette.lastEditDate,

                        palettePaint.ppPaintOrder,

                        paint.paintIdx,
                        paint.paintCode,
                        paint.brandEN,
                        paint.brandKR,
                        paint.paintNameEN,
                        paint.paintNameKR,
                        paint.colorEN,
                        paint.colorKR,
                        paint.size,
                        paint.unit,
                        paint.prodImg,
                        paint.textureImg1,

                        pigment.pigmentIdx,
                        pigment.ciName,
                        pigment.ciNumber
                )
                .from(myPalette)
                .leftJoin(palettePaint).on(palettePaint.myPalette.eq(myPalette))
                .leftJoin(palettePaint.paint, paint)
                .leftJoin(paintPigment).on(paint.paintCode.eq(paintPigment.paintCode))
                .leftJoin(paintPigment.pigment, pigment)
                .where(myPalette.palIdx.eq(palIdx))
                .fetch();


        Map<Integer, PaintInMyPaletteRepositoryDto> paintMap = new LinkedHashMap<>();
        Map<Integer, Integer> paintOrderMap = new HashMap<>();

        Integer paletteId = null;
        String palName = null;
        Integer palOrder = null;
        Integer open = null;
        String imgSrc = null;
        Integer bookmark = null;
        Date createDate = null;
        Date lastEditDate = null;

        for (Tuple row : result) {
            // 1. palette 정보 저장 (첫 row 기준)
            if (paletteId == null) {
                paletteId = row.get(myPalette.palIdx);
                palName = row.get(myPalette.palName);
                palOrder = row.get(myPalette.palOrder);
                open = row.get(myPalette.open);
                imgSrc = row.get(myPalette.imgSrc);
                bookmark = row.get(myPalette.bookmark);
                createDate = row.get(myPalette.createDate);
                lastEditDate = row.get(myPalette.lastEditDate);
            }

            Integer paintIdx = row.get(paint.paintIdx);
            if (paintIdx == null) continue;

            PaintInMyPaletteRepositoryDto paintDto = paintMap.get(paintIdx);
            if (paintDto == null) {
                paintDto = PaintInMyPaletteRepositoryDto.builder()
                        .paintIdx(paintIdx)
                        .paintCode(row.get(paint.paintCode))
                        .brandEN(row.get(paint.brandEN))
                        .brandKR(row.get(paint.brandKR))
                        .paintNameEN(row.get(paint.paintNameEN))
                        .paintNameKR(row.get(paint.paintNameKR))
                        .colorEN(row.get(paint.colorEN))
                        .colorKR(row.get(paint.colorKR))
                        .size(row.get(paint.size))
                        .unit(row.get(paint.unit))
                        .prodImg(row.get(paint.prodImg))
                        .textureImg1(row.get(paint.textureImg1))
                        .paintOrder(row.get(palettePaint.ppPaintOrder))
                        .pigments(new ArrayList<>())
                        .build();

                paintMap.put(paintIdx, paintDto);

                Integer paintOrder = row.get(palettePaint.ppPaintOrder);
                paintOrderMap.put(paintIdx, paintOrder);
            }

            // 2. pigment 정보 추가
            Integer pigmentIdx = row.get(pigment.pigmentIdx);
            if (pigmentIdx != null) {
                PigmentForPaintDto pigmentDto = PigmentForPaintDto.builder()
                        .pigmentIdx(pigmentIdx)
                        .ciName(row.get(pigment.ciName))
                        .ciNumber(row.get(pigment.ciNumber))
                        .build();

                paintDto.getPigments().add(pigmentDto);
            }
        }

        List<PaintInMyPaletteRepositoryDto> sortedPaintList = paintMap.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> paintOrderMap.getOrDefault(entry.getKey(), 0)))
                .map(Map.Entry::getValue)
                .toList();

        return MyPaletteRepositoryDto.builder()
                .palIdx(paletteId)
                .palName(palName)
                .palOrder(palOrder)
                .open(open)
                .imgSrc(imgSrc)
                .bookmark(bookmark)
                .createDate(createDate)
                .lastEditDate(lastEditDate)
                .paintList(sortedPaintList)
                .build();
    }
}