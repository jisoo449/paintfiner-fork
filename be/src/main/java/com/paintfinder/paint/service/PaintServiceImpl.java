package com.paintfinder.paint.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.global.util.FileUtil;
import com.paintfinder.image.service.ImageService;
import com.paintfinder.image.util.AwsS3Uploader;
import com.paintfinder.paint.dto.PaintDetailDto;
import com.paintfinder.paint.dto.PaintThumbnailDto;
import com.paintfinder.paint.dto.requestDto.PaintRequestDto;
import com.paintfinder.paint.dto.responseDto.PaintDetailResponseDto;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.paint.repository.PaintRepository;
import com.paintfinder.paint.util.PaintUtil;
import com.paintfinder.paint_pigment.entity.Paint_Pigment;
import com.paintfinder.paint_pigment.repository.Paint_PigmentRepository;
import com.paintfinder.pigment.dto.PigmentForPaintDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaintServiceImpl implements PaintService {

	private final PaintRepository paintRepository;
	private final Paint_PigmentRepository paintPigmentRepository;
	private final ImageService imageService;
	private final AwsS3Uploader awsS3Uploader;
	private final PaintUtil paintUtil;

	/**
	 * 물감 검색
	 * @param paintRequestDto
	 * @return
	 */
	@Override
	public ResponseDto selectPaintsList(PaintRequestDto paintRequestDto, String category1, String category2, Pageable pageable) {

		ResponseDto response;

		Page<PaintThumbnailDto> result = paintRepository.searchPaints(paintRequestDto, category1, category2, pageable);	// 물감 검색 메서드 실행

		response = ResponseDto.builder()	// responseDto에 결과 넣기
				.data(result)
				.isSuccess(true)
				.build();

		return response;
	}

	@Override
	public ResponseDto updatePaintImg(MultipartFile multipartFile, int paintIdx, String attributeName) throws UnsupportedEncodingException {
		Paint paint = paintRepository.findByPaintIdx(paintIdx)
				.orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));
		String savedImgUrl = "";
		if(attributeName.equals("textureImg1")) {
			savedImgUrl = paint.getTextureImg1();
		} else if(attributeName.equals("prodImg")){
			savedImgUrl = paint.getProdImg();
		}
		// 기존 S3에 저장된 이미지 삭제
		ResponseDto responseDto = imageService.deleteImage("paint", savedImgUrl);
		ResponseDto<String> imgResponseDto = imageService.uploadImage("paint", multipartFile);
		String imageUrl = imgResponseDto.getData();
		if(attributeName.equals("textureImg1")) {
			paint.updateTexture1Img(imageUrl);
		} else if(attributeName.equals("prodImg")){
			paint.updateProdImg(imageUrl);
		}
		log.info("생성된 {} ImgUrl :  {}", attributeName, imageUrl);
		paintRepository.save(paint);
		return responseDto;
	}
    
	/**
	 * 물감 상세보기
	 * @param paintCode
	 * @return
	 */
	@Override
	public ResponseDto paintDetail(String paintCode) {

		ResponseDto response;

		//1.물감 코드로 페인트 정보 조회
		List<Paint> paints = paintRepository.findAllByPaintCode(paintCode);

		//2. PaintDetailDto에 넣기
		PaintDetailDto paintDetailDto = null;

		for (Paint paint : paints){
			// 첫 번째 paint(paintDetailDto 초기화 전)에 대해 builder 패턴 적용
			if (paintDetailDto == null){
				paintDetailDto = PaintDetailDto.builder()
						.paintCode(paint.getPaintCode())
						.brandEN(paint.getBrandEN())
						.brandKR(paint.getBrandKR())
						.paintNameEN(paint.getPaintNameEN())
						.paintNameKR(paint.getPaintNameKR())
						.colorEN(paint.getColorEN())
						.colorKR(paint.getColorKR())
						.series(paint.getSeries())
						.transparencyEN(paint.getTransparencyEN())
						.transparencyKR(paint.getTransparencyKR())
						.lightfastnessEN(paint.getLightfastnessEN())
						.lightfastnessKR(paint.getLightfastnessKR())
						.qualityEN(paint.getQualityEN())
						.qualityKR(paint.getQualityKR())
						.textureEN(paint.getTextureEN())
						.textureKR(paint.getTextureKR())
						.cate1EN(paint.getCate1EN())
						.cate1KR(paint.getCate1KR())
						.cate2EN(paint.getCate2EN())
						.cate2KR(paint.getCate2KR())
						.binder(paint.getBinder())
						.unit(paint.getUnit())
						.vegan(paint.getVegan())
						.descriptionEN(paint.getDescriptionEN())
						.descriptionKR(paint.getDescriptionKR())
						.textureImg1(paint.getTextureImg1())
						.textureImg2(paint.getTextureImg2())
						.textureImg3(paint.getTextureImg3())
						.build();


				// 첫 번째 인자에 대해서만 안료 저장
				List<Paint_Pigment> paintPigmets = paintPigmentRepository.findByPaintCode(paint.getPaintCode()).orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));
				for (Paint_Pigment pigment : paintPigmets) {
					PigmentForPaintDto pigmentDto = new PigmentForPaintDto(
							pigment.getPigment().getPigmentIdx(),
							pigment.getPigment().getCiName(),
							pigment.getPigment().getCiNumber()
					);
					paintDetailDto.addPigment(pigmentDto);
				}

			}

			// 두 번째 인자에 대해서부터 사이즈 저장(첫 번째 사이즈는 중복되기 때문.)
			else{
				// 유효한 사이즈, 이미지, 가격 정보만 추가
				if (paint.getSize() > 0) {
					paintDetailDto.addSize(paint.getSize(), paint.getProdImg(), paint.getPrice());
				}

			}
		}

		//2. ResopnseDto에 결과 넣기
		response = ResponseDto.builder()
				.data(paintDetailDto)
				.isSuccess(true)
				.build();

		return response;
	}

	@Override
	public Map<Paint, String[]> addPaints(MultipartFile multipartFile) {
		Map<Paint, String[]> paint_pigments = new ConcurrentHashMap<>();

		try (InputStream inputStream = multipartFile.getInputStream();
			 Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 읽기

			// 엑셀의 속성명 저장된 맵
			Map<String, Integer> columnIndexMap = new ConcurrentHashMap<>();
			Row headerRow = sheet.getRow(0); // 첫 번째 행 (헤더)

			for (Cell cell : headerRow) {
				columnIndexMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
			}

			for (Row row : sheet) {
				if (row.getRowNum() == 0) continue; // 첫 번째 행(헤더) 스킵

				// 26, 27번 이미지 넣고 경로 가져오기
				// 이미지 파일 읽어와 multipartFile 변환, S3에 업로드 해 새로운 이미지 url 읽어오기
				String prodImg = "";
				if (!paintUtil.getCellValue(row, columnIndexMap.get("prodImg")).equals("")) {
					MultipartFile imgFile = FileUtil.convertUrlToMultipartFile(paintUtil.getCellValue(row, columnIndexMap.get("prodImg")));
//					log.info("제품이미지 : {}, {}",paintUtil.getCellValue(row, columnIndexMap.get("prodImg")), imgFile.getOriginalFilename());
					prodImg = awsS3Uploader.uploadImage("paint", imgFile);
				}

				String textureImg1 = "";
				if (!paintUtil.getCellValue(row, columnIndexMap.get("textureImg1")).equals("")) {
					MultipartFile imgFile = FileUtil.convertUrlToMultipartFile(paintUtil.getCellValue(row, columnIndexMap.get("textureImg1")));
					textureImg1 = awsS3Uploader.uploadImage("paint", imgFile);
				}

				String textureImg2 = "";
				if (!paintUtil.getCellValue(row, columnIndexMap.get("textureImg2")).equals("")) {
					MultipartFile imgFile = FileUtil.convertUrlToMultipartFile(paintUtil.getCellValue(row, columnIndexMap.get("textureImg2")));
					textureImg2 = awsS3Uploader.uploadImage("paint", imgFile);
				}

				String textureImg3 = "";
				if (!paintUtil.getCellValue(row, columnIndexMap.get("textureImg3")).equals("")) {
					MultipartFile imgFile = FileUtil.convertUrlToMultipartFile(paintUtil.getCellValue(row, columnIndexMap.get("textureImg3")));
					textureImg3 = awsS3Uploader.uploadImage("paint", imgFile);
				}

				String paintName = paintUtil.getCellValue(row, columnIndexMap.get("paintNameEN"));
				String brand = paintUtil.getCellValue(row, columnIndexMap.get("brandEN"));
				String color = paintUtil.getCellValue(row, columnIndexMap.get("colorEN"));

				// oldHolland 브랜드만 size가 나뉘어 있지 않아 , 구분자 생성
//				String[] sizeArr = paintUtil.getCellValue(row, columnIndexMap.get("sizes")).split(",");
//				for(String sz : sizeArr){
//					sz = sz.trim();
//					float size = 0;
//					String unit = "ml";
//
//					// 문자열의 시작이 숫자면 ml 단위
//					if(Character.isDigit(sz.charAt(0))){
//						size = Float.parseFloat(sz);
//					}
//					// halfpan 이라는 문자가 오면 unit 에 저장
//					else {
//						unit = sz;
//					}

					// ----oldHolland 특별법 끝

					// paintCode 중복 검사 로직
					String paintCode = paintRepository.findPaintCodeNative(brand, paintName, color)
							.orElse(paintUtil.generatePaintCode(brand, paintName, color));

					Paint paint = Paint.builder()
							.paintCode(paintCode)
							.brandEN(paintUtil.getCellValue(row, columnIndexMap.get("brandEN")))
							.brandKR(paintUtil.getCellValue(row, columnIndexMap.get("brandKR")))
							.paintNameEN(paintUtil.getCellValue(row, columnIndexMap.get("paintNameEN")))
							.paintNameKR(paintUtil.getCellValue(row, columnIndexMap.get("paintNameKR")))
							.colorgroupEN(paintUtil.getCellValue(row, columnIndexMap.get("colorgroupEN")))
							.colorgroupKR(paintUtil.getCellValue(row, columnIndexMap.get("colorgroupKR")))
							.colorEN(paintUtil.getCellValue(row, columnIndexMap.get("colorEN")))
							.colorKR(paintUtil.getCellValue(row, columnIndexMap.get("colorKR")))
							.series(paintUtil.getCellValue(row, columnIndexMap.get("series")))
							.transparencyEN(paintUtil.getCellValue(row, columnIndexMap.get("transparencyEN")))
							.transparencyKR(paintUtil.getCellValue(row, columnIndexMap.get("transparencyKR")))
							.lightfastnessEN(paintUtil.getCellValue(row, columnIndexMap.get("lightfastnessEN")))
							.lightfastnessKR(paintUtil.getCellValue(row, columnIndexMap.get("lightfastnessKR")))
							.qualityEN(paintUtil.getCellValue(row, columnIndexMap.get("qualityEN")))
							.qualityKR(paintUtil.getCellValue(row, columnIndexMap.get("qualityKR")))
							.textureEN(paintUtil.getCellValue(row, columnIndexMap.get("textureEN")))
							.textureKR(paintUtil.getCellValue(row, columnIndexMap.get("textureKR")))
							.cate1EN(paintUtil.getCellValue(row, columnIndexMap.get("cate1EN")))
							.cate1KR(paintUtil.getCellValue(row, columnIndexMap.get("cate1KR")))
							.cate2EN(paintUtil.getCellValue(row, columnIndexMap.get("cate2EN")))
							.cate2KR(paintUtil.getCellValue(row, columnIndexMap.get("cate2KR")))
							.binder(paintUtil.getCellValue(row, columnIndexMap.get("binder")))
							.size(paintUtil.getVeganValue(row, columnIndexMap.get("size")))
//							.size(size)
							.sizes(paintUtil.getCellValue(row, columnIndexMap.get("sizes")))
							.unit(paintUtil.getCellValue(row, columnIndexMap.get("unit")))
//							.unit(unit)
							.vegan(paintUtil.getVeganValue(row, columnIndexMap.get("vegan")))
							.descriptionEN(paintUtil.getCellValue(row, columnIndexMap.get("descriptionEN")))
							.descriptionKR(paintUtil.getCellValue(row, columnIndexMap.get("descriptionKR")))
							.prodImg(prodImg)
							.textureImg1(textureImg1)
							.textureImg2(textureImg2)
							.textureImg3(textureImg3)
							.build();

					// 포함된 안료 값 반환값에 넣기
					paint_pigments.put(paint,
							Arrays.stream(paintUtil.getCellValue(row, columnIndexMap.get("pigments")).split("[,-]")) // , 또는 - 로 분리
									.map(s -> s.replaceAll("\\s+", "")) // 공백 제거
									.filter(s -> !s.isEmpty()) // 빈 문자열 제거
									.toArray(String[]::new));


					// 페인트 저장
					paintRepository.save(paint);

//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return paint_pigments;
	}

	@Override
	public ResponseDto getPaintDetail(int paintIdx) {

		String paintCode = paintRepository.findByPaintIdx(paintIdx).orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT)).getPaintCode();

		List<PaintDetailResponseDto> responseDtoList = paintRepository.findPaintDetailByPaintCode(paintCode)
				.orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

		return ResponseDto.success(responseDtoList);
	}

}
