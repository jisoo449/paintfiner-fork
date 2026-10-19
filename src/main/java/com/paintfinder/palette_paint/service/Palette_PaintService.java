package com.paintfinder.palette_paint.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.mypalette.entity.MyPalette;

import java.util.List;

public interface Palette_PaintService {
    /**
     * 팔레트에 저장될 페인트들을 저장하는 서비스
     * @param myPalette
     * @param paintList
     * @return success or fail
     */
    public ResponseDto addPalette_Paints(MyPalette myPalette, List<Integer> paintList);

    /**
     * 복사된 팔레트에 페인트 목록도 복사하는 서비스
     * @param copyMyPalette
     * @param palIdx
     * @return
     */
    public ResponseDto copyPalette_Paints(MyPalette copyMyPalette, int palIdx);
}
