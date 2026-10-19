package com.paintfinder.member.dto.responseDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoLoginResponseDto {
    private String grantType;       //JWT에 대한 인증 타입. ex)Bearer 등
    private String accessToken;
    private String refreshToken;
}
