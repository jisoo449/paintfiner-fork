package com.paintfinder.member.repository;

import com.paintfinder.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAll();
    Optional<Member> findByMemberIdx(int memberIdx);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    void deleteByMemberIdx(int memberIdx);
    Member findMemberByEmail(String email);
}
