package com.paintfinder.paint_pigment.repository;

import com.paintfinder.paint.entity.Paint;
import com.paintfinder.paint_pigment.entity.Paint_Pigment;
import com.paintfinder.pigment.entity.Pigment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Paint_PigmentRepository extends JpaRepository<Paint_Pigment, Integer> {

    /**
     * 페인트_피그먼트 중복 검사를 위한 데이터 존재 여부 판별 쿼리문
     * @param paintCode
     * @param pigment
     * @return true/false
     */
    boolean existsByPaintCodeAndPigment(String paintCode, Pigment pigment);

    Optional<List<Paint_Pigment>> findByPaintCode(String paintCode);
}
