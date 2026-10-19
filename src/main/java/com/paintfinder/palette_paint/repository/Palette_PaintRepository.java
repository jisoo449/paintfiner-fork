package com.paintfinder.palette_paint.repository;

import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.palette_paint.entity.Palette_Paint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface Palette_PaintRepository extends JpaRepository<Palette_Paint, Long>, Palette_PaintRepositoryCustom {

    @Transactional
    void deleteByMyPalette(MyPalette myPalette);

    @Transactional
    void deleteByMyPaletteAndPaint(MyPalette myPalette, Paint paint);

    Integer countByMyPalette(MyPalette myPalette);

    Optional<Palette_Paint> findPpPaintOrderByMyPaletteAndPaint(MyPalette myPalette, Paint paint);

    List<Palette_Paint>findPalette_PaintsByMyPalette(MyPalette myPalette);
}
