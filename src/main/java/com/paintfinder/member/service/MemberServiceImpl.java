package com.paintfinder.member.service;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.global.handler.JwtTokenHandler;
import com.paintfinder.member.dto.KakaoTokenDto;
import com.paintfinder.member.dto.KakaoMemberDataDto;
import com.paintfinder.member.dto.requestDto.MemberRequestDto;
import com.paintfinder.member.dto.responseDto.KakaoLoginResponseDto;
import com.paintfinder.member.dto.serviceDto.MemberServiceDto;
import com.paintfinder.member.entity.Member;
import com.paintfinder.member.error.MemberErrorCode;
import com.paintfinder.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final JwtTokenHandler jwtTokenHandler;

	private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int STRING_LENGTH = 6;
	private static final Random random = new Random();
	private static final int MAX_KOR_LENGTH = 8;
	private static final int MAX_ENG_LENGTH = 15;

//	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity findAllMember() {
		List<Member> memberList = memberRepository.findAll();
		return new ResponseEntity(memberList, HttpStatus.OK);
	}

	/**
	 * Member 엔티티에 저장된 값을 memberIdx로 검색해 MemberServiceDto로 형변환 후 반환.
	 * @param memberIdx
	 * @return MemberServiceDto
	 */
	@Override
	@Transactional(readOnly = true)
	public MemberServiceDto findByMemberIdx(int memberIdx) {
		Member member = memberRepository.findByMemberIdx(memberIdx)
				.orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

		if(member == null){
			return null;
		}

		MemberServiceDto memberServiceDto = MemberServiceDto.builder()
				.email(member.getEmail())
				.memberIdx(member.getMemberIdx())
				.ageRange(member.getAgeRange())
				.gender(member.getGender())
				.nickname(member.getNickname())
				.build();

		return memberServiceDto;
	}

	// 이메일 중복 검사

	@Override
	@Transactional
	public ResponseEntity addMember(MemberRequestDto memberRequestDto) {

		Member newMember = Member.builder()
				.email(memberRequestDto.getEmail())
				.nickname(memberRequestDto.getNickname())
				.build();
		try {
			memberRepository.save(newMember);
			return new ResponseEntity(HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity("에러 발생으로 회원가입 실패", HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * 이미 등록된 이메일이면 true, 신규 이메일이면 false
	 * @param email
	 * @return boolean
	 */
	@Override
	@Transactional
	public boolean duplicationCheckByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}

	@Override
	public ResponseEntity duplicationCheckByNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)){
			return new ResponseEntity("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST);
		}
		else{
			return new ResponseEntity("사용 가능한 닉네임입니다.", HttpStatus.OK);
		}
	}

	@Override
	@Transactional
	public ResponseEntity nicknameConvention(String nickname) {
		// 길이 제한(1~16), 한/영 기반, 특수문자로만 이루어지기 불가(', -, _,  )
		String reg = "^(?=.*[가-힣A-Za-z])[가-힣A-Za-z0-9 -_']{3,16}$";
		Pattern pattern = Pattern.compile(reg);
		if (pattern.matcher(nickname).matches()){
			return new ResponseEntity(HttpStatus.OK);
		}
		else{
			return new ResponseEntity("올바르지 않은 닉네임입니다.",HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseDto updateNickname(int memberIdx, String nickname) {
		Member member = memberRepository.findByMemberIdx(memberIdx).orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

		String engPattern = String.format("^(?! *$)[A-Za-z0-9._ ]{1,%d}$", MAX_ENG_LENGTH);
		String korPattern = String.format("^(?! *$)[가-힣0-9 ]{1,%d}$", MAX_KOR_LENGTH);

		// 닉네임 정합성 검사
		// 공백은 전송안됨
		if(nickname == null){
			return ResponseDto.fail(MemberErrorCode.INVALID_NICKNAME);
		}

		// 닉네임 중복검사
		if(memberRepository.existsByNickname(nickname)){
			return ResponseDto.fail(MemberErrorCode.DUPLICATE_NICKNAME);
		}

		// 닉네임 영어일 시 '.' , '_', ' '만 가능, 20자 제한
		if(nickname.matches(engPattern)){
			member.changeMemberNickname(nickname);
		}

		// 닉네임 한글일 시 8자 제한, 공백 가능
		else if(nickname.matches(korPattern)){
			member.changeMemberNickname(nickname);
		}

		// 유효하지 않은 닉네임일 시 에러로 반환(에러코드2002)
		else {
			return ResponseDto.fail(MemberErrorCode.INVALID_NICKNAME);
		}
		memberRepository.save(member);

		MemberServiceDto memberServiceDto = MemberServiceDto.builder()
				.nickname(member.getNickname())
				.ageRange(member.getAgeRange())
				.email(member.getEmail())
				.gender(member.getGender())
				.memberIdx(member.getMemberIdx()).build();

		// 닉네임이 변경되었으니, accessToken 에 저장된 닉네임에 반영하기 위해 token 재발행
		String accessToken = jwtTokenHandler.generateAccessToken(memberServiceDto);
		String refreshToken = jwtTokenHandler.generateRefreshToken();

		KakaoLoginResponseDto loginResponseDto = KakaoLoginResponseDto.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();

		return ResponseDto.success(loginResponseDto);
	}

	@Override
	public ResponseEntity updateEmail(String email) {
		return null;
	}

	@Override
	@Transactional
	public ResponseDto deleteMember(int memberIdx) {

		Member member = memberRepository.findByMemberIdx(memberIdx)
				.orElseThrow(() -> new GlobalException(ErrorCode.EMPTY_RESULT));

		memberRepository.deleteByMemberIdx(member.getMemberIdx());

		return ResponseDto.success("성공적으로 탈퇴 되었습니다.");
	}

	@Override
	public ResponseEntity addMember(KakaoMemberDataDto kakaoMemberDataDto, KakaoTokenDto kakaoTokenDto, String nickname) {

		if(memberRepository.existsByEmail(kakaoMemberDataDto.getEmail())){
			return new ResponseEntity("이미 가입한 회원입니다.",HttpStatus.OK);
		}

		// 현재 시간을 Asia/Seoul 시간대로 가져옴
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));


		Member member = Member.builder()
				.email(kakaoMemberDataDto.getEmail())
				.nickname(nickname)
				.kakaoAccessToken(kakaoTokenDto.getAccess_token())
				.kakaoRefreshToken(kakaoTokenDto.getRefresh_token())
				.ageRange(kakaoMemberDataDto.getAgeRange())
				.gender(kakaoMemberDataDto.getGender())
				.latestLoginDatetime(currentTime)
				.build();

		try{
			memberRepository.save(member);
		} catch (Exception e) {
			return new ResponseEntity("DB에 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}


		return new ResponseEntity(member, HttpStatus.OK);
	}

	// 랜덤 닉네임 생성 메소드
	@Override
	public String createRandomNickname() {
		String nickname_base = "member_";
		String nickname;

		do {
			StringBuilder sb = new StringBuilder(STRING_LENGTH);
			for (int i = 0; i < STRING_LENGTH; i++) {
				sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
			}
			nickname = nickname_base + sb.toString();
		} while(memberRepository.existsByNickname(nickname));

		return nickname;
	}

	@Override
	public Member findByMemberEmail(String email) {
		return memberRepository.findMemberByEmail(email);
	}

	/**
	 * Redis 테이블에 저장된 refreshToken/memberIdx 를 삭제.
	 * @param refreshToken
	 * @return true/false
	 */
//	@Override
//	public boolean deleteByRefreshToken(String refreshToken) {
//		try {
//			redisTemplate.delete(refreshToken);
//		}
//		catch (IllegalArgumentException e) {
//			System.err.println("Invalid argument passed to Redis: " + e.getMessage());
//			return false;
//		}
//		catch (DataAccessException e) {
//			System.err.println("Failed to delete the refresh token from Redis: " + e.getMessage());
//			return false;
//		}
//		catch (Exception e) {  // 모든 예외를 잡기 위해 Exception 사용
//			System.err.println("Failed to delete the refresh token from Redis: " + e.getMessage());
//			return false;
//		}
//		return true;
//	}
}
