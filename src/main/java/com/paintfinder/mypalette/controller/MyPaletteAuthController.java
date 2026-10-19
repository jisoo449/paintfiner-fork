package com.paintfinder.mypalette.controller;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.util.JsonUtil;
import com.paintfinder.image.service.ImageService;
import com.paintfinder.mypalette.dto.request.*;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.mypalette.service.MyPaletteAuthService;
import com.paintfinder.palette_paint.service.Palette_PaintService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/auth/mypal")
@RequiredArgsConstructor
@Slf4j
public class MyPaletteAuthController {

    private final MyPaletteAuthService myPaletteService;
    private final Palette_PaintService palettePaintService;
    private final ImageService imageService;

    private final int maxPaintSize = 100;

    @ApiOperation(value = "마이팔레트 신규 등록 api", notes = "사진, 페인트 idx, 팔레트 이름, 순서, 북마크, 공개여부 가 필요")
    @PostMapping("/addMypal")
    public ResponseEntity addMypalette(
            @RequestPart(value = "img", required = false) MultipartFile multipartFile,
            @RequestPart("myPalette") String myPaletteRequestDtoStr,
            HttpServletRequest request){

        // TODO :
        //  페인트가 없는 idx로 입력되었을 때, 마이팔레트는 저장 되면서, 사진은 저장되지 않음.
        //  방법1. 잘못된 페인트(이상한 데이터)를 알려주고, 정상적인 애들은 저장하기.
        //  방법2. 아예 실행 실패로 던지기
        //  방법3. 잘못된 페인트 목록을 알려주고, 실행 실패로 던지기


        // 유저 idx 읽어오기
        int memberIdx = (int) request.getAttribute("memberIdx");

        // JSON 데이터를 DTO로 변환 JsonToDto
        MyPaletteRequestDto myPaletteRequestDto = JsonUtil.parseJson(myPaletteRequestDtoStr, MyPaletteRequestDto.class);

        // 팔레트 등록(등록된 팔레트 100개 이상일 때, 팔레트 내 물감 개수가 100개 이상일 때는 에러 발생)
        ResponseDto<MyPalette> responseDto = myPaletteService.addMyPalette(memberIdx, myPaletteRequestDto);

        MyPalette myPalette = responseDto.getData();

        List<Integer> paintList = myPaletteRequestDto.getPaintList();

        // Palette_Paint 테이블에 마이팔레트, 페인트 idx 목록 넘겨줘 저장하기
        palettePaintService.addPalette_Paints(myPalette, paintList);

        // 사진 등록은 팔레트 생성 후 idx 생긴 후 실행
        if(multipartFile != null && !multipartFile.isEmpty()){
            ResponseDto<String> imageResponseDto = imageService.uploadImage("palette", multipartFile);

            String imageUrl = imageResponseDto.getData();

            myPaletteService.addMyPaletteImageUrl(myPalette, imageUrl);
        }

        // 신규 등록한 팔레트의 순서를 맨 앞으로 보내기
        myPaletteService.updateMyPaletteOrder(memberIdx, new MyPaletteOrderDto(myPalette.getPalIdx(), 1));

        // 기본순으로 정렬 조회한 값 전송하기
        ResponseDto myPalettesResponseDto = myPaletteService.findMyPalettes(memberIdx, PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "pal_order")));

        return new ResponseEntity(myPalettesResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 찾기", notes = "페이지 번호(몇번째 페이지), 정렬 기준(날짜, 이름순의 asc, desc, 기본), 페이지당 표시 개수가 필요함.")
    @GetMapping("/getMypalList")
    public ResponseEntity findMypalettes(
            HttpServletRequest request,
            Pageable pageable) {

        // 유저 idx 읽어오기
        int memberIdx = (int) request.getAttribute("memberIdx");

        // 조건에 맞는 마이팔레트 리스트 반환
        ResponseDto myPalettesResponseDto = myPaletteService.findMyPalettes(memberIdx, pageable);

        return new ResponseEntity(myPalettesResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 삭제하기", notes = "swagger 에는 삭제할 idx(int) 값 하나씩 입력")
    @DeleteMapping("/deleteMypal")
    public ResponseEntity deleteMypalettes(
            HttpServletRequest request,
            @RequestBody List<Integer> myPalIdxList) {

        int memberIdx = (int) request.getAttribute("memberIdx");

        // 유저 소유여부 확인
        myPaletteService.checkMyPaletteOwner(memberIdx, myPalIdxList);

        // 삭제
        ResponseDto responseDto = myPaletteService.deleteMyPalettes(memberIdx, myPalIdxList);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 순서 변경하기", notes = "팔레트 순서 (palOrder) 변경, 팔레트 아이디와 바뀐 순서만 입력하면 된다.")
    @PutMapping("/updateMypalOrder")
    public ResponseEntity updateMypaletteOrder(
            HttpServletRequest request,
            @RequestBody MyPaletteOrderDto myPaletteOrderDto) {

        int memberIdx = (int) request.getAttribute("memberIdx");

        // 유저 소유여부 확인. 접근자가 소유자가 아닐 경우 에러 발생하며 종료된다.
        myPaletteService.checkMyPaletteOwner(memberIdx, List.of(myPaletteOrderDto.getPalIdx()));

        // 팔레트 순서 변경
        myPaletteService.updateMyPaletteOrder(memberIdx, myPaletteOrderDto);

        ResponseDto myPalettesResponseDto = myPaletteService.findMyPalettes(memberIdx, PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "pal_order")));

        return new ResponseEntity(myPalettesResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 이름 수정하기", notes = "내 팔레트 이름 수정")
    @PutMapping("/updateMypalName")
    public ResponseEntity updateMypaletteName(
            @RequestBody MyPaletteNameDto myPaletteNameDto,
            HttpServletRequest request
    ) {
        int memberIdx = (int) request.getAttribute("memberIdx");

        MyPalette myPalette = myPaletteService.findMyPalette(myPaletteNameDto.getPalIdx(), memberIdx);

        myPaletteService.updateMyPaletteName(myPaletteNameDto.getMyPalName(), myPalette);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 사진 수정하기", notes = "내 팔레트 사진 신규등록/변경")
    @PutMapping("/updateMypalImg")
    public ResponseEntity updateMypaletteImg(
            @RequestPart(value = "img", required = false) MultipartFile multipartFile,
            int myPaletteIdx,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
        // 유저 정보 읽어오기
        int memberIdx = (int) request.getAttribute("memberIdx");

        MyPalette myPalette = myPaletteService.findMyPalette(myPaletteIdx, memberIdx);

        ResponseDto responseDto = myPaletteService.updateMyPaletteImg(myPalette, multipartFile);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 사진 삭제하기", notes = "내 팔레트 사진 삭제")
    @DeleteMapping("/deleteMypalImg")
    public ResponseEntity deleteMypaletteImg(
            @RequestParam("palIdx") int myPaletteIdx,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
        // 유저 정보 읽어오기
        int memberIdx = (int) request.getAttribute("memberIdx");

        MyPalette myPalette = myPaletteService.findMyPalette(myPaletteIdx, memberIdx);

        ResponseDto responseDto = myPaletteService.deleteMyPaletteImg(myPalette);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 내 페인트 위치 수정", notes = "페인트 1개 위치 수정 api. 마이팔레트 위치 수정과 로직 동일")
    @PutMapping("/updateMypalPaintOrder")
    public ResponseEntity updateMypalettePaintOrder(
            @RequestBody MyPalettePaintOrderDto myPalettePaintOrderDto,
            HttpServletRequest request
    ) {
        // 유저 정보 읽어오기
        int memberIdx = (int) request.getAttribute("memberIdx");

        // 유저 소유여부 확인. 접근자가 소유자가 아닐 경우 에러 발생하며 종료된다.
        myPaletteService.checkMyPaletteOwner(memberIdx, List.of(myPalettePaintOrderDto.getPalIdx()));

        ResponseDto responseDto = myPaletteService.updateMyPalettePaintOrder(myPalettePaintOrderDto);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    // 북마크(좋아요 or 별표) api
    @ApiOperation(value = "내 팔레트 즐겨찾기", notes = "bookmark 속성 0 또는 1 로 설정")
    @PutMapping("/bookmarkMypal")
    public ResponseEntity bookmarkMypalette(
            HttpServletRequest request,
            @RequestBody MyPaletteBookmarkDto myPaletteBookmarkDto
            ){
        int memberIdx = (int) request.getAttribute("memberIdx");

        myPaletteService.checkMyPaletteOwner(memberIdx, List.of(myPaletteBookmarkDto.getPalIdx()));

        ResponseDto responseDto = myPaletteService.bookmarkMyPalette(memberIdx, myPaletteBookmarkDto);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    // 공개여부 토글 스위치 (on/off) 적용할것이기에 개별 api 생성
    @ApiOperation(value = "내 팔레트 공개/비공개", notes = "open 속성 0 또는 1 로 설정")
    @PutMapping("/openMypal")
    public ResponseEntity openMypalette(
            HttpServletRequest request,
            @RequestBody MyPaletteOpenDto myPaletteOpenDto
    ){
        int memberIdx = (int) request.getAttribute("memberIdx");

        myPaletteService.checkMyPaletteOwner(memberIdx, List.of(myPaletteOpenDto.getPalIdx()));

        ResponseDto responseDto = myPaletteService.openMyPalette(myPaletteOpenDto);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 내 모든 페인트 목록 삭제", notes = "팔레트 idx 를 받아 그 팔레트에 해당하는 페인트 목록 삭제")
    @DeleteMapping("/deleteAllPaints")
    public ResponseEntity deleteAllMypalettePaints(
            HttpServletRequest request,
            @RequestBody int myPaletteIdx
    ){
        int memberIdx = (int) request.getAttribute("memberIdx");

        MyPalette myPalette = myPaletteService.findMyPalette(myPaletteIdx, memberIdx);

        myPaletteService.deleteAllMypalettePaints(myPalette);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 내 페인트 하나 삭제", notes = "팔레트 idx, 페인트 idx 를 받아 해당 팔레트_페인트 삭제")
    @DeleteMapping("/deleteMypalPaint")
    public ResponseEntity deleteMypalettePaint(
            HttpServletRequest request,
            @RequestBody MyPalettePaintDeleteDto myPalettePaintDeleteDto
    ){
        int memberIdx = (int) request.getAttribute("memberIdx");

        // 삭제 시 페인트 정렬 순서 맞추기
        myPaletteService.updateMyPalettePaintOrder(new MyPalettePaintOrderDto().builder()
                .palIdx(myPalettePaintDeleteDto.getPalIdx())
                .paintIdx(myPalettePaintDeleteDto.getPaintIdx())
                .changeOrder(maxPaintSize)
                .build());

        myPaletteService.deleteMyPalettePaint(memberIdx, myPalettePaintDeleteDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "내 팔레트 내 페인트 추가(단일, 다중 모두 가능)", notes = "")
    @PostMapping("/addMypalPaints")
    public ResponseEntity addMypalettePaint(
            HttpServletRequest request,
            @RequestBody MyPalettePaintAddDto myPalettePaintAddDto
    ){
        int memberIdx = (int) request.getAttribute("memberIdx");

        MyPalette myPalette = myPaletteService.findMyPalette(myPalettePaintAddDto.getPalIdx(), memberIdx);

        palettePaintService.addPalette_Paints(myPalette, myPalettePaintAddDto.getPaintIdxList());

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "팔레트 복사", notes = "palIdx 만 전달받아옴")
    @PostMapping("/copyMypal/{palIdx}")
    public ResponseEntity copyMypalette(
            HttpServletRequest request,
            @PathVariable("palIdx") int palIdx) throws Exception {

        int memberIdx = (int) request.getAttribute("memberIdx");

        // 복사본 마이팔레트 생성 및 db 저장
        MyPalette copyMypalette = myPaletteService.copyMyPalette(memberIdx, palIdx);

        // 신규 생성된 팔레트는 항상 맨 앞으로 가져오기!
        myPaletteService.updateMyPaletteOrder(memberIdx, new MyPaletteOrderDto(copyMypalette.getPalIdx(), 1));

        // 팔레트_페인트 목록도 복사해서 db 저장
        palettePaintService.copyPalette_Paints(copyMypalette, palIdx);

        // 기본순으로 정렬 조회한 값 전송하기
        ResponseDto myPalettesResponseDto = myPaletteService.findMyPalettes(memberIdx, PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "pal_order")));

        return new ResponseEntity(myPalettesResponseDto, HttpStatus.OK);
    }
}