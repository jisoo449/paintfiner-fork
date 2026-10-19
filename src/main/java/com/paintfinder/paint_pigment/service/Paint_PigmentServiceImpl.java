package com.paintfinder.paint_pigment.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.paint_pigment.entity.Paint_Pigment;
import com.paintfinder.paint_pigment.repository.Paint_PigmentRepository;
import com.paintfinder.pigment.entity.Pigment;
import com.paintfinder.pigment.repository.PigmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class Paint_PigmentServiceImpl implements Paint_PigmentService {

    private final Paint_PigmentRepository paintPigmentRepository;
    private final PigmentRepository pigmentRepository;

    @Override
    public ResponseDto addPaint_Pigments(Map<Paint, String[]> paint_pigments) {

        Map<Integer, String> registFailList = new ConcurrentHashMap<>();

        for(Map.Entry<Paint, String[]> entry : paint_pigments.entrySet()) {
            for(int i = 0; i<entry.getValue().length; i++) {

                // 1. pigment ci_name 으로 찾기
                Pigment pigment = pigmentRepository.findFirstByCiName(entry.getValue()[i])
                        .orElse(null);

                // 2. 안료값 ci_number 로 찾기
                if(pigment == null){
                    pigment = pigmentRepository.findFirstByCiNumber(entry.getValue()[i])
                            .orElse(null);
                }

                // 3. ci_name, ci_number 다 없을 경우 피그먼트를 새로 생성하기
                if(pigment == null) {
                    log.info("Paint : {}, Pigment Name : {} not found",entry.getKey(), entry.getValue()[i]);
                    registFailList.put(entry.getKey().getPaintIdx(), entry.getValue()[i]);

                    // 첫 글자가 숫자면 ci_number 가 넘어온 것
                    if(Character.isDigit(entry.getValue()[i].charAt(0))) {
                        pigment = Pigment.builder().ciNumber(entry.getValue()[i]).build();
                    }
                    // 첫 글자가 문자면 ci_name 이 넘어온 것
                    else {
                        pigment = Pigment.builder().ciName(entry.getValue()[i]).build();
                    }

                    pigmentRepository.save(pigment);
                }

                // 피그먼트가 이미 등록되어 있는 지 확인 필요.
                if(paintPigmentRepository.existsByPaintCodeAndPigment(entry.getKey().getPaintCode(), pigment)){ // 존재하면
                    continue;
                } else { // 등록된 중계테이블(페인트, 피그먼트) 가 없으면 등록한다.
                    Paint_Pigment paintPigment = Paint_Pigment.builder()
                            .paintCode(entry.getKey().getPaintCode())
                            .pigment(pigment)
                            .build();

                    paintPigmentRepository.save(paintPigment);
                }

            }
        }
        return ResponseDto.success(registFailList);
    }
}
