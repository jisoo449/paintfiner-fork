package com.paintfinder.paint.controller;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.paint.dto.requestDto.PaintRequestDto;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.paint_pigment.service.Paint_PigmentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paintfinder.paint.service.PaintService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/paint")
@RequiredArgsConstructor
public class PaintController {

	private final PaintService service;
	private final PaintService paintService;
	private final Paint_PigmentService paint_pigmentService;

	/**
	 * 물감 요청
	 * @param paintRequestDto
	 * @param pageable
	 * @return
	 */
	@GetMapping(value={"", "/{category1}", "/{category1}/{category2}"})
	public ResponseEntity<ResponseDto> search(
			@PathVariable(required = false) String category1,
			@PathVariable(required = false) String category2,
			@ModelAttribute PaintRequestDto paintRequestDto, Pageable pageable) {
		ResponseDto result = service.selectPaintsList(paintRequestDto, category1, category2, pageable);

		return ResponseEntity.ok(result);
	}
  
	@ApiOperation(value = "페인트 prod img 수정 api", notes = "페인트 사진 등록 api. img파일과 paintIdx 필요")
	@PutMapping("/updatePaintProdImg")
	public ResponseEntity updatePaintProdImg(
			@RequestPart(value = "img", required = true)MultipartFile multipartFile,
			int paintIdx
			) throws UnsupportedEncodingException {

		ResponseDto responseDto = paintService.updatePaintImg(multipartFile, paintIdx, "prodImg");

		return ResponseEntity.ok(responseDto);
	}

	@ApiOperation(value = "페인트 texture1 img 수정 api", notes = "페인트 질감1 사진 등록 api. img파일과 paintIdx 필요")
	@PutMapping("/updatePaintTexture1Img")
	public ResponseEntity updatePaintTexture1Img(
			@RequestPart(value = "img", required = true)MultipartFile multipartFile,
			int paintIdx
	) throws UnsupportedEncodingException {

		ResponseDto responseDto = paintService.updatePaintImg(multipartFile, paintIdx, "textureImg1");

		return ResponseEntity.ok(responseDto);
	}
  
	/**
	 * 물감 상세 페이지
	 * @param paintCode
	 * @return
	 */
	@GetMapping(params = "paintCode", value={"", "/{category1}", "/{category1}/{category2}"})
	@ResponseBody
	public ResponseEntity<ResponseDto> paintDetail(@RequestParam("paintCode") String paintCode) {
		return ResponseEntity.ok(service.paintDetail(paintCode));
	}

	@ApiOperation(value = "크롤링 페인트 데이터 insert용", notes = "excel 파일(xlsx 확장자) 필요")
	@PostMapping("/addPaints")
	public ResponseEntity<ResponseDto> addPaints(
			@RequestPart(value = "excel", required = false) MultipartFile multipartFile
	) {

		Map<Paint, String[]> paint_pigments = paintService.addPaints(multipartFile);

		ResponseDto responseDto = paint_pigmentService.addPaint_Pigments(paint_pigments);

		return new ResponseEntity(responseDto, HttpStatus.OK);
	}

	// 페인트 삭제 시 paintCode가 같으면, textureImg 또는 prodImg 가 동일한 걸 사용하는 애들이 있을 수 있음.
	// 따라서 페인트 삭제 시 페인트코드가 같은 녀석이 존재하지 않을 때만 S3 의 img 까지 지우기로.

	@ApiOperation(value="페인트 상세 조회", notes="페인트 고유 번호(paintIdx)를 이용해 페인트 상세 정보(paintCode가 동일한 제품, 즉 같은 상품의 모든 사이즈)를 조회한다.")
	@GetMapping("/getPaintDetail")
	public ResponseEntity<ResponseDto> getPaintDetail(
			@RequestParam int paintIdx
	) {

		ResponseDto responseDto = service.getPaintDetail(paintIdx);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}

