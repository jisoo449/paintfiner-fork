//참고: https://velog.io/@whereami2048/Spring-Boot-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-feat-JWT-Spring-Security-2
//https://suddiyo.tistory.com/entry/Spring-Spring-Security-JWT-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-2
package com.paintfinder.global.handler;

import com.paintfinder.member.dto.serviceDto.MemberServiceDto;
import com.paintfinder.member.entity.Member;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtTokenHandler {
    private final Key key;

    @Value("${ACCESS_TOKEN_EXPIRE_TIME}")
    private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${REFRESH_TOKEN_EXPIRE_TIME}")
    private long REFRESH_TOKEN_EXPIRE_TIME;
    @Value("${GRANT_TYPE}")
    private String GRANT_TYPE;

//    private final RedisTemplate<String, Object> redisTemplate;

    // application-key.properties에서 secret 값 가져와 key에 저장
    public JwtTokenHandler(@Value("${jwt.secret}") String secretKey){ //, RedisTemplate<String, Object> redisTemplate){
//        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * access Token 생성, 유저 닉네임, 유저 idx, 토큰발행시간, 토큰만료시간 포함됨
     * @return String accessToken
     */
    public String generateAccessToken(MemberServiceDto member){
        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now+ACCESS_TOKEN_EXPIRE_TIME);

        //Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(member.getNickname())
                .claim("memberIdx", member.getMemberIdx())  // jwt 토큰의 payload 에 들어갈 값
                .setIssuedAt(new Date(now)) // 발행 시간(iat)을 현재 시간으로 설정
                .setExpiration(accessTokenExpiresIn) // 토큰 만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 토큰 암호화 키, 방식 설정
                .compact();

        return accessToken;
    }

    /**
     * refresh Token 생성, 유저 idx, 토큰 idx, 만료일이 저장됨.
     * @return String refreshToken
     */
    public String generateRefreshToken(){
        long now = (new Date()).getTime();

        //Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(now)) // 발행 시간(iat)을 현재 시간으로 설정
                .setExpiration(new Date(now+REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    /**
     * 토큰 정보 검증 메서드<p>
     * SecurityException : 토큰 변조시, 서명 키가 맞지 않을 시<p>
     * MalformedJwtException : 토큰이 올바른 JWT 형식을 따르지 않을 시<p>
     * ExpiredJwtException : JWT 토큰이 만료 시<p>
     * UnsupportedJwtException : JWT의 특정 기능이 지원되지 않는 경우 (특정 압축 알고리즘이나 암호화 방식이 지원되지 않을 때)<p>
     * IllegalArgumentException : JWT 토큰 문자열이 null, 빈 문자열일 경우<p>
     * @param token
     * @return true/false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) { 
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // 토큰 복호화 메서드
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * findRefreshToken
     * @param refreshToken
     * @return refeshToken
     * 키값(refresh token)을 가지고 redis에 저장된 userIdx를 가져오는 메서드
     */
//    public String findMemberIdxByRefresh(String refreshToken) {
//        return (String) redisTemplate.opsForValue().get(refreshToken);
//    }

    /**
     * 레디스에 accessToken을 키값으로 refreshToken을 저장.
     * @param memberIdx
     * @param refreshToken
     * @return
     */
//    public void setRedis(int memberIdx, String refreshToken) {
//        redisTemplate.opsForValue().set(refreshToken, String.valueOf(memberIdx));//, refreshTokenExpireTime, TimeUnit.MICROSECONDS);
//    }
}
