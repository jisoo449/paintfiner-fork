package com.paintfinder.mypalette.repository;

import com.paintfinder.member.entity.Member;
import com.paintfinder.mypalette.dto.repository.MyPaletteRepositoryDto;
import com.paintfinder.mypalette.dto.repository.RandomMyPaletteRepositoryDto;
import com.paintfinder.mypalette.dto.request.MyPaletteBookmarkDto;
import com.paintfinder.mypalette.dto.request.MyPaletteOpenDto;
import com.paintfinder.mypalette.dto.request.MyPaletteOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyPaletteRepositoryCustom {

    /**
     * 내 팔레트 항목 가져오기
     * @param memberIdx
     * @param pageable
     * @return
     */
    Page<MyPaletteRepositoryDto> findMyPaletteDetail(int memberIdx, Pageable pageable);

    /**
     * 마이팔레트 사용자 지정 정렬 순서 변경
     * @param member
     * @param myPaletteOrderDto
     * @return true false
     */
    boolean updateMypaletteOrder(Member member, MyPaletteOrderDto myPaletteOrderDto);

    /**
     * 마이팔레트 북마크 지정/해제
     * @param member
     * @param myPaletteBookmarkDto
     * @return true false
     */
    int bookmarkMypalette(Member member, MyPaletteBookmarkDto myPaletteBookmarkDto);

    /**
     * 마이팔레트 공개/비공개 설정 레포지토리
     * @param myPaletteOpenDto
     * @return true false
     */
    boolean openMypalette(MyPaletteOpenDto myPaletteOpenDto);

    /**
     * 메인화면용 마이팔레트 랜덤 정렬 리스트 메서드
     * @return
     */
    List<RandomMyPaletteRepositoryDto> findRandomPaletteDetail(List<Integer> randomPaletteIds);

    /**
     * 팔레트 단일 조회
     * @param palIdx
     * @return
     */
    MyPaletteRepositoryDto getMyPaletteByPalIdx(int palIdx);
}
