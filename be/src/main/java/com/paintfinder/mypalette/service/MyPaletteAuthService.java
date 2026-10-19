package com.paintfinder.mypalette.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.mypalette.dto.request.*;
import com.paintfinder.mypalette.entity.MyPalette;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MyPaletteAuthService {
    /**
     * 마이팔레트에 memberIdx를 매칭해 저장한다. 사진, 페인트 목록은 제외하고 저장됨.
     * @param memberIdx
     * @param myPaletteRequestDto
     * @return MyPalette 저장된 마이팔레트 엔티티를 리턴
     */
    public ResponseDto<MyPalette> addMyPalette(int memberIdx, MyPaletteRequestDto myPaletteRequestDto);

    /**
     * 사진만 기존의 myPalette 엔티티에 저장됨.
     * @param myPalette
     * @param imgUrl
     * @return
     */
    public ResponseDto addMyPaletteImageUrl(MyPalette myPalette, String imgUrl);

    /**
     * 내 팔레트 목록 가져오기. 페이징 데이터 입력 없을 시 에러발생
     * @param memberIdx
     * @param pageable
     * @return
     */
    public ResponseDto findMyPalettes(int memberIdx, Pageable pageable);

    /**
     * 팔레트 목록 소유주와 접근자가 일치하는지 확인. 불일치시 에러 발생 후 종료됨.
     * @param memberIdx
     * @param myPalIdxList
     * @return
     */
    public void checkMyPaletteOwner(int memberIdx, List<Integer> myPalIdxList);

    /**
     * 내 팔레트 삭제하기
     * @param memberIdx
     * @param myPalIdxList
     * @return
     */
    public ResponseDto deleteMyPalettes(int memberIdx, List<Integer> myPalIdxList);

    /**
     * 내 팔레트 순서 변경하기
     * @param myPaletteOrderDto
     * @return
     */
    public ResponseDto updateMyPaletteOrder(int memberIdx, MyPaletteOrderDto myPaletteOrderDto);

    /**
     * 내 팔레트 북마크하기
     * @param myPaletteBookmarkDto
     * @return
     */
    public ResponseDto bookmarkMyPalette(int memberIdx, MyPaletteBookmarkDto myPaletteBookmarkDto);

    /**
     * 내 팔레트 공개여부 결정하는 서비스
     * @param myPaletteOpenDto
     * @return
     */
    public ResponseDto openMyPalette(MyPaletteOpenDto myPaletteOpenDto);

    /**
     * 내 팔레트 이름 수정하기
     * @param updatePalName, myPalette
     * @return Success or Fail
     */
    public ResponseDto updateMyPaletteName(String updatePalName, MyPalette myPalette);

    /**
     * 내 팔레트 찾기. 팔레트 idx 로 하나의 팔레트를 찾아옴. 멤버 idx가 일치하지 않을 시 에러 발생
     * @param palIdx
     * @return MyPalette
     */
    public MyPalette findMyPalette(int palIdx, int memberIdx);

    /**
     * 내 팔레트 사진 수정하기.
     * @param myPalette
     * @param multipartFile
     * @return
     */
    public ResponseDto updateMyPaletteImg(MyPalette myPalette, MultipartFile multipartFile) throws UnsupportedEncodingException;

    /**
     * 내 팔레트 사진 삭제하기
     * @param myPalette
     * @return
     * @throws UnsupportedEncodingException
     */
    public ResponseDto deleteMyPaletteImg(MyPalette myPalette) throws UnsupportedEncodingException;

    /**
     * 내 팔레트 내 페인트의 순서 변경 로직. 하나의 페인트만 순서 변경 가능
     * @param myPalettePaintOrderDto
     * @return
     */
    public ResponseDto updateMyPalettePaintOrder(MyPalettePaintOrderDto myPalettePaintOrderDto);

    /**
     * 내 팔레트 내 모든 페인트 목록 삭제
     * @param myPalette
     * @return
     */
    public ResponseDto deleteAllMypalettePaints(MyPalette myPalette);

    /**
     * 내 팔레트 내 하나의 페인트만 삭제
     * @param myPalettePaintDeleteDto
     * @return
     */
    public ResponseDto deleteMyPalettePaint(int memberIdx, MyPalettePaintDeleteDto myPalettePaintDeleteDto);

    /**
     * 내 팔레트 복사해 값 채우기
     * @param palIdx
     * @return
     */
    public MyPalette copyMyPalette(int memberIdx, int palIdx) throws Exception;
}
