package com.paintfinder.pigment.service;

import com.paintfinder.pigment.dto.PigmentRequestDto;
import com.paintfinder.pigment.entity.Pigment;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PigmentService {

    ResponseEntity findAllPigment();
    ResponseEntity findByPigmentIdx(int pigment_idx);
    ResponseEntity addPigment(PigmentRequestDto pigmentRequestDto);
    ResponseEntity changePigment(PigmentRequestDto pigmentRequestDto);
}
