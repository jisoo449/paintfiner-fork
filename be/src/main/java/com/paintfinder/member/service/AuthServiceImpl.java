package com.paintfinder.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paintfinder.member.dto.KakaoTokenDto;
import com.paintfinder.member.dto.KakaoMemberDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;
    @Value("${KAKAO_REDIRECT_URI}")
    private String KAKAO_REDIRECT_URI;
    @Value("${KAKAO_CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    @Override
    @Transactional
    public KakaoTokenDto getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID); // 카카오 Dev 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI); // 카카오 Dev redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
        params.add("client_secret", KAKAO_CLIENT_SECRET); // 카카오 Dev 카카오 로그인 Client Secret

        // 헤더와 바디 합치기 위해 Http Entity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오로부터 Access token 받아오기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }

    @Override
    @Transactional
    public KakaoMemberDataDto getMemberDataFromKakao(KakaoTokenDto kakaoTokenDto) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoTokenDto.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 후 response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );
        // JSON Parsing (-> KakaoMemberDataDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoMemberDataDto kakaoMemberDataDto = new KakaoMemberDataDto();
        try {
            // json 문자열을 json 객체로 파싱
            JsonNode rootNode = objectMapper.readTree(accountInfoResponse.getBody().toString());

            // "kakao_account" 객체 가져오기
            JsonNode kakaoAccountNode = rootNode.get("kakao_account");

            // 값을 가져오기. 이메일 제외 선택 안함 가능
            kakaoMemberDataDto.setEmail(kakaoAccountNode.get("email").asText());

            // 선택 안했을 시 분기처리
            if(kakaoAccountNode.get("gender") != null){
                kakaoMemberDataDto.setGender(kakaoAccountNode.get("gender").asText());
            }
            if(kakaoAccountNode.get("age_range") != null){
                kakaoMemberDataDto.setAgeRange(kakaoAccountNode.get("age_range").asText());
            }
            if(kakaoAccountNode.get("birthday") != null){
                kakaoMemberDataDto.setBirthday(kakaoAccountNode.get("birthday").asText());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoMemberDataDto;
    }
}
