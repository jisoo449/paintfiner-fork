package com.paintfinder.mypalette.service;

import com.paintfinder.global.dto.ResponseDto;

public interface MyPaletteService {
    /**
     * 최대 10개의 랜덤한 팔레트 조회.
     * @return
     */
    public ResponseDto getRandomPaletteList();

    /**
     * 팔레트 idx 로 팔레트 단일 조회 로직.
     * @param palIdx
     * @return 팔레트, 팔레트 내 페인트 목록, 페인트의 안료 목록
     */
    public ResponseDto getMyPalette(int palIdx);
}
