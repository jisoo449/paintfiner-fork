package com.paintfinder.pigment.service;

import com.paintfinder.pigment.dto.PigmentRequestDto;
import com.paintfinder.pigment.entity.Pigment;
import com.paintfinder.pigment.repository.PigmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PigmentServiceImpl implements PigmentService{

    private final PigmentRepository pigmentRepository;
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity findAllPigment() {
        List<Pigment> pigmentList =pigmentRepository.findAll();
        return new ResponseEntity(pigmentList, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity findByPigmentIdx(int pigmentIdx) {
        Pigment pigment = pigmentRepository.findByPigmentIdx(pigmentIdx);

        if(pigment == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(pigment, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity addPigment(PigmentRequestDto pigmentRequestDto) {
        Pigment pigment = Pigment.builder()
                .ciName(pigmentRequestDto.getCiName())
                .commonName(pigmentRequestDto.getCommonName())
                .ciNumber(pigmentRequestDto.getCiNumber())
                .pigmentColor(pigmentRequestDto.getPigmentColor())
                .opacity(pigmentRequestDto.getOpacity())
                .lightfastness(pigmentRequestDto.getLightfastness())
                .toxicLevel(pigmentRequestDto.getToxicLevel())
                .build();

        try {
            pigmentRepository.save(pigment);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity("Pigment 저장 시 중대한 문제 발생.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity changePigment(PigmentRequestDto pigmentRequestDto) {
        Pigment pigment = pigmentRepository.findByPigmentIdx(pigmentRequestDto.getPigmentIdx());
        if(pigment == null) {
            return new ResponseEntity("Pigment 인덱스를 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        // **** builder 패턴을 사용해 pigment 인스턴스의 idx 값을 유지하며 변경 가능한지 테스트 필요
        pigment.changePigmentInfo(pigmentRequestDto);
        return new ResponseEntity(pigment, HttpStatus.OK);
    }

}
