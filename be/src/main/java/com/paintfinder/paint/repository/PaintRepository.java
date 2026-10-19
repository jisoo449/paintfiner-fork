package com.paintfinder.paint.repository;

import com.paintfinder.paint.entity.Paint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaintRepository extends JpaRepository<Paint, Integer>, PaintRepositoryCustom {

    /**
     * 물감 인덱스 값으로 물감 entity 반환
     * @param paintIdx
     * @return
     */
    Optional<Paint> findByPaintIdx(int paintIdx);

    /**
     * 물감 코드로 물감 조회
     * @param paintCode
     * @return
     */
//    @Query("SELECT p FROM Paint p LEFT JOIN FETCH p.paintPigmentList WHERE p.paintCode = :paintCode")
    List<Paint> findAllByPaintCode(String paintCode);

    /**
     * 페인트 코드 중복 검사 쿼리
     * @param brandEN
     * @param paintNameEN
     * @param colorEN
     * @return 1개의 Paint
     */
    @Query(value = "SELECT paint_code FROM Paint WHERE brand_en = :brandEN AND paint_name_en = :paintNameEN AND color_en = :colorEN LIMIT 1", nativeQuery = true)
    Optional<String> findPaintCodeNative(
            @Param("brandEN") String brandEN,
            @Param("paintNameEN") String paintNameEN,
            @Param("colorEN") String colorEN
    );
}
