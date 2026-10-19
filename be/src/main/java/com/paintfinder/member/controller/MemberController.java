package com.paintfinder.member.controller;

import com.paintfinder.member.dto.KakaoTokenDto;
import com.paintfinder.member.dto.KakaoMemberDataDto;
import com.paintfinder.member.dto.requestDto.KakaoAccessDto;
import com.paintfinder.member.dto.requestDto.MemberRequestDto;
import com.paintfinder.member.dto.responseDto.KakaoLoginResponseDto;
import com.paintfinder.member.dto.serviceDto.MemberServiceDto;
import com.paintfinder.member.entity.Member;
import com.paintfinder.member.service.AuthService;
import com.paintfinder.global.handler.JwtTokenHandler;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paintfinder.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final AuthService authService;
	private final JwtTokenHandler jwtTokenHandler;

	@ApiOperation(value = "유저 전체 검색", notes = "유저 테이블의 모든 내용 검색")
	@GetMapping("/findAll")
	public ResponseEntity findAllMember() {
		return memberService.findAllMember();
	}

	@ApiOperation(value="닉네임 중복체크", notes="중복되는 닉네임인지 확인")
	@PutMapping("/nickname_chk")
	public ResponseEntity duplicationCheckByNickname(@RequestBody MemberRequestDto memberRequestDto){
		return memberService.duplicationCheckByNickname(memberRequestDto.getNickname());
	}

	@ApiOperation(value = "카카오 로그인, 회원가입", notes = "카카오 소셜 회원가입, 회원가입 이력이 있다면 로그인")
	@PostMapping("/accessCode")
	public ResponseEntity<KakaoLoginResponseDto> addMember(@RequestBody KakaoAccessDto kakaoAccessDto) throws Exception{

		//1. 토큰 받아오기
		KakaoTokenDto kakaoTokenDto = authService.getKakaoAccessToken(kakaoAccessDto.getAccessCode());

		//2. 토큰으로 사용자 정보 받아오기
		KakaoMemberDataDto kakaoMemberDataDto = authService.getMemberDataFromKakao(kakaoTokenDto);

		Member member;

		//3. 이메일로 중복 체크. 이미 가입되어 있을 시 사용자 정보 저장 건너뛰기
		if(memberService.duplicationCheckByEmail(kakaoMemberDataDto.getEmail())){
			member = memberService.findByMemberEmail(kakaoMemberDataDto.getEmail());
//			return new ResponseEntity("이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
		}
		//3-1. 사용자 정보 저장
		else {
			String randNickname = memberService.createRandomNickname();
			ResponseEntity memberData = memberService.addMember(kakaoMemberDataDto, kakaoTokenDto, randNickname);
			member = (Member) memberData.getBody();
		}

		MemberServiceDto memberServiceDto = MemberServiceDto.builder()
				.ageRange(member.getAgeRange())
				.memberIdx(member.getMemberIdx())
				.email(member.getEmail())
				.gender(member.getGender())
				.nickname(member.getNickname())
				.build();

		//4. JWT 토큰 발행
		String accessToken = jwtTokenHandler.generateAccessToken(memberServiceDto);
		String refreshToken = jwtTokenHandler.generateRefreshToken();

		//5. JWT refreshToken redis DB 저장
//		jwtTokenHandler.setRedis(memberServiceDto.getMemberIdx(), refreshToken);

		//6. 생성된 토큰 전송
		KakaoLoginResponseDto loginResponseDto = KakaoLoginResponseDto.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();

		return new ResponseEntity(loginResponseDto, HttpStatus.OK);
	}


}