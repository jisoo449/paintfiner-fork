package com.paintfinder.paint_pigment.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.paint.entity.Paint;

import java.util.Map;

public interface Paint_PigmentService {

    /**
     * 페인트에 저장된 안료값 저장하는 로직
     * @param paint_pigments
     * @return
     */
    public ResponseDto addPaint_Pigments(Map<Paint, String[]> paint_pigments);
}
