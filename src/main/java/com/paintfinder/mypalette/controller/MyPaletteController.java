package com.paintfinder.mypalette.controller;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.mypalette.service.MyPaletteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mypal")
@RequiredArgsConstructor
@Slf4j
public class MyPaletteController {

    private final MyPaletteService myPaletteService;

    @ApiOperation(value = "메인화면 추천 팔레트 목록 찾기", notes = "메인화면에 보여줄 팔레트(10개?) 리스트로 출력")
    @GetMapping("/getRandomPaletteList")
    public ResponseEntity getRandomdPaletteList() {

        ResponseDto responseDto = myPaletteService.getRandomPaletteList();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "단일 팔레트 조회", notes = "palette detail 페이지를 위한 palIdx 로 팔레트 정보 조회. palette, paint, pigment(cinum, ciname) 반환")
    @GetMapping("/getMypal")
    public ResponseEntity getMypal(
            @RequestParam int palIdx
    ){
        ResponseDto responseDto = myPaletteService.getMyPalette(palIdx);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
