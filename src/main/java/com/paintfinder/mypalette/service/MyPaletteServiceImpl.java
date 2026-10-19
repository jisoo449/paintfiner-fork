package com.paintfinder.mypalette.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.mypalette.dto.repository.MyPaletteRepositoryDto;
import com.paintfinder.mypalette.dto.repository.RandomMyPaletteRepositoryDto;
import com.paintfinder.mypalette.repository.MyPaletteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPaletteServiceImpl implements MyPaletteService {

    private final MyPaletteRepository myPaletteRepository;

    @Override
    public ResponseDto getRandomPaletteList() {

        // palIdx 랜덤하게 10개 가져오기
        List<Integer> randomPalettes = myPaletteRepository.findRandomPalettes(10);

        List<RandomMyPaletteRepositoryDto> randomPalette = myPaletteRepository.findRandomPaletteDetail(randomPalettes);

        return ResponseDto.success(randomPalette);
    }

    @Override
    public ResponseDto getMyPalette(int palIdx) {

        MyPaletteRepositoryDto myPaletteRepositoryDto = myPaletteRepository.getMyPaletteByPalIdx(palIdx);

        return ResponseDto.success(myPaletteRepositoryDto);
    }
}
