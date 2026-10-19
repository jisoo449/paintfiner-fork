package com.paintfinder.palette_paint.repository;

import com.paintfinder.mypalette.dto.request.MyPalettePaintOrderDto;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.paint.entity.Paint;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Palette_PaintRepositoryCustom {

    /**
     * 마이팔레트 내 페인트의 사용자 지정 정렬 순서 변경 로직
     * @param myPalette
     * @param paint
     * @param changeOrder
     * @return true false
     */
    @Transactional
    boolean updateMypalettePaintOrder(MyPalette myPalette, Paint paint, int changeOrder);

    /**
     * 마이팔레트 내 페인트 삭제 후 order 정렬 로직
     * @param myPalette
     * @param deletedOrder
     * @return true false
     */
    @Transactional
    boolean updatePaintOrderAfterDelete(MyPalette myPalette, int deletedOrder);
}
