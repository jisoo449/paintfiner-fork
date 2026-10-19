package com.paintfinder.pigment.controller;

import com.paintfinder.pigment.dto.PigmentRequestDto;
import com.paintfinder.pigment.entity.Pigment;
import com.paintfinder.pigment.service.PigmentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pigment")
@RequiredArgsConstructor
public class PigmentController {

    private final PigmentService pigmentService;

    @ApiOperation(value = "전체 피그먼트", notes = "피그먼트 모든 정보를 가져오는 컨트롤러입니다.")
    @GetMapping("/findAll")
    public ResponseEntity findAllPigment (){

        return pigmentService.findAllPigment();

    }

    @ApiOperation(value = "pigment_idx에 해당하는 피그먼트", notes = "피그먼트 index로 찾는 기능입니다.")
    @GetMapping("/findByPigmentIdx/{pigment_idx}")
    public ResponseEntity findByPigmentIdx(@PathVariable("pigment_idx") int pigment_idx){

        return pigmentService.findByPigmentIdx(pigment_idx);
    }

    @ApiOperation(value = "피그먼트 등록", notes = "피그먼트 정보 등록")
    @PutMapping("/add")
    public ResponseEntity addPigment(@RequestBody PigmentRequestDto pigmentRequestDto){
        return pigmentService.addPigment(pigmentRequestDto);
    }

    @ApiOperation(value = "피그먼트 수정", notes = "피그먼트 수정")
    @PutMapping("/change")
    public ResponseEntity changePigment(@RequestBody PigmentRequestDto pigmentRequestDto){
        return pigmentService.changePigment(pigmentRequestDto);
    }
}
