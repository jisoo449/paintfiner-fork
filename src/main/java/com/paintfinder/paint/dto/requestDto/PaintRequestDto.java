package com.paintfinder.paint.dto.requestDto;

import lombok.Data;

/**
 * 물감 검색 request dto
 * 필터, 카테고리, 검색어를 입력으로 받는다
 */
@Data
public class PaintRequestDto {

    //******** 물감 목록 검색 **************************************

    private String search;          // 검색어

    // ******** 필터 **************************
    private String brand;           // 브랜드

    private String quality;			// 퀄리티(품질)
    private String texture;			// 질감
    private String transparency;	// 투명도
    private String lightfastness;	// 내광성
    private String colorgroup;		// 색분류

    private float start_size;	    // 용량 - 시작 범위
    private float end_size;	        // 용량 - 끝 범위
    private String unit;     	    // 용량 단위(g, ml, cm)

//    private float start_price;      // 가격 - 시작 범위
//    private float end_price;        // 가격 - 끝 범위

    private int sets;				// set 유무

}
