package com.paintfinder.palette_paint.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.mypalette.repository.MyPaletteRepository;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.paint.repository.PaintRepository;
import com.paintfinder.palette_paint.entity.Palette_Paint;
import com.paintfinder.palette_paint.error.Palette_PaintErrorCode;
import com.paintfinder.palette_paint.repository.Palette_PaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Palette_PaintServiceImpl implements Palette_PaintService{

    private final Palette_PaintRepository palettePaintRepository;
    private final PaintRepository paintRepository;
    private final MyPaletteRepository myPaletteRepository;
    private final int maxPaintCount = 100;

    @Override
    public ResponseDto addPalette_Paints(MyPalette myPalette, List<Integer> paintList) {

        // 기존에 저장된 팔레트 내 페인트 개수 카운트
        int existPalettePaintCount = palettePaintRepository.countByMyPalette(myPalette);

        // 페인트 개수가 한도 이상일 경우 문제 발생
        if(paintList.size() + existPalettePaintCount>maxPaintCount) {
            throw new GlobalException(Palette_PaintErrorCode.EXCEEDED_PAINT_MAXIMUM_LIMIT);
        }

        // 등록할 페인트 개수가 없는 경우 아래과정 스킵 후 성공처리
        if(paintList.isEmpty()){
            return ResponseDto.success("등록할 페인트가 없습니다.");
        }

        // TODO : 한 팔레트 내 중복된 페인트를 어떻게 할 것인지 논의 필요.
        // 중복된 페인트가 있을 경우 중복된 페인트 삭제 후 실행
//        List<Integer> distinctPaintList = paintList.stream().distinct().collect(Collectors.toList());

        // 테이블에 하나하나 저장해야됨
        for (int i = 0; i < paintList.size(); i++) {

            //TODO : 저장 도중 없는 idx 일 시 에러 발생시킴. 저장됐던 애들은 저장이 된건지 확인이 필요함.
            // 현재는 하나하나 저장하다가 없는 페인트 저장 시 저장 멈춤.
            Paint paint = paintRepository.findByPaintIdx(paintList.get(i))
                    .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

            Palette_Paint palettePaint = Palette_Paint.builder()
                    .ppPaintOrder(i + existPalettePaintCount + 1)
                    .myPalette(myPalette)
                    .paint(paint)
                    .build();

            palettePaintRepository.save(palettePaint);
        }

        return ResponseDto.success();
    }

    @Override
    public ResponseDto copyPalette_Paints(MyPalette copyMyPalette, int palIdx) {

        MyPalette originMyPalette = myPaletteRepository.findMyPaletteByPalIdx(palIdx)
                .orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

        List<Palette_Paint> paintList = palettePaintRepository.findPalette_PaintsByMyPalette(originMyPalette);

        if(paintList.isEmpty()){
            return ResponseDto.success("등록할 페인트가 없습니다.");
        }

        // 테이블에 하나하나 저장해야됨
        for (int i = 0; i < paintList.size(); i++) {
            Palette_Paint palettePaint = Palette_Paint.builder()
                    .ppPaintOrder(paintList.get(i).getPpPaintOrder())
                    .myPalette(copyMyPalette)
                    .paint(paintList.get(i).getPaint())
                    .build();

            palettePaintRepository.save(palettePaint);
        }

        return ResponseDto.success();
    }
}
