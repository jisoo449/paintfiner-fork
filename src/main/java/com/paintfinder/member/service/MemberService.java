package com.paintfinder.member.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.member.dto.KakaoTokenDto;
import com.paintfinder.member.dto.KakaoMemberDataDto;
import com.paintfinder.member.dto.requestDto.MemberRequestDto;

import com.paintfinder.member.dto.serviceDto.MemberServiceDto;
import com.paintfinder.member.entity.Member;
import org.springframework.http.ResponseEntity;

public interface MemberService {

	ResponseEntity findAllMember();
	MemberServiceDto findByMemberIdx(int memberIdx);
	ResponseEntity addMember(MemberRequestDto memberRequestDto);
	boolean duplicationCheckByEmail(String email);

	ResponseEntity duplicationCheckByNickname(String nickname);
	ResponseEntity nicknameConvention(String nickname);

	/**
	 * 닉네임 변경 메소드. 닉네임이 변경되면, accessToken 에 저장된 닉네임도 변경되기에
	 * token 을 재발행한다.
	 * @param nickname
	 * @return accessToken, refreshToken
	 */
	ResponseDto updateNickname(int memberIdx, String nickname);
	ResponseEntity updateEmail(String email);
	ResponseDto deleteMember(int memberIdx);
	ResponseEntity addMember(KakaoMemberDataDto kakaoMemberDataDto, KakaoTokenDto kakaoTokenDto, String nickname);
	String createRandomNickname();
	Member findByMemberEmail(String email);
//	boolean deleteByRefreshToken(String refreshToken);

}
