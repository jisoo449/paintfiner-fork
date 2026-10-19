package com.paintfinder.member.service;

import com.paintfinder.member.dto.KakaoTokenDto;
import com.paintfinder.member.dto.KakaoMemberDataDto;

public interface AuthService {
    /**
     *
     * @param code
     * @return
     */
    public KakaoTokenDto getKakaoAccessToken(String code);

    /**
     *
     * @param kakaoTokenDto
     * @return
     */
    public KakaoMemberDataDto getMemberDataFromKakao(KakaoTokenDto kakaoTokenDto);
}
