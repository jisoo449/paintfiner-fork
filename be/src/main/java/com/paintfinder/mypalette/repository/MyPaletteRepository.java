package com.paintfinder.mypalette.repository;

import com.paintfinder.member.entity.Member;
import com.paintfinder.mypalette.entity.MyPalette;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MyPaletteRepository extends JpaRepository<MyPalette, Long>, MyPaletteRepositoryCustom {
    Optional<Integer> countMyPaletteByMember(Member member);

    /**
     * 마이팔레트 리스트 형태로 삭제 메서드. 마이팔레트 인덱스 번호로 접근해 삭제한다. 중계 테이블도 함께 삭제됨.
     * @param myPalIdxList
     */
    @Transactional
    void deleteByPalIdxIn(List<Integer> myPalIdxList);

    Optional<List<MyPalette>> findMyPaletteByPalIdxIn(List<Integer> palIdxList);

    Optional<MyPalette> findMyPaletteByPalIdx(int palIdx);

    /**
     * 메인화면에 출력될 모든 마이팔레트 중 랜덤된 mypalIdx 값을 list로 반환하는 메서드</p>
     * open=1 이며 imgUrl != "" 일 때만 가져올 수 있도록 해야한다
     * @param limit
     * @return mypalIdx list
     */
    @Query(value = "SELECT pal_idx FROM MyPalette WHERE open = 1 AND img_src <> '' ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Integer> findRandomPalettes(int limit);
}
