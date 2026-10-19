package com.paintfinder.member.controller;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.handler.JwtTokenHandler;
import com.paintfinder.member.dto.requestDto.MemberRequestDto;
import com.paintfinder.member.dto.responseDto.KakaoLoginResponseDto;
import com.paintfinder.member.dto.responseDto.MemberResponseDto;
import com.paintfinder.member.dto.serviceDto.MemberServiceDto;
import com.paintfinder.member.service.AuthService;
import com.paintfinder.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

// 토큰 값이 필요한 api url의 경우 /auth/member 와 같이 경로에 auth 가 추가된다.
@Slf4j
@RestController
@RequestMapping("/auth/member")
@RequiredArgsConstructor
public class MemberAuthController {
    private final MemberService memberService;
    private final AuthService authService;
    private final JwtTokenHandler jwtTokenHandler;

    @ApiOperation(value = "탈퇴", notes = "회원정보 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity deleteMember(HttpServletRequest request){
        int memberIdx = (int) request.getAttribute("memberIdx");
        ResponseDto responseDto = memberService.deleteMember(memberIdx);
        return new ResponseEntity(responseDto, HttpStatus.OK);
        // TODO : 유저 탈퇴 시 등록한 마이팔레트도 함께 삭제될지, 아니면 보관될 지 생각 필요
    }

    @ApiOperation(value = "유저 정보 검색", notes = "토큰으로 유저 정보 검색")
    @GetMapping("/findMember")
    public ResponseEntity<MemberResponseDto> findMember(HttpServletRequest request){
        MemberServiceDto member = memberService.findByMemberIdx((int) request.getAttribute("memberIdx"));

        System.out.println("파인드유저 컨트롤러 : " + member.getEmail());

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .ageRange(member.getAgeRange())
                .email(member.getEmail())
                .gender(member.getGender())
                .nickname(member.getNickname())
                .build();
        System.out.println("파인드유저 컨트롤러 : " + memberResponseDto.getEmail());
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "토큰 재발행", notes = "refresh 토큰으로 요청 시 토큰 재발행")
    @GetMapping("/refreshToken")
    public ResponseEntity<KakaoLoginResponseDto> refreshToken(HttpServletRequest request){

        String oldRefreshToken = (String) request.getAttribute("refreshToken");

        int memberIdx = (Integer) request.getAttribute("refreshMemIdx"); //Integer.parseInt(jwtTokenHandler.findMemberIdxByRefresh(oldRefreshToken));

        // 유저Idx 로 유저 찾기
        MemberServiceDto memberServiceDto = memberService.findByMemberIdx(memberIdx);

        // 새로운 토큰 발행
        String newAccessToken = jwtTokenHandler.generateAccessToken(memberServiceDto);
        String newRefreshToken = jwtTokenHandler.generateRefreshToken();

        // 오래된 토큰 삭제
        //memberService.deleteByRefreshToken(oldRefreshToken);
        // 새로운 토큰 Redis db 저장
        //jwtTokenHandler.setRedis(memberIdx, newRefreshToken);

        KakaoLoginResponseDto loginResponseDto = KakaoLoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .grantType("Bearer")
                .build();

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "유저 이름(별명)변경", notes = "유저 이름변경 메서드")
    @PutMapping("/UpdateMemberNickname")
    public ResponseEntity UpdateMemberNickname(
            HttpServletRequest request,
            String nickName){

        int memberIdx = (int) request.getAttribute("memberIdx");

        ResponseDto responseDto = memberService.updateNickname(memberIdx, nickName);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

//    @ApiOperation(value = "로그아웃", notes = "로그아웃 실행 시 헤더에 refreshToken으로 전달해주기")
//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutMember(HttpServletRequest request){
//
//        String refreshToken = (String) request.getAttribute("refreshToken");
//
//        boolean success = memberService.deleteByRefreshToken(refreshToken);
//
//            if(success){
//            return new ResponseEntity<>("성공", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("서버에서 문제 발생", HttpStatus.UNAUTHORIZED);
//        }
//
//    }
}