package com.paintfinder.member.dto.requestDto;

import lombok.Data;

@Data
public class MemberRequestDto {
    private int memberIdx;
    private String email;
    private String nickname;
}
