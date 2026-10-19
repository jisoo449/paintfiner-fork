package com.paintfinder.paint.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.paint.dto.requestDto.PaintRequestDto;
import com.paintfinder.paint.entity.Paint;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PaintService {

	/**
	 * 검색(필터,검색어,카테고리) 요청에 따른 물감 목록 반환
	 * @param paintRequestDto
	 * @return
	 */
	ResponseDto selectPaintsList(PaintRequestDto paintRequestDto, String category1, String category2, Pageable pageable);

	ResponseDto updatePaintImg(MultipartFile multipartFile, int paintIdx, String attributeName) throws UnsupportedEncodingException;

	/**
	 * 물감 상세보기 페이지
	 * @param paintCode
	 * @return
	 */
	ResponseDto paintDetail(String paintCode);

	/**
	 * 물감 excel 데이터로 추가
	 * @param multipartFile
	 * @return
	 */
	Map<Paint, String[]> addPaints(MultipartFile multipartFile);

	/**
	 * 페인트 상세 정보 조회
	 * @param paintIdx
	 * @return 상세 페인트 정보를 담은 ResponseDto 객체
	 */
	ResponseDto getPaintDetail(int paintIdx);
}
