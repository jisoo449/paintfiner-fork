package com.paintfinder.palette_paint.repository;

import com.paintfinder.mypalette.dto.request.MyPalettePaintOrderDto;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.palette_paint.entity.QPalette_Paint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class Palette_PaintRepositoryImpl implements Palette_PaintRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean updateMypalettePaintOrder(MyPalette myPalette, Paint paint, int changeOrder) {
        QPalette_Paint palettePaint = QPalette_Paint.palette_Paint;

        // 기존 순서 가져오기
        Integer currentOrder = jpaQueryFactory
                .select(palettePaint.ppPaintOrder)
                .from(palettePaint)
                .where(palettePaint.paint.eq(paint)
                        .and(palettePaint.myPalette.eq(myPalette)))
                .fetchOne();

        if(currentOrder == null) {
            return false;
        }

        if(currentOrder > changeOrder) {
            jpaQueryFactory.update(palettePaint)
                    .set(palettePaint.ppPaintOrder, palettePaint.ppPaintOrder.add(1))
                    .where(palettePaint.myPalette.eq(myPalette)
                            .and(palettePaint.ppPaintOrder.between(changeOrder, currentOrder - 1)))
                    .execute();
        }

        else {
            jpaQueryFactory.update(palettePaint)
                    .set(palettePaint.ppPaintOrder, palettePaint.ppPaintOrder.add(-1))
                    .where(palettePaint.myPalette.eq(myPalette)
                            .and(palettePaint.ppPaintOrder.between(currentOrder + 1, changeOrder)))
                    .execute();
        }

        jpaQueryFactory.update(palettePaint)
                .set(palettePaint.ppPaintOrder, changeOrder)
                .where(palettePaint.paint.eq(paint)
                        .and(palettePaint.myPalette.eq(myPalette)))
                .execute();

        return true;
    }

    @Override
    public boolean updatePaintOrderAfterDelete(MyPalette myPalette, int deletedOrder) {
        QPalette_Paint palettePaint = QPalette_Paint.palette_Paint;

        jpaQueryFactory.update(palettePaint)
                .set(palettePaint.ppPaintOrder, palettePaint.ppPaintOrder.add(-1))
                .where(palettePaint.myPalette.eq(myPalette)
                        .and(palettePaint.ppPaintOrder.gt(deletedOrder)))
                .execute();

        return false;
    }
}
