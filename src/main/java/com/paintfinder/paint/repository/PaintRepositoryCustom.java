package com.paintfinder.paint.repository;

import com.paintfinder.paint.dto.PaintThumbnailDto;
import com.paintfinder.paint.dto.requestDto.PaintRequestDto;
import com.paintfinder.paint.dto.responseDto.PaintDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaintRepositoryCustom {

    /**
     * QueryDSL을 사용해 물감 다중검색
     * @param paintRequestDto
     * @return
     */
    Page<PaintThumbnailDto> searchPaints(PaintRequestDto paintRequestDto, String category1, String category2, Pageable pageable);

    /**
     * QueryDSL 을 사용해 paintCode 에 해당하는 물감 전체 조회.
     * @param paintCode
     * @return
     */
    Optional<List<PaintDetailResponseDto>> findPaintDetailByPaintCode(String paintCode);
}
